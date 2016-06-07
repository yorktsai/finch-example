package tw.york.finch.example.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.twitter.util.Eval

import io.finch.BadRequest
import io.finch.Endpoint
import io.finch.Endpoint.StringEndpointOps
import io.finch.Ok
import io.finch.body
import io.finch.internal.Mapper.mapperFromOutputFunction
import io.finch.jackson.decodeJackson
import io.finch.post
import io.finch.stringToMatcher
import ru.arkoit.finchrich.controller.Controller
import tw.york.finch.example.APIServer
import tw.york.finch.example.ObjectProvider

object EvalController {
  case class Input(expression: String)
  case class Output(result: String)
}

class EvalController(server: APIServer) extends Controller {

  implicit val objectMapper: ObjectMapper = ObjectProvider.objectMapper

  val evalCounter = server.statsReceiver.counter("rdc/api/eval/counter")

  val execute: Eval = new Eval()

  val evalEndPoint: Endpoint[EvalController.Output] =
    post("eval" :: body.as[EvalController.Input]) { i: EvalController.Input =>
      // metrics
      evalCounter.incr()

      // handle request
      val output = EvalController.Output(execute[Any](i.expression).toString)

      // response
      Ok(output)
    } handle {
      case e: Exception => BadRequest(e)
    }
}
