package com.trends.bitcoin.server.routes

import com.trends.bitcoin.Schema.{MovingPrice, Price}
import com.trends.bitcoin.loader.DataLoader
import com.trends.bitcoin.server.services.HistoricalService
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{Endpoint, Ok, get, jsonBody, post}
import io.finch._

trait HistoricalRoutes extends LazyLogging {

  private val lastWeekAPI = baseAPIPath :: "last_week"
  val lastWeek: Endpoint[List[Price]] = get(lastWeekAPI) {
    logger.info(s"$lastWeekAPI API called")
    Ok(HistoricalService(DataLoader.bitcoinData).lastWeekMovement)
  }

  private val lastMonthAPI = baseAPIPath :: "last_month"
  val lastMonth: Endpoint[List[Price]] = get(lastMonthAPI) {
    logger.info(s"$lastMonthAPI API called")
    Ok(HistoricalService(DataLoader.bitcoinData).lastMonthMovement)
  }

  private val priceMovementByDateAPI = baseAPIPath :: "price_movement_by_date"
  val priceMovementByDate: Endpoint[List[Price]] =
    post(priceMovementByDateAPI :: jsonBody[PriceMovementByDateRequest]) { req: PriceMovementByDateRequest =>
      logger.info(s"$priceMovementByDateAPI API called")
      Ok(HistoricalService(DataLoader.bitcoinData).getPriceMovementByDate(req.date))
    }

  private val movingAvgAPI = baseAPIPath :: "moving_avg"
  val movingAvgBetweenDates: Endpoint[List[MovingPrice]] =
    post(movingAvgAPI :: jsonBody[MovingAvgRequest]) { req: MovingAvgRequest =>
      logger.info(s"$movingAvgAPI API called")
      Ok(HistoricalService(DataLoader.bitcoinData).getMovingAvgBetweenDates(req.startDate, req.endDate, req.period))
    }
}
