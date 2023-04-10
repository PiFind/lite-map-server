package io.pifind.mapserver.controller;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;

public class StreamRestTestClient {

    @Test
    public void test() {
        // 创建一个 RestTemplate 对象，用来发送 HTTP 请求
        RestTemplate restTemplate = new RestTemplate();
        // 设置请求头，指定接受 text/event-stream 类型的数据
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_EVENT_STREAM));
        HttpEntity<String> request = new HttpEntity<>(headers);
        // 发送 GET 请求，获取响应体
        ResponseEntity<String> response = restTemplate.exchange(
                "http://127.0.0.1:9003/v1/test/message",
                HttpMethod.GET,
                request,
                String.class
        );
        // 打印响应体内容
        System.out.println(response.getBody());
    }

    @Test
    public void test2() throws InterruptedException {
        WebClient client = WebClient.create("http://127.0.0.1:9003");
        ParameterizedTypeReference<String> type = new ParameterizedTypeReference<String>() {};
        Flux<String> eventStream = client.get()
                .uri("/v1/test/message")
                .retrieve()
                .bodyToFlux(type);
        eventStream.subscribe(System.out::println);
        eventStream.doOnComplete(()->{});
        Thread.sleep(6000);
    }

}
