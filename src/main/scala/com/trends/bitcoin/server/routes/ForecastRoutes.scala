package com.trends.bitcoin.server.routes

import com.trends.bitcoin.Schema.ForecastedPrice
import com.trends.bitcoin.server.services.ForecastService
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{Endpoint, Ok, get, jsonBody, post}

trait ForecastRoutes {

  val forecast15Days: Endpoint[List[ForecastedPrice]] = get("forecast_15_days") {
    Ok(ForecastService.forecast(15))
  }

  val forecast: Endpoint[List[ForecastedPrice]] =
    post("forecast" :: jsonBody[ForecastPriceRequest]) { req: ForecastPriceRequest =>
      Ok(ForecastService.forecast(req.days))
    }
}
