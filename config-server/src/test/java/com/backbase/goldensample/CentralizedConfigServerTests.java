package com.siriusxi.cloud.infra.cs;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = {"spring.profiles.active: native"})
class CentralizedConfigServerTests {

  @Test
  void contextLoads() {
    Assertions.assertTrue(true);
  }
}
