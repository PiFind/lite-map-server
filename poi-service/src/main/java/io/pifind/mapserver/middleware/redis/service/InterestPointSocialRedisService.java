package io.pifind.mapserver.middleware.redis.service;

import io.pifind.mapserver.middleware.redis.model.InterestPointSocialDTO;

import java.util.List;

public interface InterestPointSocialRedisService {

    /**
     * 是否包含兴趣点ID
     * @param id 兴趣点ID
     * @return 返回类型为 boolean
     * <ul>
     *     <li><b>包含兴趣点ID</b> - 返回 {@code true}</li>
     *     <li><b>不包含兴趣点ID</b> - 返回 {@code false}</li>
     * </ul>
     */
    boolean containsId(Long id);

    /**
     * 清零兴趣点社交相关的数据（直接在 Redis 中删除）
     * @param id 兴趣点ID
     * @return 返回类型为 boolean
     * <ul>
     *     <li><b>清理成功</b> - 返回 {@code true}</li>
     *     <li><b>清理失败</b> - 返回 {@code false}</li>
     * </ul>
     */
    boolean deleteById(Long id);

    /**
     * 通过兴趣点ID增加浏览量
     * @param id 兴趣点ID
     * @param n 增加的浏览量
     * @return 当前的浏览量增量
     */
    int increasePageviewsById(Long id,int n);

    /**
     * 通过兴趣点ID获取浏览量增量
     * @param id 兴趣点ID
     * @return 当前的浏览量增量
     */
    int getPageviewsIncrementById(Long id);

    /**
     * 通过兴趣点ID增加收藏量
     * @param id 兴趣点ID
     * @param n 增加的收藏数
     * @return 当前的收藏量增量
     */
    int increaseCollectionsById(Long id,int n);

    /**
     * 通过兴趣点ID获取收藏数增量
     * @param id 兴趣点ID
     * @return 当前的收藏数增量
     */
    int getCollectionsIncrementById(Long id);

    /**
     * 通过兴趣点ID增加积分
     * @param id 兴趣点ID
     * @param score 积分数
     * @param participants 参与评分的人数
     * @return 当前的积分增量
     */
    int increaseScoreById(Long id,int score,int participants);

    /**
     * 通过兴趣点ID获取积分增量
     * @param id 兴趣点ID
     * @return 当前的积分增量
     */
    int getScoreIncrementById(Long id);

    /**
     * 通过兴趣点ID获取投票人数增量
     * @param id 兴趣点ID
     * @return 当前的投票人数增量
     */
    int getParticipantsIncrementById(Long id);

    /**
     * 通过兴趣点ID获取兴趣点社交DTO对象
     * @param id 兴趣点ID
     * @return 兴趣点社交DTO对象
     */
    InterestPointSocialDTO getInterestPointSocialById(Long id);

    /**
     * 通过兴趣点ID回去兴趣点社交DTO对象
     * @param id 兴趣点ID
     */
    void setInterestPointSocial(InterestPointSocialDTO dto);

    /**
     * 获取存储在 Redis 里面的随机的兴趣点社交DTO列表
     * @param count 兴趣点社交DTO个数
     * @return 随机的兴趣点社交DTO列表
     */
    List<InterestPointSocialDTO> randomInterestPointSocialDtoList(int count);

    /**
     * 根据失效条件随机删除一些兴趣点社交DTO
     * @param count 随机数量总数
     * @param condition 无效条件
     * @return 被移除兴趣点社交DTO数据
     */
    List<InterestPointSocialDTO> randomDeleteInterestPointSocialByCondition(int count,InvalidCondition condition);

    /**
     * 无效条件接口
     * <p>
     *     配合 {@link InterestPointSocialRedisService#randomDeleteInterestPointSocialByCondition(int count,InvalidCondition condition)} 使用
     *     用于定义兴趣点社交记录失效的情况
     * </p>
     */
    @FunctionalInterface
    interface InvalidCondition {

        boolean isInvalid(InterestPointSocialDTO dto);

    }

}
