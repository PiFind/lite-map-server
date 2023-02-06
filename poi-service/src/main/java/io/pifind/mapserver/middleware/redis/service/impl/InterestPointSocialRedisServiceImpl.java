package io.pifind.mapserver.middleware.redis.service.impl;

import io.pifind.mapserver.middleware.redis.model.InterestPointSocialDTO;
import io.pifind.mapserver.middleware.redis.service.InterestPointSocialRedisService;
import io.pifind.mapserver.redis.FormattedStringKeyGenerator;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class InterestPointSocialRedisServiceImpl implements InterestPointSocialRedisService {

    private static final FormattedStringKeyGenerator KEY_GENERATOR = FormattedStringKeyGenerator.create("PoiSocial@%d");

    // TODO
    @Resource(name = "")
    private RedisTemplate<String, InterestPointSocialDTO> redisTemplate;

    @Override
    public boolean deleteById(Long id) {
        String key = KEY_GENERATOR.generate(id);
        if(Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.delete(key);
        }
        return true;
    }

    @Override
    public int increasePageviewsById(Long id, int n) {
        SessionCallback<InterestPointSocialDTO> callback = RedisCallbackFactory.newCallback(
                id,
                interestPointSocialDTO -> interestPointSocialDTO.setPageviewsIncrement(
                        interestPointSocialDTO.getPageviewsIncrement() + n
                )
        );
        return redisTemplate.execute(callback).getPageviewsIncrement();
    }

    @Override
    public int getPageviewsIncrementById(Long id) {
        return getInterestPointSocialById(id).getPageviewsIncrement();
    }

    @Override
    public int increaseCollectionsById(Long id, int n) {
        SessionCallback<InterestPointSocialDTO> callback = RedisCallbackFactory.newCallback(
                id,
                interestPointSocialDTO -> interestPointSocialDTO.setCollectionsIncrement(
                        interestPointSocialDTO.getCollectionsIncrement() + n
                )
        );
        return redisTemplate.execute(callback).getCollectionsIncrement();
    }

    @Override
    public int getCollectionsIncrementById(Long id) {
        return getInterestPointSocialById(id).getCollectionsIncrement();
    }

    @Override
    public int increaseScoreById(Long id, int score, int participants) {
        SessionCallback<InterestPointSocialDTO> callback = RedisCallbackFactory.newCallback(
                id,
                interestPointSocialDTO -> {
                    interestPointSocialDTO.setScoreIncrement(
                            interestPointSocialDTO.getScoreIncrement() + score
                    );
                    interestPointSocialDTO.setParticipantsIncrement(
                            interestPointSocialDTO.getParticipantsIncrement() + participants
                    );
                }
        );
        return redisTemplate.execute(callback).getScoreIncrement();
    }

    @Override
    public int getScoreIncrementById(Long id) {
        return getInterestPointSocialById(id).getScoreIncrement();
    }

    @Override
    public int getParticipantsIncrementById(Long id) {
        return getInterestPointSocialById(id).getParticipantsIncrement();
    }

    @Override
    public InterestPointSocialDTO getInterestPointSocialById(Long id) {
        String key = KEY_GENERATOR.generate(id);
        if(Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return redisTemplate.boundValueOps(key).get();
        }
        return null;
    }

    @Override
    public void setInterestPointSocial(InterestPointSocialDTO dto) {
        String key = KEY_GENERATOR.generate(dto.getId());
        redisTemplate.opsForValue().set(key,dto);
    }

    @Override
    public List<InterestPointSocialDTO> randomInterestPointSocialDtoList(final int count) {

        // 作为一个事务来执行
        List<InterestPointSocialDTO> res = redisTemplate.execute(new SessionCallback<List<InterestPointSocialDTO>>() {
            @Override
            public <K, V> List<InterestPointSocialDTO> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                List<InterestPointSocialDTO> dtoList = new ArrayList<>(count);
                for (int i = 0; i < count ; i ++) {
                    K key = operations.randomKey();

                    // 检查数据是否为 null，如果为 null，则认为Redis中已经没有数据了
                    if (key == null) {
                        break;
                    }

                    if (key instanceof String) {
                        String keyStr = (String) key;
                        // 检查是否匹配 key 的构造方法
                        if (KEY_GENERATOR.isMatched(keyStr)) {
                            continue;
                        }
                    } else {
                        continue;
                    }

                    // 添加到列表里
                    dtoList.add((InterestPointSocialDTO) operations.opsForValue().get(key));
                }
                operations.exec();
                return dtoList;
            }
        });

        return res;
    }

    @Override
    public List<InterestPointSocialDTO> randomDeleteInterestPointSocialByCondition(int count, InvalidCondition condition) {

        List<InterestPointSocialDTO> res = redisTemplate.execute(new SessionCallback<List<InterestPointSocialDTO>>() {
            @Override
            public <K, V> List<InterestPointSocialDTO> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                List<InterestPointSocialDTO> dtoList = new ArrayList<>(count);
                for (int i = 0; i < count ; i ++) {
                    K key = operations.randomKey();

                    // 检查数据是否为 null，如果为 null，则认为Redis中已经没有数据了
                    if (key == null) {
                        break;
                    }

                    if (key instanceof String) {
                        String keyStr = (String) key;
                        // 检查是否匹配 key 的构造方法
                        if (KEY_GENERATOR.isMatched(keyStr)) {
                            continue;
                        }
                    } else {
                        continue;
                    }

                    InterestPointSocialDTO dto = (InterestPointSocialDTO) operations.opsForValue().get(key);

                    // 如果满足失效条件
                    if (condition.isInvalid(dto)) {
                        if (Boolean.TRUE.equals(operations.hasKey(key))) {

                            // 移除数据
                            operations.delete(key);

                            // 将删除的数据加入列表中
                            dtoList.add((InterestPointSocialDTO) operations.opsForValue().get(key));

                        }
                    }

                }
                operations.exec();
                return dtoList;
            }
        });

        return res;
    }

    private static class RedisCallbackFactory {

        /**
         *
         * @param id 兴趣点ID
         * @param handler 兴趣点社交DTO对象处理器
         * @return 带事务的Redis反馈对象
         */
        public static SessionCallback<InterestPointSocialDTO> newCallback(final Long id,final InterestPointSocialDtoHandler handler) {
            SessionCallback<InterestPointSocialDTO> callback = new SessionCallback<InterestPointSocialDTO>() {
                @Override
                public <K, V> InterestPointSocialDTO execute(RedisOperations<K, V> operations) throws DataAccessException {

                    // 开启事务
                    operations.multi();

                    // 获取数据
                    InterestPointSocialDTO interestPointSocialDTO = getInterestPointSocialById(operations,id);

                    // 处理数据
                    if (handler != null) {
                        handler.handle(interestPointSocialDTO);
                    }

                    // 保存数据
                    updateInterestPointSocial(operations,interestPointSocialDTO);

                    // 执行事务
                    operations.exec();

                    return interestPointSocialDTO;
                }
            };
            return callback;
        }

        /**
         * 获取兴趣点对象
         * @param operations Redis操作对象
         * @param id 兴趣点ID
         * @return 兴趣点社交DTO对象
         * @param <K> 键类型
         * @param <V> 值类型
         */
        private static <K,V> InterestPointSocialDTO getInterestPointSocialById(RedisOperations<K,V> operations,Long id) {
            InterestPointSocialDTO interestPointSocialDTO;
            String key = KEY_GENERATOR.generate(id);

            if(Boolean.TRUE.equals(operations.hasKey((K) key))) {
                interestPointSocialDTO = (InterestPointSocialDTO) operations.opsForValue().get((K) key);
            } else {
                // 如果没有就创建一个新的数据
                interestPointSocialDTO = new InterestPointSocialDTO();
                interestPointSocialDTO.setId(id);
                interestPointSocialDTO.setCreateTimestamp(System.currentTimeMillis());
                interestPointSocialDTO.setUpdateTimestamp(System.currentTimeMillis());
            }

            return interestPointSocialDTO;
        }

        /**
         * 设置兴趣点对象
         * @param operations Redis操作对象
         * @param dto 兴趣点社交DTO对象
         * @param <K> 键类型
         * @param <V> 值类型
         */
        public static <K,V> void updateInterestPointSocial(RedisOperations<K,V> operations, InterestPointSocialDTO dto) {
            String key = KEY_GENERATOR.generate(dto.getId());
            operations.opsForValue().set((K) key, (V) updateFrequency(dto));
        }

        /**
         * 频率状态更新
         * @param dto 兴趣点社交DTO对象
         */
        private static InterestPointSocialDTO updateFrequency(InterestPointSocialDTO dto) {
            dto.setUpdateTimestamp(System.currentTimeMillis());
            dto.setTimes(dto.getTimes() + 1);
            return dto;
        }
    }

    /**
     * 兴趣点社交DTO对象处理器
     */
    @FunctionalInterface
    private static interface InterestPointSocialDtoHandler {

        /**
         * 兴趣点社交DTO对象处理方法
         * @param interestPointSocialDTO 兴趣点社交DTO对象
         */
        void handle(InterestPointSocialDTO interestPointSocialDTO);

    }

}
