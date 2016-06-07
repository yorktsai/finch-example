# Finch API Server Example

POC for REST API using Finch and friends.

- [API Framework](#api-framework)
- [Monitoring](#monitoring)
- [Configuration](#configuration)
- [Logging](#logging)
- [Profiling](#profiling)
- [Process Management](#process-management)
- [Testing](#testing)
- [API Documentation](#api-documentation)

## API Framework

[finagle/finch](https://github.com/finagle/finch) is a pure functional REST API framework based on Twitter's [Finagle](https://twitter.github.io/finagle/).

> Finagle is an extensible RPC system for the JVM, used to construct high-concurrency servers. Finagle implements uniform client and server APIs for several protocols, and is designed for high performance and concurrency. Most of Finagle’s code is protocol agnostic, simplifying the implementation of new protocols.

Twitter use Finagle in their production and is actively developed.

## Monitoring

Finagle comes with [twitter/twitter-server](https://github.com/twitter/twitter-server).

> TwitterServer defines a template from which servers at Twitter are built. It provides common application components such as an administrative HTTP server, tracing, stats, etc. These features are wired in correctly for use in production at Twitter.

Also check the [finagle-stats](https://twitter.github.io/finagle/guide/Metrics.html) subproject.

Need to implement our own tool to check logs and generate alerts.

## Configuration

[typesafehub/config](https://github.com/typesafehub/config)

## Logging

Finagle use [twitter/util](https://github.com/twitter/util) for logging, however it has some serious performance problem (Or we just dont understand it enough to tune it correctly). Since [log4j](http://logging.apache.org/log4j/) with [logback](http://logback.qos.ch/) is industry default so we use log4j instead of twitter.util.logging.

Might want to use [Scribe](https://github.com/facebookarchive/scribe) or [Apache Flume](https://flume.apache.org/).

## Profiling

### Server Process

[kubernetes/heapster](https://github.com/kubernetes/heapster) works great with [twitter-server](https://twitter.github.io/twitter-server/Admin.html).

The profile can be analysised using [gperftools](https://github.com/gperftools/gperftools). Just ```brew install gperftools``` if using osx.

### Distributed Tracing

[Zipkin](http://zipkin.io/)

> Zipkin is a distributed tracing system. It helps gather timing data needed to troubleshoot latency problems in microservice architectures. It manages both the collection and lookup of this data. Zipkin’s design is based on the Google Dapper paper.

Check the following [gist](https://gist.github.com/padurean/deb324ecbb3fec9bc342) for finch integration.

Currently finagle-zipkin requires Scribe and zipkin itself requires Cassandra, ElasticSearch. or at least MySQL as backend.

## Process Management

* [Apache Commons Daemon](http://commons.apache.org/proper/commons-daemon/)
* [sbt-native-packager](http://www.scala-sbt.org/sbt-native-packager/index.html)

Twitter server can integrate nicely with [Apache Mesos](http://mesos.apache.org/).

```/admin/health``` is still useful. By default, respond with content-body “OK”. This endpoint can be managed manually by mixing in the Lifecycle.Warmup trait with your server.

## Testing

### Unit Test / Integration Test

Finch/Fiangle come with a simple test framework works great with [ScalaTest](http://www.scalatest.org/). However, we might need to setup DI to mock external services for unit test.

For DI solution, [adamw/macwire](https://github.com/adamw/macwire) is probably a better light-weight solution than [google/guice](https://github.com/google/guice) or Spring.

### Stress Test / Regression Test / Acceptance Test

* [wg/wrk](https://github.com/wg/wrk) is a great stress test tool, [Gatling](http://gatling.io/) works also fine.
* [scalacheck](https://www.scalacheck.org/) can be used as correctness regression test.
* [ScalaMeter](https://scalameter.github.io/) can be used for performance regression test.
* [Robot Framework](http://robotframework.org/) is a great acceptance test tool.
* [twitter/diffy](https://github.com/twitter/diffy) is a bug detection tool. Consider using it for staging.

## API Documentation

[Swagger.io](http://swagger.io/) would be great. However, there is currently no way to generate swagger.io documentation from Finagle. Probably need to implement our own or hand-write YAML.
