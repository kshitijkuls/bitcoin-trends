package com.trends.bitcoin.server.routes

import com.trends.bitcoin.Schema.{MovingPrice, Price}
import com.trends.bitcoin.server.services.Service
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{Endpoint, Ok, get, jsonBody, post}

trait Routes {

  val lastWeek: Endpoint[List[Price]] = get("last_week") {
    Ok(Service.lastWeekMovement)
  }

  val lastMonth: Endpoint[List[Price]] = get("last_month") {
    Ok(Service.lastMonthMovement)
  }

  val priceMovementByDate: Endpoint[List[Price]] =
    post("price_movement_by_date" :: jsonBody[PriceMovementByDateRequest]) { req: PriceMovementByDateRequest =>
    Ok(Service.getPriceMovementByDate(req.date))
  }

  val movingAvgBetweenDates: Endpoint[List[MovingPrice]] =
    post("moving_avg" :: jsonBody[MovingAvgRequest]) { req: MovingAvgRequest =>
      Ok(Service.getMovingAvgBetweenDates(req.startDate, req.endDate, req.period))
    }
}
