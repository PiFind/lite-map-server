package io.pifind.mapserver.map;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class MapTemplate extends RestTemplate {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private <T> ParameterizedTypeReference<R<T>> getReference(Class<T> clazz) {
        //objectMapper已经缓存Type，不需要额外缓存
        JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType(
                R.class, R.class, clazz
        );
        return ParameterizedTypeReference.forType(javaType);
    }

    private <T> ParameterizedTypeReference<R<List<T>>> getListReference(Class<T> clazz) {
        //objectMapper已经缓存Type，不需要额外缓存
        JavaType tJavaType = objectMapper.getTypeFactory().constructParametrizedType(
                List.class, List.class, clazz
        );
        JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType(
                R.class, R.class, tJavaType
        );
        return ParameterizedTypeReference.forType(javaType);
    }

    private <T> ParameterizedTypeReference<R<Page<T>>> getPageReference(Class<T> clazz) {
        //objectMapper已经缓存Type，不需要额外缓存
        JavaType tJavaType = objectMapper.getTypeFactory().constructParametrizedType(
                Page.class, Page.class, clazz
        );
        JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType(
                R.class, R.class, tJavaType
        );
        return ParameterizedTypeReference.forType(javaType);
    }

    /**
     * GET 方法请求标准返回
     * @param url 请求URL
     * @param tClass 请求结果类型
     * @return 请求返回的标准结果
     * @param <T> 请求结果类型
     */
    public <T> R<T> getForStandardResponse(String url,Class<T> tClass) {
        ResponseEntity<R<T>> response =
                exchange(url, HttpMethod.GET, null, getReference(tClass));
        return response.getBody();
    }

    /**
     * GET 方法请求标准列表返回
     * @param url 请求URL
     * @param tClass 请求结果类型
     * @return 请求返回的标准结果
     * @param <T> 请求结果类型
     */
    public <T> R<List<T>> getForStandardListResponse(String url, Class<T> tClass) {
        ResponseEntity<R<List<T>>> response =
                exchange(url, HttpMethod.GET, null, getListReference(tClass));
        return response.getBody();
    }

    /**
     * GET 方法请求标准页返回
     * @param url 请求URL
     * @param tClass 请求结果类型
     * @return 请求返回的标准结果
     * @param <T> 请求结果类型
     */
    public <T> R<Page<T>> getForStandardPageResponse(String url, Class<T> tClass) {
        ResponseEntity<R<Page<T>>> response =
                exchange(url, HttpMethod.GET, null, getPageReference(tClass));
        return response.getBody();
    }

    /**
     * POST 方法请求标准返回
     * @param url 请求URL
     * @param requestBody 请求体
     * @param tClass 请求结果类型
     * @return 请求返回的标准结果
     * @param <T> 请求结果类型
     */
    public <T> R<T> postForStandardResponse(String url,Object requestBody,Class<T> tClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);
        //调用RestTemplate的exchange方法，传入请求URL、方法、HttpEntity对象、ParameterizedTypeReference对象，得到一个ResponseEntity对象
        ResponseEntity<R<T>> response =
                exchange(url, HttpMethod.POST, request, getReference(tClass));
        return response.getBody();
    }

    /**
     * POST 方法请求标准返回
     * @param url 请求URL
     * @param requestBody 请求体
     * @param tClass 请求结果类型
     * @return 请求返回的标准结果
     * @param <T> 请求结果类型
     */
    public <T> R<List<T>> postForListStandardResponse(String url,Object requestBody,Class<T> tClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);
        //调用RestTemplate的exchange方法，传入请求URL、方法、HttpEntity对象、ParameterizedTypeReference对象，得到一个ResponseEntity对象
        ResponseEntity<R<List<T>>> response =
                exchange(url, HttpMethod.POST, request, getListReference(tClass));
        return response.getBody();
    }

    /**
     * POST 方法请求标准返回
     * @param url 请求URL
     * @param requestBody 请求体
     * @param tClass 请求结果类型
     * @return 请求返回的标准结果
     * @param <T> 请求结果类型
     */
    public <T> R<Page<T>> postForPageStandardResponse(String url, Object requestBody, Class<T> tClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);
        //调用RestTemplate的exchange方法，传入请求URL、方法、HttpEntity对象、ParameterizedTypeReference对象，得到一个ResponseEntity对象
        ResponseEntity<R<Page<T>>> response =
                exchange(url, HttpMethod.POST, request, getPageReference(tClass));
        return response.getBody();
    }


}
