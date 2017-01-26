package controllers

import play.api.libs.json.Json
import play.api.mvc._
import services.{LeigulistinnScraper, Scraper}

/**
  * Created by sveinbjorn on 15.1.2017.
  */
class LeigulistinnController extends Controller with Scraper{
  def scrape = Action {
    Ok(Json.obj("Apartments" -> LeigulistinnScraper.scrape()))
  }
}
