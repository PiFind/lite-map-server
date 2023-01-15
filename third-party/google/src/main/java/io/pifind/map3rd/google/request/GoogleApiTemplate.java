package io.pifind.map3rd.google.request;

import org.springframework.http.HttpRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

/**
 * Google API 请求模板
 */
public class GoogleApiTemplate extends RestTemplate {

    private final String key;

    public GoogleApiTemplate(String key) {
        this.key = key;
        init();
    }

    private void init() {
        // 添加一个拦截器
        this.getInterceptors().add(0, (request, body, execution) -> {

            // 添加上key
            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(request.getURI()).queryParam("key",key);

            // 创建一个新的请求
            HttpRequest newRequest = getRequestFactory().createRequest(
                    builder.build(true).toUri(),
                    Objects.requireNonNull(request.getMethod())
            );
            newRequest.getHeaders().addAll(request.getHeaders());

            // 继续往下执行
            return execution.execute(newRequest,body);

        });
    }

}
