import scala.util.{Try, Success, Failure}
import scala.concurrent._

object Test {
  def main(args: Array[String]) {
    // import an implicit ExecutionContext, needed to chain operations
    // together with Futures
    import ExecutionContext.Implicits.global

    val ipAddress: Future[String] = for {
      response  <- fetchURL("http://ip.jsontest.com/")
    } yield parseIpAddress(response).get

    // block until the future is complete and print
    ipAddress.onComplete {
      case Success(s) => println(s)
      case Failure(ex) => println(ex.getMessage)
    }
    Await.ready(ipAddress, duration.Duration.Inf)
  }

  /** Parse a JSON string of the form `{ "ipAddress" : "1.2.3.4" }` */
  def parseIpAddress(text: String): Try[String] = {
    import play.api.libs.json.Json // http://www.playframework.com/documentation/2.0/ScalaJson

    val json = Json.parse(text)
    val ipAddress = (json \ "ip")
    Try(ipAddress.as[String])
  }

  /** Fetch the body of the given URL via HTTP. */
  def fetchURL(urlString: String)
              (implicit execContext: ExecutionContext): Future[String] = {
    import dispatch._ // http://dispatch.databinder.net/Dispatch.html

    Http(url(urlString).OK(as.String))
  }
}
