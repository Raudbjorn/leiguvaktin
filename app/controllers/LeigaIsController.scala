package controllers

import play.api.libs.json.Json
import play.api.mvc._
import services.{Apartment, LeigaIsScraper, Scraper}

/**
  * Created by sveinbjorn on 15.1.2017.
  */
class LeigaIsController extends Controller with Scraper {
  def scrape = Action {
    implicit val apartmentFormat = Json.format[Apartment]
    Ok(Json.obj("Apartments" -> LeigaIsScraper.scrape()))
  }
}
