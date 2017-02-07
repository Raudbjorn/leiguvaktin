package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sveinbjorn on 11.1.2017.
  */
class RootController extends Controller with Scraper {

  def scrape = Action.async {
    implicit val baseTime = System.currentTimeMillis

    val result = for {
      mbl <- Future(MblScraper.scrape())
      visir <-  Future(VisirScraper.scrape())
      bland <- Future(BlandScraper.scrape())
      leiga <- Future(LeigaIsScraper.scrape())
      leiguListinn <- Future(LeigulistinnScraper.scrape())
    } yield mbl ++ visir ++ bland ++ leiga ++ leiguListinn

    result.map(r => Ok(Json.obj("Apartments" -> r)))
  }
}
