package io.pifind.mapserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.pifind.mapserver.model.po.CountryPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CountryMapper extends BaseMapper<CountryPO> {

}
