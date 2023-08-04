package io.pifind.mapserver.service.impl;

import io.pifind.common.response.R;
import io.pifind.mapserver.model.dto.PcmIntegralDTO;
import io.pifind.mapserver.util.OkHttpUtil;
import io.pifind.mapserver.util.Result;
import io.pifind.mapserver.util.SignatureGenerator;
import io.pifind.poi.api.IInterestPointIntegralService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * InterestPointIntegralServiceImpl
 *
 * @author chenxiaoli
 * @date 2023-07-26
 * @description 积分服务实现类
 */
@Service
@Slf4j
public class InterestPointIntegralServiceImpl implements IInterestPointIntegralService {

    @Value("${pcm.wallet.url}")
    private String url;

    @Value("${pcm.wallet.appId}")
    private Integer appId;

    @Override
    public R integral(String username, String type) {
        PcmIntegralDTO pcmIntegral = new PcmIntegralDTO();
        pcmIntegral.setUsername(username);
        pcmIntegral.setType(type);
        pcmIntegral.setAppId(appId);
        long timestamp = Instant.now().getEpochSecond();
        pcmIntegral.setTimestamp(timestamp);
        try {
            pcmIntegral.setXToken(SignatureGenerator.generateSignature(timestamp));
            Result result = OkHttpUtil.doPostJson(url + "/api/adv/pcm/points/contribute", pcmIntegral, Result.class);
            if (Result.isOk(result)) {
                return R.success();
            }
            return R.failure(result.getMsg());
        }  catch (Exception e) {
            log.error("username:{} trans error:", username, e);
            return R.failure("integral error");
        }
    }
}
