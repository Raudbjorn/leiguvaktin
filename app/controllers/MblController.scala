package controllers

import play.api.mvc.Action
import play.api.libs.json._
import play.api.mvc._
import services.{Apartment, MblScraper, Scraper}


/**
  * Created by erept on 10.1.2017.
  */
class MblController extends Controller with Scraper {

  def getAll = Action {
    implicit val apartmentFormat = Json.format[Apartment]
    Ok(Json.obj("Apartments" -> MblScraper.scrape()))
  }
}
