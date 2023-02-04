package io.pifind.mapserver.middleware.redis.model;

import lombok.Data;

@Data
public class InterestPointSocialDTO {

    /**
     * 兴趣点ID
     */
    private long id;

    /**
     * 浏览量增量
     */
    private int pageviewsIncrement;

    /**
     * 收藏量增量
     */
    private int collectionsIncrement;

    /**
     * 分数增量
     */
    private int scoreIncrement;

    /**
     * 参与评分的人数增量
     */
    private int participantsIncrement;

    /**
     * 创建时间戳
     */
    private Long createTimestamp;

    /**
     * 更新时间戳
     */
    private Long updateTimestamp;

    /**
     * 更新频率
     */
    private int times;

}
