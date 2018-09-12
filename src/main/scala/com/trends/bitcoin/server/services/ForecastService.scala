package com.trends.bitcoin.server.services

import java.time.{ZoneId, ZonedDateTime}

import com.cloudera.sparkts.models.ARIMA
import com.cloudera.sparkts.{DateTimeIndex, DayFrequency, TimeSeriesRDD}
import com.trends.bitcoin.Schema.{ForecastedPrice, Price}
import com.trends.bitcoin.loader.{DataLoader, SparkEngine}
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.TimestampType
import org.apache.spark.sql.{Dataset, Row}
import org.joda.time.DateTime

class ForecastService(bitcoinData: List[Price]) extends SparkEngine with LazyLogging {

  import spark.sqlContext.implicits._

  val symbolColumn = "symbol"
  val timeColumn = "time"
  val priceColumn = "price"
  val predictedColumn = "values"

  def forecast(days: Int): List[ForecastedPrice] = {

    logger.info(s"Forecasting days requested: $days")
    val bitcoinDf = spark.sparkContext.parallelize(bitcoinData).toDF()
      .withColumn(symbolColumn, lit("bitcoin"))
      .withColumn(timeColumn, col(timeColumn) cast TimestampType)
      .sort(timeColumn)

    val tsRdd: TimeSeriesRDD[String] = TimeSeriesRDD.timeSeriesRDDFromObservations(
      targetIndex = dtIndex(bitcoinDf),
      df = bitcoinDf,
      tsCol = timeColumn,
      keyCol = symbolColumn,
      valueCol = priceColumn
    )

    val df = tsRdd.mapSeries { vector => {
      val newVec = new DenseVector(vector.toArray.map(x => if (x.equals(Double.NaN)) 0 else x))
      val arimaModel = ARIMA.fitModel(1, 0, 0, newVec)
      val forecasted = arimaModel.forecast(newVec, days)
      new DenseVector(forecasted.toArray.slice(forecasted.size - (days + 1), forecasted.size - 1))
    }
    }.toDF(symbolColumn, predictedColumn)

    val forecastDates = (for (f <- 1 to days) yield new DateTime().plusDays(f)).map(_.toString("yyyy-MM-dd"))
    (forecastDates zip df.select(predictedColumn).collect().head.get(0).asInstanceOf[DenseVector].values)
      .map { case (date, price) => ForecastedPrice(date, price) }.toList
  }

  private def dtIndex(bitcoinDf: Dataset[Row]) = {
    val minDate = bitcoinDf.select(min(timeColumn)).collect()(0).getTimestamp(0)
    val maxDate = bitcoinDf.select(max(timeColumn)).collect()(0).getTimestamp(0)

    logger.debug(s"Training data min max dates: $minDate and $maxDate")

    val zone = ZoneId.systemDefault()
    val dtIndex = DateTimeIndex.uniformFromInterval(
      ZonedDateTime.of(minDate.toLocalDateTime, zone),
      ZonedDateTime.of(maxDate.toLocalDateTime, zone),
      new DayFrequency(1)
    )
    dtIndex
  }
}

object ForecastService {
  def apply(bitcoinData: List[Price]): ForecastService = new ForecastService(bitcoinData)
}
