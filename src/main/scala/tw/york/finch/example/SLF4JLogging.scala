package tw.york.finch.example

import java.util.logging.Level
import java.util.logging.LogManager

import org.slf4j.bridge.SLF4JBridgeHandler

import com.twitter.app.App

trait SLF4JLogging { self: App =>
  init {
    // Turn off Java util logging so that slf4j can configure it
    LogManager.getLogManager.getLogger("").getHandlers.toList.foreach { l =>
      l.setLevel(Level.OFF)
    }
    SLF4JBridgeHandler.install()
  }

  onExit {
    SLF4JBridgeHandler.uninstall()
  }
}
