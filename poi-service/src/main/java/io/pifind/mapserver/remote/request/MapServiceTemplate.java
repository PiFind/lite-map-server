package io.pifind.mapserver.remote.request;

import io.pifind.authorization.model.User;
import io.pifind.authorization.utils.JwtUtils;
import io.pifind.common.request.StandardRestTemplate;
import org.springframework.stereotype.Component;

/**
 * 地图服务请求模板
 */
@Component
public class MapServiceTemplate extends StandardRestTemplate {

    public MapServiceTemplate() {
        init();
    }

    /**
     * 初始化
     * 因为 map-service 服务需要鉴权，所以需要在请求头中添加 token，
     * 这里使用游客的 token，所以这里为所有请求添加游客的 token
     */
    private void init() {
        getClientHttpRequestInitializers().add(httpRequest -> {

            // (1) 使用 jwt 生成一个游客的 token
            User loggedInUser = new User();
            loggedInUser.setId(0L);
            loggedInUser.setRole("TOURIST");
            loggedInUser.setUsername("tourist");
            loggedInUser.setPiPlatformToken("");
            String loggedInUserToken = JwtUtils.generateToken(loggedInUser);

            // (2) 添加 token 到请求头
            httpRequest.getHeaders().add(
                    "Authorization",
                    "Bearer " + loggedInUserToken
            );

        });
    }

}
