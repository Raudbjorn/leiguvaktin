package controllers

import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc._
import services.{Apartment, MblScraper, Scraper, VisirScraper}

/**
  * Created by erept on 11.1.2017.
  */
class VisirController extends Controller with Scraper {
  def scrape = Action {
    implicit val apartmentFormat = Json.format[Apartment]
    Ok(Json.obj("Apartments" -> VisirScraper.scrape()))
  }
}
