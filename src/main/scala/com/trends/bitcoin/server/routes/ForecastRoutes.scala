package com.trends.bitcoin.server.routes

import com.trends.bitcoin.Schema.ForecastedPrice
import com.trends.bitcoin.loader.DataLoader
import com.trends.bitcoin.server.services.ForecastService
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._
import io.finch.circe._
import io.finch._
import io.finch.{Endpoint, Ok, get, jsonBody, post}

trait ForecastRoutes extends LazyLogging {

  private val forecast15DaysAPI = baseAPIPath :: "forecast_15_days"
  val forecast15Days: Endpoint[List[ForecastedPrice]] = get(forecast15DaysAPI) {
    logger.info(s"$forecast15DaysAPI API called")
    Ok(ForecastService(DataLoader.bitcoinData).forecast(15))
  }

  private val forecastAPI = baseAPIPath :: "forecast"
  val forecast: Endpoint[List[ForecastedPrice]] =
    post(forecastAPI :: jsonBody[ForecastPriceRequest]) { req: ForecastPriceRequest =>
      logger.info(s"$forecastAPI API called")
      Ok(ForecastService(DataLoader.bitcoinData).forecast(req.days))
    }
}
