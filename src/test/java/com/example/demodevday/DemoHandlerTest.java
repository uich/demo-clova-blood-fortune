package com.example.demodevday;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.linecorp.clova.extension.test.CEKRequestGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class DemoHandlerTest {

  @Autowired
  MockMvc mvc;

  @SpyBean
  DemoHandler handler;

  @Autowired
  ObjectMapper objectMapper;
  Configuration configuration;

  @PostConstruct
  void afterPropertiesSet() {
    this.configuration = Configuration.builder()
        .mappingProvider(new JacksonMappingProvider(objectMapper))
        .jsonProvider(new JacksonJsonProvider(objectMapper))
        .build();
  }

  @Test
  public void handleLaunch() throws Exception {
    String body = CEKRequestGenerator.requestBodyBuilder("data/request.json", configuration)
        .launch()
        .build();

    mvc.perform(post("/")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(body))
        .andDo(print())
        .andExpect(status().is2xxSuccessful());

    verify(handler).handleLaunch();
  }

  @Test
  public void handleIntent() throws Exception {
    String body = CEKRequestGenerator.requestBodyBuilder("data/request.json", configuration)
        .intent("Fortune")
        .slot("blood_type", "C")
        .build();

    mvc.perform(post("/")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(body))
        .andDo(print())
        .andExpect(status().is2xxSuccessful());

    verify(handler).handleIntent(eq(Optional.of(DemoHandler.BloodType.A)));
  }

}