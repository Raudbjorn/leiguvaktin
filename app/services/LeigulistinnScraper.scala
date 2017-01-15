package services

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element

/**
  * Created by sveinbjorn on 15.1.2017.
  */
object LeigulistinnScraper extends Scraper {
  private val url = "http://leigulistinn.is/synishorn"
  private val roomsRegex = "[0-9]* Herb".r
  private val fixRooms: Element => String = {
    case r if r.text == "Herbergi" => "1"
    case r if roomsRegex.findFirstIn(r.text).isDefined => roomsRegex.findFirstIn(r.text.trim).get.replace(" Herb", "")
    case _ => "?"
  }

  val parseApartment: Element => Apartment = info => {
    val details = info >> elementList("td")
    val address = details.lift(1).map(i => i.text).getOrElse("?")
    val zip = details.lift(2).map(i => i.text).getOrElse("?")
    val rooms = details.lift(3).map(fixRooms).getOrElse("?")
    val size = details.lift(4).map(i => i.text).getOrElse("?")
    val price = details.lift(5).map(i => i.text).getOrElse("?")

    Apartment(address, zip, rooms, size, price, url)
  }

  def scrape(): List[Apartment] = {
    val data = getPage(url) >> element("#reseter") >> elementList("tr")
    data.map(e => parseApartment(e))
  }

}
