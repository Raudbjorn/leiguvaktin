package controllers

import play.api.libs.json.Json
import services.{Apartment, LeigulistinnScraper, Scraper}
import play.api.mvc._

/**
  * Created by sveinbjorn on 15.1.2017.
  */
class LeigulistinnController extends Controller with Scraper{
  def scrape = Action {
    implicit val apartmentFormat = Json.format[Apartment]
    Ok(Json.obj("Apartments" -> LeigulistinnScraper.scrape()))
  }
}
