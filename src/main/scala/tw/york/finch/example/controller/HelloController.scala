package tw.york.finch.example.controller

import com.fasterxml.jackson.databind.ObjectMapper

import io.finch.BadRequest
import io.finch.Endpoint
import io.finch.Ok
import io.finch.get
import io.finch.internal.Mapper.mapperFromOutputValue
import io.finch.stringToMatcher
import ru.arkoit.finchrich.controller.Controller
import tw.york.finch.example.APIServer
import tw.york.finch.example.ObjectProvider

object HelloController {
  case class Input(expression: String)
  case class Output(
    result: String,
    resultArray: Seq[String],
    resultMap: Map[String, String],
    resultNest: Map[String, Map[Long, Long]])

  case class AccessLog(
      input: Input,
      output: Output) {
    val clazz = getClass.getName
  }
}

class HelloController(server: APIServer) extends Controller {

  implicit val objectMapper: ObjectMapper = ObjectProvider.objectMapper

  lazy val logger = org.slf4j.LoggerFactory.getLogger(getClass.getName)
  //val debugLogger = ObjectProvider.loggerFactory(getClass.getName)

  val helloCounter = server.statsReceiver.counter("rdc/api/hello/counter")

  val endPoint: Endpoint[HelloController.Output] = get("hello") {
    // metrics
    helloCounter.incr()

    // handle request
    val input = HelloController.Input("input")
    val output = HelloController.Output(
      result = "Hello World!",
      resultArray = Seq("Hello", "World", "!"),
      resultMap = Map("Hello" -> "World"),
      resultNest = Map("Hello" -> Map(0L -> 1L), "World" -> Map(2L -> 3L)))

    // log
    val logJson = objectMapper.writeValueAsString(HelloController.AccessLog(input, output))
    logger.info(s"${logJson}")

    // response
    Ok(output)
  } handle {
    case e: Exception => BadRequest(e)
  }
}
