package controllers

import play.api.libs.json._
import play.api.mvc.{Action, _}
import services.{MblScraper, Scraper}


/**
  * Created by sveinbjorn on 10.1.2017.
  */
class MblController extends Controller with Scraper {

  def scrape = Action {
    Ok(Json.obj("Apartments" -> MblScraper.scrape()))
  }
}
