package com.trends.bitcoin

object Schema {

  case class Message(message: String)

  case class _Price(price: String, time: String)

  case class Data(base: String, currency: String, prices: List[_Price])

  case class BitCoin(data: Data)

  case class Price(price: Double, time: String, epochTime: Long)

  case class MovingPrice(movingPrice: Double, time: String, epochTime: Long)

  case class TimeWindow(frame: Int, window: Int)

  case class ForecastedPrice(date: String, price: Double)
}