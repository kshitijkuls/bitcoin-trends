package com.trends.bitcoin.server

package object routes {

  case class PriceMovementByDateRequest(date: String)

  case class MovingAvgRequest(startDate: String, endDate: String, period: Int)

  case class ForecastPriceRequest(days: Int)

}
