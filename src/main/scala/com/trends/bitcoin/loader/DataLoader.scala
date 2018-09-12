package com.trends.bitcoin.loader

import java.text.SimpleDateFormat
import com.trends.bitcoin.Schema.{BitCoin, Price}
import com.typesafe.config.ConfigFactory
import org.json4s.native.JsonMethods.parse
import org.json4s.{DefaultFormats, _}

object DataLoader {

  implicit val formats: DefaultFormats.type = DefaultFormats
  private val timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  private val data: String = scala.io.Source.fromURL(ConfigFactory.load.getString("bitcoin.url")).mkString
  val bitcoinData: List[Price] = parse(data).extract[BitCoin].data.prices.map(
    x => Price(x.price.toDouble, x.time, timestampFormat.parse(x.time).getTime)
  )

}
