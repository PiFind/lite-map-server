package io.pifind.mapserver.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.pifind.mapserver.PlaceApplication;
import io.pifind.mapserver.model.AdministrativeAreaPO;
import io.pifind.mapserver.model.CountryPO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = PlaceApplication.class)
public class CountryMapperTest {

    @Autowired
    private AdministrativeAreaMapper administrativeAreaMapper;

    @Autowired
    private CountryMapper countryMapper;

    // @Test
    public void test() throws IOException {
        String url = "https://www.guojiadaima.com/";
        Document document = Jsoup.connect(url).get();
        List<Element> tables=  document.body().getElementsByTag("table");
        Element table = tables.get(1);

        Element tbody = table.getElementsByTag("tbody").get(0);
        List<Element> rows = tbody.getElementsByTag("tr");

        int p = 0;
        for (Element row : rows) {
            if (row.classNames().contains("e-rowbg")) {
                continue;
            }
            List<Element> cols = row.getElementsByTag("td");
            Integer sn = Integer.parseInt(cols.get(1).text());
            String nameCN = cols.get(3).text();
            String nameEN = cols.get(4).text();
            String iso2 = cols.get(5).text();
            String iso3 = cols.get(6).text();
            Integer number = Integer.parseInt(cols.get(7).text());
            String areaCode = cols.get(8).text();
            String domain = cols.get(9).text();
            String remarks = cols.get(10).text();

            if (nameCN.equals("塞尔维亚")) {
                nameCN = "塞尔维亚和黑山";
            } else if (nameCN.equals("圣皮埃尔和密克隆")) {
                nameCN = "圣皮埃尔和密克隆群岛";
            } else if (nameCN.equals("俄罗斯联邦")) {
                nameCN = "俄罗斯";
            } else if (nameCN.equals("北马里亚纳")) {
                nameCN = "北马里亚纳群岛";
            } else if (nameCN.equals("荷属安的列斯")) {
                nameCN = "荷属安地列斯";
            } else if (nameCN.equals("赫德岛和麦克唐纳岛")) {
                nameCN = "赫德和麦克唐纳群岛";
            } else if (nameCN.equals("福克兰群岛")) {
                nameCN = "弗兰克群岛";
            } else if (nameCN.equals("南乔治亚岛和南桑德韦奇岛")) {
                nameCN = "南乔治亚和南桑德韦奇群岛";
            } else if (nameCN.equals("斯瓦尔巴岛和扬马延岛")) {
                nameCN = "斯瓦尔巴和扬马延";
            } else if (nameCN.equals("刚果（布）")) {
                nameCN = "刚果";
            } else if (nameCN.equals("刚果（金）")) {
                nameCN = "刚果民主共和国";
            } else if (nameCN.equals("美国本土外小岛屿")) {
                nameCN = "美属外岛";
            } else if (nameCN.equals("英国属地曼岛")) {
                nameCN = "曼岛";
            }

            AdministrativeAreaPO area = administrativeAreaMapper.selectOne(
                    new LambdaQueryWrapper<AdministrativeAreaPO>()
                            .eq(AdministrativeAreaPO::getNameCN,nameCN)
                            .le(AdministrativeAreaPO::getId,500)
            );

            if (area == null) {
                area = administrativeAreaMapper.selectOne(
                        new LambdaQueryWrapper<AdministrativeAreaPO>()
                                .eq(AdministrativeAreaPO::getNameEN,nameEN)
                                .le(AdministrativeAreaPO::getId,500)
                );
            }

            if (area != null) {

                if (countryMapper.exists(
                        new LambdaQueryWrapper<CountryPO>().eq(CountryPO::getId,area.getId())
                )) {
                    continue;
                }

                CountryPO countryPO = new CountryPO();
                countryPO.setId(area.getId());
                countryPO.setNameCN(nameCN);
                countryPO.setNameEN(nameEN);
                countryPO.setISO2(iso2);
                countryPO.setISO3(iso3);
                countryPO.setNumber(number);
                countryPO.setDomain(domain);
                countryPO.setRemark(remarks);
                String[] areaCodeBlocks =  areaCode.split("/");
                ArrayList<Integer> areaCodes = new ArrayList<>();
                for (int i = 0 ;i < areaCodeBlocks.length ; i ++ ) {
                    if (areaCodeBlocks[i].equals("-") ||
                            areaCodeBlocks[i].isEmpty() ||
                            areaCodeBlocks[i].matches("^\\s+$")) {
                        continue;
                    }
                    areaCodes.add(
                            Integer.parseInt(areaCodeBlocks[i].replaceAll("\\s+",""))
                    );
                }
                countryPO.setAreaCodes(areaCodes);
                countryMapper.insert(countryPO);
            } else {
                p ++;
                System.out.println(nameCN + ":" + iso2);
            }
        }

        System.out.println(p);

    }

