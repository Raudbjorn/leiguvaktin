package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, _}
import services.{Scraper, VisirScraper}

/**
  * Created by sveinbjorn on 11.1.2017.
  */
class VisirController extends Controller with Scraper {
  def scrape = Action {
    Ok(Json.obj("Apartments" -> VisirScraper.scrape()))
  }
}
