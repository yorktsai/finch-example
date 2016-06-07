package tw.york.finch.example.controller

import org.scalatest.fixture.FlatSpec

import io.finch.test.ServiceIntegrationSuite

class HelloControllerIntegrationSpec extends FlatSpec
  with ServiceIntegrationSuite with HelloControllerSuite
