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

    @Resource(name = "InterestPointSocialRedisTemplate")
    private RedisTemplate<String, InterestPointSocialDTO> redisTemplate;

    @Override
    public boolean containsId(Long id) {
        String key = KEY_GENERATOR.generate(id);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

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

        // ???????????????????????????
        List<InterestPointSocialDTO> res = redisTemplate.execute(new SessionCallback<List<InterestPointSocialDTO>>() {
            @Override
            public <K, V> List<InterestPointSocialDTO> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                List<InterestPointSocialDTO> dtoList = new ArrayList<>(count);
                for (int i = 0; i < count ; i ++) {
                    K key = operations.randomKey();

                    // ????????????????????? null???????????? null????????????Redis????????????????????????
                    if (key == null) {
                        break;
                    }

                    if (key instanceof String) {
                        String keyStr = (String) key;
                        // ?????????????????? key ???????????????
                        if (KEY_GENERATOR.isMatched(keyStr)) {
                            continue;
                        }
                    } else {
                        continue;
                    }

                    // ??????????????????
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

                    // ????????????????????? null???????????? null????????????Redis????????????????????????
                    if (key == null) {
                        break;
                    }

                    if (key instanceof String) {
                        String keyStr = (String) key;
                        // ?????????????????? key ???????????????
                        if (KEY_GENERATOR.isMatched(keyStr)) {
                            continue;
                        }
                    } else {
                        continue;
                    }

                    InterestPointSocialDTO dto = (InterestPointSocialDTO) operations.opsForValue().get(key);

                    // ????????????????????????
                    if (condition.isInvalid(dto)) {
                        if (Boolean.TRUE.equals(operations.hasKey(key))) {

                            // ????????????
                            operations.delete(key);

                            // ?????????????????????????????????
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
         * @param id ?????????ID
         * @param handler ???????????????DTO???????????????
         * @return ????????????Redis????????????
         */
        public static SessionCallback<InterestPointSocialDTO> newCallback(final Long id,final InterestPointSocialDtoHandler handler) {
            SessionCallback<InterestPointSocialDTO> callback = new SessionCallback<InterestPointSocialDTO>() {
                @Override
                public <K, V> InterestPointSocialDTO execute(RedisOperations<K, V> operations) throws DataAccessException {

                    // ????????????
                    operations.multi();

                    // ????????????
                    InterestPointSocialDTO interestPointSocialDTO = getInterestPointSocialById(operations,id);

                    // ????????????
                    if (handler != null) {
                        handler.handle(interestPointSocialDTO);
                    }

                    // ????????????
                    updateInterestPointSocial(operations,interestPointSocialDTO);

                    // ????????????
                    operations.exec();

                    return interestPointSocialDTO;
                }
            };
            return callback;
        }

        /**
         * ?????????????????????
         * @param operations Redis????????????
         * @param id ?????????ID
         * @return ???????????????DTO??????
         * @param <K> ?????????
         * @param <V> ?????????
         */
        private static <K,V> InterestPointSocialDTO getInterestPointSocialById(RedisOperations<K,V> operations,Long id) {
            InterestPointSocialDTO interestPointSocialDTO;
            String key = KEY_GENERATOR.generate(id);

            if(Boolean.TRUE.equals(operations.hasKey((K) key))) {
                interestPointSocialDTO = (InterestPointSocialDTO) operations.opsForValue().get((K) key);
            } else {
                // ???????????????????????????????????????
                interestPointSocialDTO = new InterestPointSocialDTO();
                interestPointSocialDTO.setId(id);
                interestPointSocialDTO.setCreateTimestamp(System.currentTimeMillis());
                interestPointSocialDTO.setUpdateTimestamp(System.currentTimeMillis());
            }

            return interestPointSocialDTO;
        }

        /**
         * ?????????????????????
         * @param operations Redis????????????
         * @param dto ???????????????DTO??????
         * @param <K> ?????????
         * @param <V> ?????????
         */
        public static <K,V> void updateInterestPointSocial(RedisOperations<K,V> operations, InterestPointSocialDTO dto) {
            String key = KEY_GENERATOR.generate(dto.getId());
            operations.opsForValue().set((K) key, (V) updateFrequency(dto));
        }

        /**
         * ??????????????????
         * @param dto ???????????????DTO??????
         */
        private static InterestPointSocialDTO updateFrequency(InterestPointSocialDTO dto) {
            dto.setUpdateTimestamp(System.currentTimeMillis());
            dto.setTimes(dto.getTimes() + 1);
            return dto;
        }
    }

    /**
     * ???????????????DTO???????????????
     */
    @FunctionalInterface
    private static interface InterestPointSocialDtoHandler {

        /**
         * ???????????????DTO??????????????????
         * @param interestPointSocialDTO ???????????????DTO??????
         */
        void handle(InterestPointSocialDTO interestPointSocialDTO);

    }

}
