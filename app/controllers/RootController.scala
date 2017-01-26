package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * Created by sveinbjorn on 11.1.2017.
  */
class RootController extends Controller with Scraper {

  def scrape = Action {
    implicit val baseTime = System.currentTimeMillis

    val mblProcess: Future[List[Apartment]] = Future {
      MblScraper.scrape()
    }

    val visirProcess: Future[List[Apartment]] = Future {
      VisirScraper.scrape()
    }

    val blandProcess: Future[List[Apartment]] = Future {
      BlandScraper.scrape()
    }

    val leigaProcess: Future[List[Apartment]] = Future {
      LeigaIsScraper.scrape()
    }

    val leiguListinnProcess: Future[List[Apartment]] = Future {
      LeigulistinnScraper.scrape()
    }


    val future = for {
      mbl <- mblProcess
      visir <- visirProcess
      bland <- blandProcess
      leiga <- leigaProcess
      leiguListinn <- leiguListinnProcess
    } yield mbl ++ visir ++ bland ++ leiga ++ leiguListinn


    //TODO: blocking code, will fix later
    val result = Await.result(future, Duration.Inf)

    Ok(Json.obj("Apartments" -> result))
  }
}
