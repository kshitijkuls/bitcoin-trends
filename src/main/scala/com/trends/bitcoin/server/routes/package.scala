package com.trends.bitcoin.server

import io.finch._
import shapeless.HNil

package object routes {

  val baseAPIPath: Endpoint[HNil] = "api" :: "v1"

  case class PriceMovementByDateRequest(date: String)

  case class MovingAvgRequest(startDate: String, endDate: String, period: Int)

  case class BucketMaxPriceRequest(startDate: String, endDate: String, bucket: Int)

  case class ForecastPriceRequest(days: Int)

}
