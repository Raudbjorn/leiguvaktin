package services

import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}

/**
  * Created by sveinbjorn on 14.1.2017.
  */
object LeigaIsScraper extends Scraper {
  private val getPage: Int => Document = pageNo => getPage(s"http://leiga.is/eignir?page=$pageNo&order=date&property=0")

  private val zipRegex = "^[0-9]{3} ".r
  private val roomsRegx = "^[0-9]* herbergi".r
  private val sizeRegex = "[0-9]* m2".r

  val parseApartment: Element => Apartment = info => {
    val address = info >> element("a") >> text("strong")
    val dirtyZip = info >> element("span") >> text("em")
    val zip = zipRegex.findFirstIn(dirtyZip).getOrElse("?").trim
    val details = (info >> text(".detail")).split('|')
    val rooms = details.headOption.map(r => roomsRegx.findFirstIn(r).getOrElse("?")).getOrElse("?").replace(" herbergi", "")
    val size = details.lift(1).map(r => sizeRegex.findFirstIn(r).getOrElse("?")).getOrElse("?").replace(" m2", "")

    val price = Option(info >> text(".price")).filter(_.trim.nonEmpty).getOrElse("?")
    val link = "http://leiga.is" + (info >> attr("href")("a"))

    Apartment(address, zip, rooms, size, price, link)
  }

  def scrape(): List[Apartment] = {
    val handleNoContainers: Document => List[Element] = d => (d >?> element(".list-container") >> elementList("li")).getOrElse(List())
    Stream.from(1).map(getPage).map(handleNoContainers).takeWhile(_.nonEmpty)
      .flatten.map(e => parseApartment(e)).toList
  }
}
