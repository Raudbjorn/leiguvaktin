package services

import play.api.libs.json.{JsObject, Json, Writes}
import services.Apartment.{Price, Rooms, Size, Zip}

import scala.util.Try

/**
  * Created by sveinbjorn on 10.1.2017.
  */
object Apartment {


  class Rooms(val roomsStr: String) extends AnyVal {
    def format(): Int = Try(roomsStr.toInt).toOption.getOrElse(0)

    override def toString: String = roomsStr
  }

  class Size(val sizeStr: String) extends AnyVal {
    def format(): Int = Try(sizeStr.toInt).toOption.getOrElse(0)

    override def toString: String = sizeStr
  }

  class Zip(val zipStr: String) extends AnyVal {
    def format(): Int = Try(zipStr.toInt).toOption.getOrElse(0)

    override def toString: String = zipStr
  }


  class Price(val priceStr: String) extends AnyVal {
    private def formatPrice(formatString: String) = {
      formatString.trim.toLowerCase
        .replace(".", "")
        .replace(" þús", "000")
        .replaceAll("-.*$", "")
        .replaceAll("[^0-9.]", "")
    }

    def format(): BigDecimal = {
      val formattedPrice = formatPrice(priceStr)
      if (formattedPrice.isEmpty) BigDecimal(0.00) else BigDecimal(formattedPrice)
    }

    override def toString: String = priceStr
  }

  implicit def priceFromString(priceStr: String): Price = new Price(priceStr)

  implicit def sizeFromString(sizeStr: String): Size = new Size(sizeStr)

  implicit def roomsFromString(sizeStr: String): Rooms = new Rooms(sizeStr)

  implicit def zipFromString(zipStr: String): Zip = new Zip(zipStr)

  implicit val apartmentWrites = new Writes[Apartment] {
    def writes(apartment: Apartment): JsObject = Json.obj(
      "address" -> apartment.address,
      "zip" -> apartment.zip.format(),
      "rooms" -> apartment.rooms.format(),
      "size" -> apartment.size.format(),
      "price" -> apartment.price.format(),
      "link" -> apartment.link
    )
  }
}

case class Apartment(address: String, zip: Zip, rooms: Rooms, size: Size, price: Price, link: String) {}

