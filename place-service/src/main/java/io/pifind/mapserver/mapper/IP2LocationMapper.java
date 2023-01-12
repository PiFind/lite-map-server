package io.pifind.mapserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.pifind.mapserver.model.po.IP2LocationPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IP2LocationMapper extends BaseMapper<IP2LocationPO> {

}
