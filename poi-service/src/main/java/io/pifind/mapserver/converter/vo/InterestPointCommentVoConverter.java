package io.pifind.mapserver.converter.vo;

import io.pifind.common.converter.AdvancedConverter;
import io.pifind.mapserver.model.po.InterestPointCommentPO;
import io.pifind.poi.constant.PoiCommentStatusEnum;
import io.pifind.poi.model.vo.InterestPointCommentVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface InterestPointCommentVoConverter extends AdvancedConverter<InterestPointCommentPO, InterestPointCommentVO> {

    @Override
    default InterestPointCommentVO convert(InterestPointCommentPO source) {
        InterestPointCommentVO interestPointCommentVO = new InterestPointCommentVO();
        interestPointCommentVO.setId(source.getId());
        interestPointCommentVO.setUsername(source.getUsername());
        interestPointCommentVO.setInterestPointId(source.getInterestPointId());
        interestPointCommentVO.setSuperiorId(source.getSuperiorId());
        interestPointCommentVO.setContent(source.getContent());
        interestPointCommentVO.setLikes(source.getLikes());
        switch (source.getStatus()) {
            case PENDING_MACHINE_AUDIT: {
                interestPointCommentVO.setStatus(PoiCommentStatusEnum.UNVERIFIED);
                break;
            }
            case MACHINE_AUDIT_PASS: {
                interestPointCommentVO.setStatus(PoiCommentStatusEnum.VERIFIED);
                break;
            }
            case MACHINE_AUDIT_REFUSE: {
                interestPointCommentVO.setStatus(PoiCommentStatusEnum.INVALID);
                break;
            }
        }
        interestPointCommentVO.setCreateTime(source.getCreateTime());
        interestPointCommentVO.setUpdateTime(source.getUpdateTime());
        return interestPointCommentVO;
    }

}
