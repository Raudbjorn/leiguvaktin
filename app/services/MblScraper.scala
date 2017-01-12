package services

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import scala.util.matching.Regex
import net.ruippeixotog.scalascraper.model.{Document, Element}

/**
  * Created by sg on 10.1.2017.
  */
object MblScraper extends Scraper {
  val url = "http://www.mbl.is/fasteignir/leiga/leit/?type=&min_rooms=&max_rooms=&min_price=&max_price=&text_query="
  val zipRegex = ", [0-9]{3}".r
  val roomRegex = "[0-9]* herbergi".r
  val sizeRegex = "[0-9]* fm".r
  val getPage: Int => Document = pageNo => getPage(s"http://www.mbl.is/fasteignir/leiga/leit/?page=$pageNo&type=&min_rooms=&max_rooms=&min_price=&max_price=&text_query=")

  val parseApartment: Element => Apartment = info => {
    val dirtyAddress = info >> element(".rental-itemlist-headline")
    val address = dirtyAddress.text.takeWhile(c => c != ',')
    val zip = zipRegex.findFirstIn(dirtyAddress.text).getOrElse("?").replace(", ", "")
    val link = "http://www.mbl.is" + dirtyAddress.attr("href")
    val dirtyInfo = (info >> element(".rental-itemlist-maininfo")).text
    val rooms = roomRegex.findFirstIn(dirtyInfo).getOrElse("?").replace(" herbergi", "")
    val size = sizeRegex.findFirstIn(dirtyInfo).getOrElse("?").replace(" fm", "")
    val price = (info >> element(".rental-itemlist-price")).text
    Apartment(address, zip, rooms, size, price, link)
  }

  def scrape(): List[Apartment] = {
    Stream.from(1).map(getPage).map(d => d >> elementList(".rental-itemlist-property.clear")).takeWhile(_.nonEmpty)
      .flatten.map(e => parseApartment(e)).toList
  }
}
