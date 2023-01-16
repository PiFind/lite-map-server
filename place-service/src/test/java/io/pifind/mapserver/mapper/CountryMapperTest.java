package io.pifind.mapserver.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.pifind.mapserver.PlaceApplication;
import io.pifind.mapserver.model.po.AdministrativeAreaPO;
import io.pifind.mapserver.model.po.CountryPO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
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

}
