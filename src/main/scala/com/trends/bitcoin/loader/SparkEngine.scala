package com.trends.bitcoin.loader

import org.apache.spark.sql.SparkSession

trait SparkEngine {

  implicit val spark: SparkSession = SparkSession
    .builder()
    .master("local[*]")
    .appName("Bitcoin  Preprocessing")
    .getOrCreate()
}