    // @Test
    public void testCountry() {
        for (int i = 101; i < 341 ; i ++ ){
            boolean flag = countryMapper.exists(
                    new LambdaQueryWrapper<CountryPO>()
                            .eq(CountryPO::getId,i)
            );
            if (!flag) {
                AdministrativeAreaPO countryPO = administrativeAreaMapper.selectById(i);
                System.out.println(i + ":" + countryPO);
            }
        }
    }

    @Test
    public void dropPoiDatabase() {
        List<CountryPO> countries = countryMapper.selectList(null);
        for (CountryPO country : countries) {
            System.out.printf(
                    "DROP DATABASE poi_%s;\n",
                    country.getISO2().toLowerCase());
        }
    }

    @Test
    public void createPoiDatabase() {
        List<CountryPO> countries = countryMapper.selectList(null);
        for (CountryPO country : countries) {
            System.out.printf(
                    "CREATE DATABASE IF NOT EXISTS poi_%s default charset utf8mb4 COLLATE utf8mb4_general_ci;\n",
                    country.getISO2().toLowerCase());
        }
    }

    @Test
    public void createTables() throws IOException {
        StringBuilder builder = new StringBuilder();
        List<CountryPO> countries = countryMapper.selectList(null);

        File file = new File("E:\\database\\pifind\\lite-server-database\\POI\\create_poi_tables.sql");
        file.createNewFile();

        try(FileOutputStream fout = new FileOutputStream(file)) {

            for (CountryPO country : countries) {
                builder.append(String.format("USE poi_%s;\n",country.getISO2().toLowerCase()));
                for (int i = 0; i < 3 ; i ++) {
                    builder.append(createTable(i));
                }

                // 写数据
                byte[] bytes = builder.toString().getBytes();
                fout.write(bytes);

                // 清除缓存
                builder.delete(0,builder.length());
            }

        }
    }

    private String createTable(int n) {
        return String.format(
                        "DROP TABLE IF EXISTS `interest_point_%02d`;\n" +
                        "CREATE TABLE `interest_point_%02d`  (\n" +
                        "  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',\n" +
                        "  `name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '本地语言的名字',\n" +
                        "  `name_en` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '英语名字',\n" +
                        "  `category_id` int NOT NULL COMMENT '分类ID',\n" +
                        "  `administrative_area_id` int NOT NULL COMMENT '所属地区ID',\n" +
                        "  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '详细地址',\n" +
                        "  `tels` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电话（用 | 隔开）最多填写两个',\n" +
                        "  `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '简介',\n" +
                        "  `images` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片（用 | 隔开）',\n" +
                        "  `consumption_per_person` double NOT NULL COMMENT '人均消费',\n" +
                        "  `consumption_currency` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '人均消费使用的币种',\n" +
                        "  `supported_currencies` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支持的消费币种（用 | 隔开）',\n" +
                        "  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标签（用 | 隔开）',\n" +
                        "  `publisher` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上传者',\n" +
                        "  `reliability` int NOT NULL COMMENT '可信度（0 - 100，100为完全可靠）',\n" +
                        "  `score` double NOT NULL COMMENT '评分( 0 - 5 )',\n" +
                        "  `trusted_location` point NOT NULL COMMENT '可信定位点',\n" +
                        "  `conjectured_location` point NOT NULL COMMENT '推测定位点',\n" +
                        "  `hash` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一编码哈希值',\n" +
                        "  `status` tinyint NOT NULL COMMENT '状态',\n" +
                        "  `plus_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'Plus Code',\n" +
                        "  `extended_01` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '预留字段01',\n" +
                        "  `extended_02` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '预留字段02',\n" +
                        "  `extended_03` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '预留字段03',\n" +
                        "  `create_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '创建时间',\n" +
                        "  `update_time` datetime NOT NULL DEFAULT current_timestamp ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                        "  `unavailable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '逻辑删除',\n" +
                        "  PRIMARY KEY (`id`) USING BTREE\n" +
                        ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'POI 兴趣点数据表' ROW_FORMAT = DYNAMIC;",
                n,
                n
        );
    }

}
