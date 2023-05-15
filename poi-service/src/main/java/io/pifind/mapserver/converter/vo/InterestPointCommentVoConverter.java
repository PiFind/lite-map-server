package io.pifind.mapserver.converter.vo;

import io.pifind.common.converter.AdvancedConverter;
import io.pifind.mapserver.model.po.InterestPointCommentPO;
import io.pifind.poi.model.vo.InterestPointCommentVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface InterestPointCommentVoConverter extends AdvancedConverter<InterestPointCommentPO, InterestPointCommentVO> {

    @Override
    @Mappings({
            @Mapping(target = "id",source = "source.id"),
            @Mapping(target = "username",source = "source.username"),
            @Mapping(target = "interestPointId",source = "source.interestPointId"),
            @Mapping(target = "superiorId",source = "source.superiorId"),
            @Mapping(target = "content",source = "source.content"),
            @Mapping(target = "likes",source = "source.likes"),
            @Mapping(target = "status",source = "source.status"),
            @Mapping(target = "createTime",source = "source.createTime"),
            @Mapping(target = "updateTime",source = "source.updateTime"),
    })
    InterestPointCommentVO convert(InterestPointCommentPO source);

}
