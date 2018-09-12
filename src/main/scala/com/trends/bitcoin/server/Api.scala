package com.trends.bitcoin.server

import com.trends.bitcoin.Schema.Message
import com.trends.bitcoin.server.routes.{ForecastRoutes, HistoricalRoutes}
import com.twitter.finagle.Http
import com.twitter.util.Await
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{Application, Endpoint, InternalServerError, jsonBody}

object Api
  extends HistoricalRoutes
    with ForecastRoutes {

  val endpoints =
    lastWeek :+:
      lastMonth :+:
      priceMovementByDate :+:
      movingAvgBetweenDates :+:
      forecast :+:
      forecast15Days

  def acceptedMessage: Endpoint[Message] = jsonBody[Message]

  val api = endpoints.handle {
    case e: Exception => InternalServerError(e)
  }

  def main(args: Array[String]): Unit = {
    val port = 8089
    println(s"Serving the application on port $port")
    val server = Http.server.serve(s":$port", api.toServiceAs[Application.Json])
    Await.ready(server)
  }
}
