package io.pifind.mapserver.converter.po;

import io.pifind.common.converter.AdvancedConverter;
import io.pifind.mapserver.model.po.InterestPointCommentPO;
import io.pifind.poi.model.dto.InterestPointCommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface InterestPointCommentPoConverter extends AdvancedConverter<InterestPointCommentDTO, InterestPointCommentPO> {

    @Override
    @Mappings({
            @Mapping(target = "interestPointId",source = "source.interestPointId"),
            @Mapping(target = "superiorId",source = "source.superiorId"),
            @Mapping(target = "content",source = "source.content"),
    })
    InterestPointCommentPO convert(InterestPointCommentDTO source);

}
