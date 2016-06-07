package tw.york.finch.example.controller

import org.scalatest.fixture.FlatSpec

import com.fasterxml.jackson.databind.ObjectMapper
import com.twitter.finagle.Service
import com.twitter.finagle.http.Request
import com.twitter.finagle.http.Response

import io.finch.jackson.encodeJackson
import io.finch.test.ServiceSuite
import ru.arkoit.finchrich.controller.Controller
import ru.arkoit.finchrich.controller.controllerToEndpoint
import tw.york.finch.example.Main
import tw.york.finch.example.ObjectProvider
import tw.york.finch.example.controller.HelloController.Output

trait HelloControllerSuite { this: FlatSpec with ServiceSuite =>

  implicit val objectMapper: ObjectMapper = ObjectProvider.objectMapper

  object TestController extends Controller {
    val helloworld = new tw.york.finch.example.controller.HelloController(Main)
  }

  def createService(): Service[Request, Response] = {
    val ep = controllerToEndpoint(TestController)
    ep.toService
  }

  "The HelloController" should "return some predefined results" in { f =>
    val request = Request("/hello")
    val result: Response = f(request)

    assert(result.statusCode == 200)

    val expectJson = HelloController.Output(
      result = "Hello World!",
      resultArray = Seq("Hello", "World", "!"),
      resultMap = Map("Hello" -> "World"),
      resultNest = Map("Hello" -> Map(0L -> 1L), "World" -> Map(2L -> 3L)))

    val resultJson = objectMapper.readValue(
      result.contentString,
      classOf[HelloController.Output])

    assert(resultJson.toString() == expectJson.toString())
  }

  it should "return 404 when url not defined" in { f =>
    val request = Request("/hello_not_existed")
    val result: Response = f(request)

    assert(result.statusCode == 404)
  }
}

class HelloControllerSpec extends FlatSpec with ServiceSuite with HelloControllerSuite
