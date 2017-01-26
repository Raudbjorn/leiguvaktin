package services

import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.elementList
import services.Apartment._

/**
  * Created by sveinbjorn on 11.1.2017.
  */
object VisirScraper extends Scraper {

  private val getPage: Int => Document = pageNo => getPage(s"http://fasteignir.visir.is/ajaxsearch/getresults?stype=rent&itemcount=60&page=$pageNo")
  private val zipRegex = ", [0-9]{3}".r
  private val searchIdRegex = "search_id=[0-9]*"
  private val roomsRegex = "[0-9]* herb.".r
  private val sizeRegex = "[0-9]* m²".r

  val parseApartment: Element => Apartment = info => {
    val dirtyAddress = info >> element("h2") >> text("a")
    val address = dirtyAddress.takeWhile(c => c != ',')
    val zip = zipRegex.findFirstIn(dirtyAddress).getOrElse("?").replace(", ", "")

    val details = info >> element(".b-products-item-details-param")
    val rooms = roomsRegex.findFirstIn(details >> text("table")).getOrElse("?").replace(" herb.", "")
    val size = sizeRegex.findFirstIn(details >> text("table")).getOrElse("?").replace(" m²", "")

    val link = "http://fasteignir.visir.is" + (info >> element("h2") >> attr("href")("a")).replaceAll(searchIdRegex, "")
    val price = info >> element("tbody") >> element("tr") >> element("td") >> text("strong")
    Apartment(address, zip, rooms, size, price, link)
  }

  def scrape(): List[Apartment] = {
    Stream.from(1).map(getPage).map(d => d >> elementList(".b-products-item-details")).takeWhile(_.nonEmpty)
      .flatten.map(e => parseApartment(e)).toList
  }

}