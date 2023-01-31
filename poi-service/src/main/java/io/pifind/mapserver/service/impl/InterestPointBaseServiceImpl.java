package io.pifind.mapserver.service.impl;

import io.pifind.common.response.R;
import io.pifind.mapserver.converter.dto.InterestPointDtoConverter;
import io.pifind.mapserver.converter.po.InterestPointPoConverter;
import io.pifind.mapserver.mapper.InterestPointMapper;
import io.pifind.mapserver.model.po.InterestPointPO;
import io.pifind.poi.api.InterestPointBaseService;
import io.pifind.poi.model.InterestPointDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class InterestPointBaseServiceImpl implements InterestPointBaseService {

    @Autowired
    private InterestPointDtoConverter interestPointDtoConverter;

    @Autowired
    private InterestPointPoConverter interestPointPoConverter;

    @Autowired
    private InterestPointMapper interestPointMapper;

    @Override
    public R<Boolean> addInterestPoint(@NotNull InterestPointDTO interestPoint) {

        // 将 DTO 对象转换成 PO 对象
        InterestPointPO po = interestPointPoConverter.convert(interestPoint);

        // 加入到数据库中
        interestPointMapper.insert(po);

        return null;
    }

    @Override
    public R<InterestPointDTO> getInterestPointById(@NotEmpty String id) {
        return null;
    }

    @Override
    public R<Boolean> modifyInterestPoint(@NotNull InterestPointDTO modifiedInterestPoint) {
        return null;
    }

    @Override
    public R<Boolean> removeInterestPointById(@NotEmpty String id) {
        return null;
    }
}
