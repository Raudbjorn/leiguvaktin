package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.{Apartment, MblScraper, Scraper, VisirScraper}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * Created by erept on 11.1.2017.
  */
class RootController extends Controller with Scraper {

  def scrape = Action {
    implicit val apartmentFormat = Json.format[Apartment]
    implicit val baseTime = System.currentTimeMillis

    val mblProcess: Future[List[Apartment]] = Future {
      MblScraper.scrape()
    }

    val visirProcess: Future[List[Apartment]] = Future {
      VisirScraper.scrape()
    }

    val future = for {
      mbl <- mblProcess
      visir <- visirProcess
    } yield mbl ++ visir

    val result = Await.result(future, Duration.Inf)

    Ok(Json.obj("Apartments" -> result))
  }
}
