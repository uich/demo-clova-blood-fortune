package com.example.demodevday;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.linecorp.clova.extension.boot.handler.annnotation.CEKRequestHandler;
import com.linecorp.clova.extension.boot.handler.annnotation.IntentMapping;
import com.linecorp.clova.extension.boot.handler.annnotation.LaunchMapping;
import com.linecorp.clova.extension.boot.handler.annnotation.SlotValue;
import com.linecorp.clova.extension.boot.message.response.CEKResponse;
import com.linecorp.clova.extension.boot.message.speech.OutputSpeech;

import java.util.Optional;

@CEKRequestHandler
public class DemoHandler {

  @LaunchMapping
  CEKResponse handleLaunch() {
    return CEKResponse.builder()
        .outputSpeech(OutputSpeech.text("あなたは何型ですか？"))
        .shouldEndSession(false)
        .build();
  }

  @IntentMapping("Fortune")
  CEKResponse handleIntent(@SlotValue Optional<BloodType> bloodType) {
    if (!bloodType.isPresent()) {
      return CEKResponse.builder()
          .outputSpeech(OutputSpeech.text("すみません。聞き取れませんでした。もう一度どうぞ。"))
          .shouldEndSession(false)
          .build();
    }
    return CEKResponse.builder()
        .outputSpeech(OutputSpeech.text(getMessage(bloodType.get())))
        .shouldEndSession(true)
        .build();
  }

  enum BloodType {
    A,
    B,
    O,
    AB;

    @JsonCreator
    static BloodType identifiedByText(String text) {
      try {
        return valueOf(text);
      } catch (IllegalArgumentException e) {
        return null;
      }
    }
  }

  String getMessage(BloodType bloodType) {
    switch (bloodType) {
      case A:
        return "A型のあなたの今日はとても良い日でしょう。";
      case B:
        return "B型のあなたの今日はうきうきする日でしょう。";
      case O:
        return "O型のあなたの今日は気分がとても上がる日でしょう。";
      case AB:
        return "AB型のあなたの今日は仕事が捗る日でしょう。";
    }
    throw new IllegalArgumentException();
  }
}
