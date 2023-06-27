package io.pifind.mapserver.service.impl;

import io.pifind.common.request.StandardRestTemplate;
import io.pifind.common.response.R;
import io.pifind.poi.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl
 *
 * @author chenxiaoli
 * @date 2023-06-25
 * @description
 */
@Service
public class UserService {

    @Value("${pifind.user-service.url}")
    private String userServiceUrl;

    @Autowired
    private StandardRestTemplate standardRestTemplate;

    public UserDTO getUserInfo(String username) {
        try {
            R<UserDTO> result = standardRestTemplate.getForStandardResponse(userServiceUrl + "user/information/getUserInfo?username=" + username, UserDTO.class);
            return result.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UserDTO();
    }
}
