package tw.york.finch.example

import com.fasterxml.jackson.databind.ObjectMapper
import com.twitter.finagle.Http
import com.twitter.finagle.param.Stats
import com.twitter.util.Await
import com.typesafe.config.ConfigFactory

import io.finch.jackson.encodeJackson
import ru.arkoit.finchrich.controller.Controller
import ru.arkoit.finchrich.controller.controllerToEndpoint

/**
 * A simple Finch application evaluating the given Scala expression using
 * [[https://github.com/twitter/util/blob/master/util-eval/src/main/scala/com/twitter/util/Eval.scala util-eval]].
 *
 * Use the following HTTPie commands to test endpoints.
 *
 * {{{
 *   $ http GET :8081/hello
 *   $ http POST :8081/eval expression=10+10
 *   $ http POST :8081/eval expression=\"abc\".toList.headOption
 * }}}
 */
object Main extends APIServer {

  implicit val objectMapper: ObjectMapper = ObjectProvider.objectMapper

  val appConf = ConfigFactory.load()

  object MainController extends Controller {
    val hello = new tw.york.finch.example.controller.HelloController(Main.this)
    val eval = new tw.york.finch.example.controller.EvalController(Main.this)
  }

  def main(): Unit = {
    // configuration
    val port = appConf.getInt("tw.york.server.port")

    // start server
    val ep = controllerToEndpoint(MainController)

    val server = Http.server
      .configured(Stats(statsReceiver))
      .serve(s":${port}", ep.toService)

    onExit { server.close() }

    Await.ready(adminHttpServer)
  }
}
