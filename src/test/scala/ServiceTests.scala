import com.trends.bitcoin.Schema.{MovingPrice, Price}
import com.trends.bitcoin.loader.DataLoader
import com.trends.bitcoin.server.services.HistoricalService
import org.scalatest.FunSuite
import org.scalatest.mockito.MockitoSugar

class ServiceTests extends FunSuite with MockitoSugar {

  val sample = "{\"data\":{\"base\":\"BTC\",\"currency\":\"USD\",\"prices\":[{\"price\":\"6231.21\",\"time\":\"2018-09-09T00:00:00Z\"},{\"price\":\"6362.43\",\"time\":\"2018-09-08T00:00:00Z\"},{\"price\":\"6445.20\",\"time\":\"2018-09-07T00:00:00Z\"},{\"price\":\"6422.41\",\"time\":\"2018-09-06T00:00:00Z\"},{\"price\":\"7126.16\",\"time\":\"2018-09-05T00:00:00Z\"},{\"price\":\"7318.00\",\"time\":\"2018-09-04T00:00:00Z\"},{\"price\":\"7258.19\",\"time\":\"2018-09-03T00:00:00Z\"}]}}"
  test("Test if getting parsed successfully") {
    val prices = DataLoader.parseJsonResponse(sample)
    assert(prices.size == 7)

    val expectedPriceList = List(6231.21,
      6362.43,
      6445.2,
      6422.41,
      7126.16,
      7318.0,
      7258.19)

    assert(prices.map(_.price).sorted == expectedPriceList.sorted)
  }

  test("Historical Service should get return correct set for the date provided by the user") {
    val actual = HistoricalService(DataLoader.parseJsonResponse(sample)).getPriceMovementByDate("2018-09-09")

    val expected = List(
      Price(6250.88, "2018-09-12T00:00:00Z", 1536685200000l),
      Price(6291.08, "2018-09-11T00:00:00Z", 1536598800000l),
      Price(6287.91, "2018-09-10T00:00:00Z", 1536512400000l),
      Price(6293.85, "2018-09-09T00:00:00Z", 1536426000000l)
    )

    assert(actual.diff(expected).isEmpty)
    assert(expected.diff(actual).isEmpty)
  }

  test("Historical Service should get return correct moving average set for the date provided by the user") {
    val actual = HistoricalService(DataLoader.parseJsonResponse(sample))
      .getMovingAvgBetweenDates("2018-09-07", "2018-09-12", 3)

    val expected = List(
      MovingPrice(0.0, "2018-09-07T00:00:00Z", 1536253200000l),
      MovingPrice(0.0, "2018-09-08T00:00:00Z", 1536339600000l),
      MovingPrice(6346.28, "2018-09-09T00:00:00Z", 1536426000000l)
    )

    assert(actual.diff(expected).isEmpty)
    assert(expected.diff(actual).isEmpty)
  }

  test("Historical Service should get return correct set for the start and end date provided by the user") {
    val actualD = HistoricalService(DataLoader.parseJsonResponse(sample))
      .filterDataByStartEndDate("2018-09-04", "2018-09-08")
    actualD.foreach(println)
    
    println()
    val actual = HistoricalService(DataLoader.parseJsonResponse(sample))
      .getMaxPriceByBucket("2018-09-04", "2018-09-08", 3)
    actual.foreach(println)
  }
}
