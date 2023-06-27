package io.pifind.poi.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * UserDTO
 *
 * @author chenxiaoli
 * @date 2023-06-27
 * @description
 */
@Data
public class UserDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户 HASH 值
     */
    private String hash;

    /**
     * 用户角色
     */
    private Long role;

    /**
     * 用户拥有的 pcm 积分
     */
    private Integer pcm;

    /**
     * 邀请者
     */
    private String inviter;

    /**
     * 邀请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date invitedTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
