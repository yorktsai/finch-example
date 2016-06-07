package tw.york.finch.example

import com.twitter.app.App
import com.twitter.server.Admin
import com.twitter.server.AdminHttpServer
import com.twitter.server.Hooks
import com.twitter.server.Lifecycle
import com.twitter.server.Linters
import com.twitter.server.Stats

trait APIServer extends App
    with SLF4JLogging
    with Linters
    with Hooks
    with AdminHttpServer
    with Admin
    with Lifecycle
    with Stats {

}
