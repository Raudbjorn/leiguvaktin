package services

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document
import org.jsoup.Jsoup

/**
  * Created by sveinbjorn on 10.1.2017.
  */
trait Scraper {
  val browser = JsoupBrowser()

  def retry[T](n: Int)(fn: => T): T = {
    try {
      fn
    } catch {
      case e if n > 1 => {
        Thread.sleep(100)
        retry(n - 1)(fn)
      }
    }
  }

  def getPage(url: String): Document = {
    retry(5) {
    browser.parseString(browser.requestSettings(Jsoup.connect(url)
      .ignoreContentType(true)
      .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
      .referrer("http://www.google.com")
      .timeout(1200000)
      .followRedirects(true)).get().html())
    }
  }
}
