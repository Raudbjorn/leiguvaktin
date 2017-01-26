package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.{BlandScraper, Scraper}

/**
  * Created by sveinbjorn on 12.1.2017.
  */
class BlandController extends Controller with Scraper {
  def scrape = Action {
    Ok(Json.obj("Apartments" -> BlandScraper.scrape()))
  }
}
