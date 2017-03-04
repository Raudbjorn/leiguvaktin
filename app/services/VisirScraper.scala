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
    val address = (info >?> element(".property__title") >> element("h2") >> text("a")).getOrElse("")
    val zip = (info >?> text(".property__postalcode")).getOrElse("").trim.takeWhile(_.isDigit)
    val rooms = (info >?> text(".property__arrangement")).getOrElse("").trim.takeWhile(_.isDigit)
    val size = (info >?> text(".property__size")).getOrElse("").trim.takeWhile(_.isDigit)
    val price = (info >?> text(".property__price")).getOrElse("").trim
    val link = "http://fasteignir.visir.is" + (info >?> element(".property__title") >> element("h2") >> attr("href")("a")).getOrElse("").replaceAll(searchIdRegex, "")
    Apartment(address, zip, rooms, size, price, link)
  }

  def scrape(): List[Apartment] = {
    Stream.from(1).map(getPage).map(d => d >> elementList(".property__inner")).takeWhile(_.nonEmpty)
      .flatten.map(e => parseApartment(e)).toList
  }

}