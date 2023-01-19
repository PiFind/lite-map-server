package io.pifind.map3rd.amap.request;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class AmapApiTemplate extends RestTemplate {

    private final String key;
    private final String signKey;
    private final boolean needSign;

    public AmapApiTemplate(String key) {
        this(key,null,false);
    }

    public AmapApiTemplate(String key,String signKey,boolean sign) {
        this.key = key;
        this.signKey = signKey;
        this.needSign = sign;
        init();
    }

    private void init() {
        // 添加一个拦截器
        this.getInterceptors().add(0, (request, body, execution) -> {

            // 添加上key
            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(request.getURI()).queryParam("key",key);

            // 如果需要签名
            if (needSign) {

                // 签名参数
                MultiValueMap<String, String> queryParams = builder.build().getQueryParams();
                StringBuilder queryParamsTextBuilder = new StringBuilder();

                // 按顺序进行排序参数并进行连接
                try(Stream<String> sortedParams =  queryParams.keySet().stream().sorted()){
                    sortedParams.forEachOrdered(new Consumer<String>() {
                        @Override
                        public void accept(String paramName) {
                            List<String> values= queryParams.get(paramName);
                            for (String value : values) {
                                String decodedValue = null;
                                try {
                                    decodedValue = URLDecoder.decode(value, StandardCharsets.UTF_8.name());
                                } catch (UnsupportedEncodingException e) {
                                    throw new RuntimeException(e);
                                }
                                queryParamsTextBuilder
                                        .append(paramName)
                                        .append("=")
                                        .append(decodedValue);
                                queryParamsTextBuilder.append("&");
                            }
                        }
                    });
                    // 删除最后一个"&"符号
                    queryParamsTextBuilder.deleteCharAt(queryParamsTextBuilder.length() - 1);
                }

                // 生成签名文本
                String signText = DigestUtils.md5Hex(queryParamsTextBuilder.append(signKey).toString());
                builder.queryParam("sig",signText);
            }

            // 创建一个新的请求
            HttpRequest newRequest = getRequestFactory().createRequest(
                    builder.build(true).toUri(),
                    Objects.requireNonNull(request.getMethod()));
            newRequest.getHeaders().addAll(request.getHeaders());

            // 继续往下执行
            return execution.execute(newRequest,body);
        });
    }

}
