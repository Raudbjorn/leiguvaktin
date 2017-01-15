package services

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.{Document, Element}

/**
  * Created by sveinbjorn on 12.1.2017.
  */
object BlandScraper extends Scraper {
  private val getPage: Int => Document = pageNo => getPage(s"https://bland.is/classified/?categoryId=59&sub=1&sortby=latest&size=200&page=$pageNo")
  private val zipRegex = "[0-9]{3} ".r

  val parseApartment: Element => Apartment = info => {
    val address = info >> text("h3")
    val link = "https://bland.is" + (info >> element("h3") >> attr("href")("a"))

    val possibleZip = (info >?> element(".classifiedAddress") >> text("a")).getOrElse("?")
    val zip = zipRegex.findFirstIn(possibleZip).getOrElse("?").trim

    val details = info >> element(".featuredPropertyWrapper")
    val rooms = details >> element(".featuredProperty:nth-child(3)") >> text(".featuredPropertyValue")
    val size = details >> element(".featuredProperty.featuredPropertyRight") >> text(".featuredPropertyValue")

    val price = info >> element(".priceTime") >> text("p")

    Apartment(address, zip, rooms, size, price, link)
  }

  def scrape(): List[Apartment] = {
    Stream.from(1).map(getPage).map(d => d >> elementList("#classifiedContainer")).takeWhile(_.nonEmpty)
      .flatten.map(e => parseApartment(e)).toList
  }

}
