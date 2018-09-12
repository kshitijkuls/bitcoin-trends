package com.trends.bitcoin.server.services

import java.text.SimpleDateFormat
import java.util.Calendar

import com.trends.bitcoin.Schema._
import com.trends.bitcoin.loader.DataLoader

class HistoricalService(bitcoinData: List[Price]) {

  private val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")

  lazy val lastWeekMovement: List[Price] = filterDataByWindow(TimeWindow(Calendar.WEEK_OF_MONTH, -1))

  lazy val lastMonthMovement: List[Price] = filterDataByWindow(TimeWindow(Calendar.MONTH, -1))

  def getPriceMovementByDate(date: String): List[Price] =
    DataLoader.bitcoinData.filter(_.epochTime >= simpleDateFormat.parse(date).getTime)

  def getMovingAvgBetweenDates(startDate: String, endDate: String, period: Int): List[MovingPrice] = {
    val selectedData: List[Price] = bitcoinData.filter(x =>
      simpleDateFormat.parse(startDate).getTime <= x.epochTime &&
        x.epochTime <= simpleDateFormat.parse(endDate).getTime
    ).sortBy(_.epochTime)

    (selectedData zip movingAverage(selectedData.map(_.price), period))
      .map { case (y, price) => MovingPrice(price, y.time, y.epochTime) }
  }

  private def filterDataByWindow(tw: TimeWindow): List[Price] = {
    bitcoinData.filter(_.epochTime >= prepareEpoch(tw))
  }

  private def movingAverage(values: List[Double], period: Int): List[Double] = {
    val first = (values take period).sum / period
    val subtract = values map (_ / period)
    val add = subtract drop period
    val addAndSubtract = add zip subtract map Function.tupled(_ - _)
    val res = addAndSubtract.foldLeft(first :: List.fill(period - 1)(0.0)) {
      (acc, add) => (add + acc.head) :: acc
    }.reverse
    res
  }

  private def prepareEpoch(tw: TimeWindow) = {
    val dd = Calendar.getInstance()
    dd.set(Calendar.HOUR_OF_DAY, 0)
    dd.set(Calendar.MINUTE, 0)
    dd.set(Calendar.SECOND, 0)
    dd.set(Calendar.MILLISECOND, 0)
    dd.add(tw.frame, tw.window)
    dd.getTime.getTime
  }
}

object HistoricalService {
  def apply(bitcoinData: List[Price]): HistoricalService = new HistoricalService(bitcoinData)
}