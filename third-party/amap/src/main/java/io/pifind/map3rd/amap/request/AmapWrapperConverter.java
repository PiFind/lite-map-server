package io.pifind.map3rd.amap.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pifind.map3rd.amap.model.wrapper.AmapResponseWrapper;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.lang.reflect.Type;

/**
 * 这是为了处理高德地图的返回值的转换器
 */
public class AmapWrapperConverter extends AbstractJackson2HttpMessageConverter {

    public AmapWrapperConverter() {
        this(Jackson2ObjectMapperBuilder.json().build());
    }

    public AmapWrapperConverter(ObjectMapper objectMapper) {
        super(objectMapper, MediaType.APPLICATION_JSON, new MediaType("application", "*+json"));
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        InputStream inputStream = StreamUtils.nonClosing(inputMessage.getBody());
        MediaType contentType = inputMessage.getHeaders().getContentType();

        // 读取 JSON ，并提取 status 和 info
        String json = IOUtils.toString(inputStream, getCharset(contentType));
        AmapResponseWrapper<?> wrapper =
                defaultObjectMapper.readValue(json, AmapResponseWrapper.class);

        // 将提取出来的 status 和 info 组装成新的 JSON
        String newJson =
                "{\"status\":\""+ wrapper.getStatus() +
                "\",\"info\":\""+ wrapper.getInfo() +
                "\",\"result\":" + json + "}";

        // 重新构建一个 HttpMessage
        MappingJacksonInputMessage httpInputMessage = new MappingJacksonInputMessage(
                new ByteArrayInputStream(newJson.getBytes()),
                inputMessage.getHeaders());

        return super.read(type,contextClass,httpInputMessage);

    }
}
