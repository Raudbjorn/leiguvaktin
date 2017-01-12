package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.{Apartment, BlandScraper, Scraper}

/**
  * Created by sveinbjorn on 12.1.2017.
  */
class BlandController extends Controller with Scraper {
  def scrape = Action {
    implicit val apartmentFormat = Json.format[Apartment]
    Ok(Json.obj("Apartments" -> BlandScraper.scrape()))
  }
}
