package services

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import play.api.libs.json.Json

/**
  * Created by sveinbjorn on 10.1.2017.
  */
trait Scraper {
  val browser = JsoupBrowser()
}
