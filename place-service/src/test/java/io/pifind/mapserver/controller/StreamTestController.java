package io.pifind.mapserver.controller;

import io.pifind.common.response.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;

@RestController
@RequestMapping("/v1/test")
public class StreamTestController {

    // @GetMapping(value="/message", produces = "text/event-stream;charset=utf-8")
    @GetMapping("/message")
    public SseEmitter handleSse() {
        SseEmitter emitter = new SseEmitter();

        Flux.interval(Duration.ofSeconds(1))
                .map(i -> "Server-Sent Event #" + i)
                .doOnCancel(() -> emitter.complete())
                .subscribe(
                         data -> {
                            try {
                                emitter.send(data);
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        error -> emitter.completeWithError(error),
                        () -> emitter.complete()
                );

        return emitter;
    }

}
