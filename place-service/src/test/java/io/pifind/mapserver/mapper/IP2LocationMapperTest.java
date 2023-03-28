package io.pifind.mapserver.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.pifind.mapserver.PlaceApplicationTest;
import io.pifind.mapserver.model.AdministrativeAreaPO;
import io.pifind.mapserver.model.IP2LocationPO;
import io.pifind.mapserver.util.IPv4Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
// @Rollback(true)
@SpringBootTest(classes = PlaceApplicationTest.class)
public class IP2LocationMapperTest {

    @Autowired
    private IP2LocationMapper ip2LocationMapper;

    @Autowired
    private AdministrativeAreaMapper administrativeAreaMapper;

    /**
     * 测试以下从IP获取到具体数据方法
     */
    //@Test
    public void testGetLocationByIP () {

        // 随便找一个正常的广域IP
        String ip = "120.79.38.99";

        // 将IP转换为地址
        int ipAddress = IPv4Utils.stringToInt(ip);

        // 查询数据库
        IP2LocationPO ip2LocationPO = ip2LocationMapper.selectOne(
                new LambdaQueryWrapper<IP2LocationPO>()
                        .le(IP2LocationPO::getIpFrom,ipAddress)
                        .ge(IP2LocationPO::getIpTo,ipAddress)
        );

        if (ip2LocationPO != null) {
            log.info("{}",ip2LocationPO);
        } else {
            throw new RuntimeException("没有找到数据");
        }

    }

    // @Test
    public void testConvertArea() {

        int batches = 10000;
        int startBatch = 5;

        BigDecimal min = BigDecimal.valueOf(16777216);
        BigDecimal max = new BigDecimal("3758097216");

        BigDecimal unit = max.subtract(min).divide(BigDecimal.valueOf(batches));

        BigDecimal start = min.add(unit.multiply(BigDecimal.valueOf(startBatch)));
        BigDecimal end = start.add(unit);

        int ok = 0;
        int total = 0;

        for (int i = startBatch ; i < 10000 ; i ++) {

            long startTime = System.currentTimeMillis();

            log.info("第{}次执行开始,ip={}",i,start);

            // 逻辑进行
            int[] ss = setAreas(start,end);
            ok += ss[0];
            total += ss[1];

            // 增加
            start = end;
            end = start.add(unit);

            long endTime = System.currentTimeMillis();
            log.info("第{}次执行完毕，花费了{}秒",i,(endTime - startTime)/1000L);
            log.info("命中：{}/{}  总：{}/{}",ss[0],ss[1],ok,total);

        }
        log.info("{}/{}/{}",ok,total,total - ok);
    }

    private int[] setAreas(BigDecimal start,BigDecimal end) {

        List<IP2LocationPO> locationPOS = ip2LocationMapper.selectList(
                new LambdaQueryWrapper<IP2LocationPO>()
                        .ge(IP2LocationPO::getIpFrom,start)
                        .le(IP2LocationPO::getIpFrom,end)
                        .eq(IP2LocationPO::getAdministrativeAreaId,0)
        );

        int i = 0;
        for ( IP2LocationPO ip2LocationPO : locationPOS ) {
            AdministrativeAreaPO country = administrativeAreaMapper.selectOne(
                    new LambdaQueryWrapper<AdministrativeAreaPO>()
                            .eq(AdministrativeAreaPO::getNameEN,ip2LocationPO.getCountryName())
                            .le(AdministrativeAreaPO::getId,500)
            );
            if (country != null) {
                AdministrativeAreaPO region = administrativeAreaMapper.selectOne(
                        new LambdaQueryWrapper<AdministrativeAreaPO>()
                                .eq(AdministrativeAreaPO::getNameEN,ip2LocationPO.getRegionName())
                                .le(AdministrativeAreaPO::getSuperior,country.getId())
                );
                if (region != null) {
                    AdministrativeAreaPO area ;

                    // 如果城市名为域名
                    if (ip2LocationPO.getCityName().equals(ip2LocationPO.getRegionName())) {
                        area = region;
                    } else {
                        List<AdministrativeAreaPO> cities = administrativeAreaMapper.selectList(
                                new LambdaQueryWrapper<AdministrativeAreaPO>()
                                        .eq(AdministrativeAreaPO::getNameEN,ip2LocationPO.getCityName())
                                        .le(AdministrativeAreaPO::getSuperior,region.getId())
                        );

                        if (cities != null && cities.size() == 1) {
                            area = cities.get(0);
                        } else {
                            area = null;
                        }

                        // 如果没找到就以上一级区划为目标
                        if (area == null) {
                            area = region;
                        }
                    }

                    // 写入数据
                    ip2LocationPO.setAdministrativeAreaId(area.getId());
                    ip2LocationMapper.update(
                            ip2LocationPO,
                            new LambdaQueryWrapper<IP2LocationPO>()
                                    .eq(IP2LocationPO::getIpFrom,ip2LocationPO.getIpFrom())
                                    .eq(IP2LocationPO::getIpTo,ip2LocationPO.getIpTo())
                    );
                    i ++ ;

                }
            }
        }
        return new int[]{i,locationPOS.size()};
    }

    // 检查国家
    // @Test
    public void checkCountries() {
        List<AdministrativeAreaPO> countries = administrativeAreaMapper.selectList(
                new LambdaQueryWrapper<AdministrativeAreaPO>()
                        .le(AdministrativeAreaPO::getId,500)
        );
        for (AdministrativeAreaPO country : countries) {
            boolean flag =ip2LocationMapper.exists(
                    new LambdaQueryWrapper<IP2LocationPO>()
                            .eq(IP2LocationPO::getCountryName,country.getNameEN())
            );
            if (!flag) {
                System.out.println(country.getNameEN());
            }
        }
    }

}

