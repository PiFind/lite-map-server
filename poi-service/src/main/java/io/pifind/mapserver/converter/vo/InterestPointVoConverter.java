package io.pifind.mapserver.converter.vo;

import io.pifind.common.converter.AdvancedConverter;
import io.pifind.map.constant.GeographicCoordinateSystemEnum;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.mapserver.model.constant.InterestPointStatusEnum;
import io.pifind.mapserver.model.po.InterestPointPO;
import io.pifind.mapserver.model.po.component.TimeIntervalPO;
import io.pifind.mapserver.model.po.component.TimeIntervalSet;
import io.pifind.poi.constant.BusinessStatusEnum;
import io.pifind.poi.constant.PoiStatusEnum;
import io.pifind.poi.constant.WeekEnum;
import io.pifind.poi.model.component.BusinessTimeDTO;
import io.pifind.poi.model.component.TimeIntervalDTO;
import io.pifind.poi.model.vo.InterestPointVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface InterestPointVoConverter extends AdvancedConverter<InterestPointPO,InterestPointVO> {

    /**
     * 将兴趣点PO对象转换为兴趣点VO对象
     * @param po {@link InterestPointPO 兴趣点PO对象}
     * @return 转换后的 {@link InterestPointVO 兴趣点DTO对象}
     */
    @Override
    default InterestPointVO convert(InterestPointPO po) {
        InterestPointVO vo = liteConvert(po);

        /*
         * 设置营业时间
         */

        BusinessTimeDTO businessTime = new BusinessTimeDTO();

        // 提取营业日
        Map<io.pifind.mapserver.model.constant.WeekEnum,Boolean> weekMap = po.getBusinessDay();
        if (weekMap != null) {
            List<WeekEnum> weekList = new ArrayList<>();
            for (io.pifind.mapserver.model.constant.WeekEnum day : weekMap.keySet()) {
                if (weekMap.get(day) == Boolean.TRUE) {
                    switch (day) {
                        case MONDAY:
                            weekList.add(WeekEnum.MONDAY);
                            break;
                        case TUESDAY:
                            weekList.add(WeekEnum.TUESDAY);
                            break;
                        case WEDNESDAY:
                            weekList.add(WeekEnum.WEDNESDAY);
                            break;
                        case THURSDAY:
                            weekList.add(WeekEnum.THURSDAY);
                            break;
                        case FRIDAY:
                            weekList.add(WeekEnum.FRIDAY);
                            break;
                        case SATURDAY:
                            weekList.add(WeekEnum.SATURDAY);
                            break;
                        case SUNDAY:
                            weekList.add(WeekEnum.SUNDAY);
                            break;
                    }
                }
            }
            businessTime.setBusinessDay(weekList);
        }

        // 提取营业时间段

        TimeIntervalSet timeIntervalSet = po.getBusinessHours();
        List<TimeIntervalDTO> businessHours = new ArrayList<>();
        for (TimeIntervalPO timeIntervalPO : timeIntervalSet) {

            double start = timeIntervalPO.getStart();
            double end = timeIntervalPO.getEnd();

            // 转换为时间标准格式
            String startTime = String.format("%02d:%02d",(int)start,(int)((start - ((int)start))*60.0));
            String endTime = String.format("%02d:%02d",(int)end,(int)((end - ((int)end))*60.0));

            // 创建 DTO 对象
            TimeIntervalDTO timeIntervalDTO = new TimeIntervalDTO();
            timeIntervalDTO.setStartTime(startTime);
            timeIntervalDTO.setEndTime(endTime);
            businessHours.add(timeIntervalDTO);

        }
        businessTime.setBusinessHours(businessHours);

        // 提取假期时间

        businessTime.setVacationStartTime(po.getVacationStartTime());
        businessTime.setVacationEndTime(po.getVacationEndTime());

        vo.setBusinessTime(businessTime);

        /*
         * 营业状态转换
         */

        io.pifind.mapserver.model.constant.BusinessStatusEnum businessStatus = po.getBusinessStatus();
        if (businessStatus != null) {
            switch (businessStatus) {
                case OPEN:
                    vo.setBusinessStatus(BusinessStatusEnum.OPEN);
                    break;
                case CLOSE:
                    vo.setBusinessStatus(BusinessStatusEnum.CLOSE);
                    break;
                case BANKRUPT:
                    vo.setBusinessStatus(BusinessStatusEnum.BANKRUPT);
                    break;
                default:
                    for (BusinessStatusEnum status : BusinessStatusEnum.values()) {
                        if (status.name().equals(businessStatus.name())) {
                            vo.setBusinessStatus(status);
                            break;
                        }
                    }
            }
        }

        /*
         * POI状态转换
         */

        InterestPointStatusEnum interestPointStatus = po.getPoiStatus();
        if (interestPointStatus != null) {
            switch (interestPointStatus) {
                case VERIFIED:
                    vo.setPoiStatus(PoiStatusEnum.VERIFIED);
                    break;
                case INVALID:
                    vo.setPoiStatus(PoiStatusEnum.INVALID);
                    break;
                case UNVERIFIED:
                    vo.setPoiStatus(PoiStatusEnum.UNVERIFIED);
                    break;
                default:
                    for (PoiStatusEnum status : PoiStatusEnum.values()) {
                        if (status.name().equals(interestPointStatus.name())) {
                            vo.setPoiStatus(status);
                            break;
                        }
                    }
            }
        }

        /*
         * 评分转换
         */

        // 实际分数为 正常分数 + 隐藏分数
        int realScore = po.getTotalScore() + po.getHiddenScore();
        double score = realScore/(double)po.getTotalParticipants();
        if (score > 5.0) {
            score = 5;
        }
        // 展示出保留一位小数的分数
        vo.setScore(Double.parseDouble(String.format("%.1f",score)));

        /*
         * 坐标转换
         */
        CoordinateDTO coordinateDTO = new CoordinateDTO();
        coordinateDTO.setLatitude(po.getLatitude());
        coordinateDTO.setLongitude(po.getLongitude());
        switch (po.getCoordinateSystem()) {
            case WGS84:
                coordinateDTO.setSystem(GeographicCoordinateSystemEnum.WGS84);
                break;
            case GCJ02:
                coordinateDTO.setSystem(GeographicCoordinateSystemEnum.GCJ02);
                break;
            case CGCS2000:
                coordinateDTO.setSystem(GeographicCoordinateSystemEnum.CGCS2000);
                break;
        }
        vo.setCoordinate(coordinateDTO);

        return vo;
    }

    /**
     * 不推荐使用轻转换器
     * <p>
     *     这其实是在内部实现的轻转换器，其并不能实现DTO对象对PO对象的完全转换，请使用
     *     {@link InterestPointVoConverter#convert(InterestPointPO)}进行
     *     完全的转换
     * </p>
     * @param po {@link InterestPointPO 兴趣点PO对象}
     * @return 转换后的 {@link InterestPointVO 兴趣点DTO对象}
     * @see InterestPointVoConverter#convert(InterestPointPO)
     */
    @Deprecated
    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "name",source = "name"),
            @Mapping(target = "nameEN",source = "nameEN"),
            @Mapping(target = "address",source = "address"),
            @Mapping(target = "addressEN",source = "addressEN"),
            @Mapping(target = "categoryId",source = "categoryId"),
            @Mapping(target = "administrativeAreaId",source = "administrativeAreaId"),
            @Mapping(target = "businessLicense",source = "businessLicense"),
            @Mapping(target = "tels",source = "tels"),
            @Mapping(target = "tels",source = "tels"),
            @Mapping(target = "logo",source = "logo"),
            @Mapping(target = "images",source = "images"),
            @Mapping(target = "consumptionPerPerson",source = "consumptionPerPerson"),
            @Mapping(target = "consumptionCurrency",source = "consumptionCurrency"),
            @Mapping(target = "supportedCurrencies",source = "supportedCurrencies"),
            @Mapping(target = "tags",source = "tags"),
            @Mapping(target = "publisher",source = "publisher"),
            @Mapping(target = "hash",source = "hash"),
            @Mapping(target = "pageviews",source = "pageviews"),
            @Mapping(target = "collections",source = "collections"),
            @Mapping(target = "createTime",source = "createTime"),
            @Mapping(target = "updateTime",source = "updateTime"),
    })
    InterestPointVO liteConvert(InterestPointPO po);

}
