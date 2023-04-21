package io.pifind.mapserver.middleware.redis.model;

import lombok.Data;

import java.util.Map;

/**
 * 用户投票记录
 */
@Data
public class UserVoteRecordDTO {

    /**
     * 用户投票映射
     * username -> 是否同意
     */
    private Map<String,Boolean> userVoteMap;

    /**
     * 总数量
     */
    private Integer total;

    /**
     * 同意数量
     */
    private Integer agrees;

}
