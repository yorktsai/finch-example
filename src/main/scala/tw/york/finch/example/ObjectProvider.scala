package tw.york.finch.example

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.twitter.logging.FileHandler
import com.twitter.logging.Formatter
import com.twitter.logging.Level
import com.twitter.logging.Logger
import com.twitter.logging.LoggerFactory
import com.twitter.logging.Policy

object ObjectProvider {
  val objectMapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

  /**
   * twitter.util.logging has some serious performance issue.
   * Consider use twitter.util.logging for developing debugging only.
   */
  def loggerFactory(name: String): Logger = {
    val factory = LoggerFactory(
      node = name,
      level = Some(Level.INFO),
      handlers = List(
        FileHandler(
          filename = "twitter-server-debug.log",
          rollPolicy = Policy.SigHup,
          append = true,
          formatter = new Formatter(
            timezone = Some("UTC"),
            prefix = "%.3s <yyyyMMdd-HH:mm:ss.SSS> "))))

    factory()
  }
}
