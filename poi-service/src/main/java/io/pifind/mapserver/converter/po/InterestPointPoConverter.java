package io.pifind.mapserver.converter.po;

import io.pifind.map.OpenLocationCode;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.mapserver.model.constant.BusinessStatusEnum;
import io.pifind.mapserver.model.constant.InterestPointStatusEnum;
import io.pifind.mapserver.model.constant.WeekEnum;
import io.pifind.mapserver.model.po.InterestPointPO;
import io.pifind.mapserver.model.po.component.TimeIntervalPO;
import io.pifind.mapserver.model.po.component.TimeIntervalSet;
import io.pifind.mapserver.type.Point;
import io.pifind.poi.constant.PoiStatusEnum;
import io.pifind.poi.model.InterestPointDTO;
import io.pifind.poi.model.component.BusinessTimeDTO;
import io.pifind.poi.model.component.TimeIntervalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface InterestPointPoConverter {

    /**
     * 将兴趣点DTO对象转换为兴趣点PO对象
     * @param dto {@link InterestPointDTO 兴趣点DTO对象}
     * @return 转换后的 {@link InterestPointPO 兴趣点PO对象}
     */
    default InterestPointPO convert(InterestPointDTO dto) {

        InterestPointPO interestPointPO = liteConvert(dto);
        BusinessTimeDTO businessTime =  dto.getBusinessTime();

        /*
         * 提取营业时间相关数据
         */

        if (businessTime != null) {

            /*
             * 提取营业日
             */

            List<io.pifind.poi.constant.WeekEnum> week = businessTime.getBusinessDay();
            Map<WeekEnum,Boolean> weekMap = new HashMap<>();
            for (io.pifind.poi.constant.WeekEnum day : week) {
                switch (day) {
                    case MONDAY:
                        weekMap.put(WeekEnum.MONDAY,true);
                        break;
                    case TUESDAY:
                        weekMap.put(WeekEnum.TUESDAY,true);
                        break;
                    case WEDNESDAY:
                        weekMap.put(WeekEnum.WEDNESDAY,true);
                        break;
                    case THURSDAY:
                        weekMap.put(WeekEnum.THURSDAY,true);
                        break;
                    case FRIDAY:
                        weekMap.put(WeekEnum.FRIDAY,true);
                        break;
                    case SATURDAY:
                        weekMap.put(WeekEnum.SATURDAY,true);
                        break;
                    case SUNDAY:
                        weekMap.put(WeekEnum.SUNDAY,true);
                        break;
                }
            }
            interestPointPO.setBusinessDay(weekMap);

            /*
             * 提取营业时间段
             */

            List<TimeIntervalDTO> timeIntervals = businessTime.getBusinessHours();
            TimeIntervalSet timeIntervalSet = new TimeIntervalSet();
            String timeFormatRegex = "^\\s+[0-9]{2}\\s+:\\s+[0-9]{2}\\s+$";
            for (TimeIntervalDTO timeInterval : timeIntervals) {
                double start = -1,end = -1;

                String startTime = timeInterval.getStartTime();
                if (startTime.matches(timeFormatRegex)) {
                    String[] blocks = startTime.split(":");
                    start = Double.parseDouble(blocks[0]) +
                            (Double.parseDouble(blocks[1])/60.0);
                }

                String endTime = timeInterval.getEndTime();
                if (endTime.matches(timeFormatRegex)) {
                    String[] blocks = startTime.split(":");
                    end = Double.parseDouble(blocks[0]) +
                            (Double.parseDouble(blocks[1])/60.0);
                }

                TimeIntervalPO timeIntervalPO = new TimeIntervalPO();
                if (0 <= start && end < 24 && start < end) {
                    timeIntervalPO.setStart(start);
                    timeIntervalPO.setEnd(end);
                }

                timeIntervalSet.add(timeIntervalPO);
            }
            interestPointPO.setBusinessHours(timeIntervalSet);

            /*
             * 提取休假时间
             */

            interestPointPO.setVacationStartTime(businessTime.getVacationStartTime());
            interestPointPO.setVacationEndTime(businessTime.getVacationEndTime());

        }

        /*
         * 营业状态转换
         */

        io.pifind.poi.constant.BusinessStatusEnum businessStatus = dto.getBusinessStatus();
        if (businessStatus != null) {
            switch (businessStatus) {
                case NORMAL:
                    interestPointPO.setBusinessStatus(BusinessStatusEnum.NORMAL);
                    break;
                case CLOSE:
                    interestPointPO.setBusinessStatus(BusinessStatusEnum.CLOSE);
                    break;
                case BANKRUPT:
                    interestPointPO.setBusinessStatus(BusinessStatusEnum.BANKRUPT);
                    break;
                default:
                    for (BusinessStatusEnum status : BusinessStatusEnum.values()) {
                        if (status.name().equals(businessStatus.name())) {
                            interestPointPO.setBusinessStatus(status);
                            break;
                        }
                    }
            }
        }

        /*
         * POI状态转换
         */

        PoiStatusEnum poiStatus = dto.getPoiStatus();
        if (poiStatus != null) {
            switch (poiStatus) {
                case INVALID:
                    interestPointPO.setPoiStatus(InterestPointStatusEnum.INVALID);
                    break;
                case VERIFIED:
                    interestPointPO.setPoiStatus(InterestPointStatusEnum.VERIFIED);
                    break;
                case UNVERIFIED:
                    interestPointPO.setPoiStatus(InterestPointStatusEnum.UNVERIFIED);
                    break;
                default:
                    for (InterestPointStatusEnum status : InterestPointStatusEnum.values()) {
                        if (status.name().equals(poiStatus.name())) {
                            interestPointPO.setPoiStatus(status);
                            break;
                        }
                    }
            }
        }

        /*
         * 坐标转换
         */

        CoordinateDTO coordinate = dto.getCoordinate();
        if (coordinate != null) {

            // 定位坐标
            Point location = new Point();
            location.setLatitude(coordinate.getLatitude());
            location.setLongitude(coordinate.getLongitude());
            interestPointPO.setLocation(location);

            // Plus Code
            OpenLocationCode plusCode = new OpenLocationCode(
                    coordinate.getLatitude(),
                    coordinate.getLongitude(),
                    12);
            interestPointPO.setPlusCode(plusCode.getCode());

        }

        return interestPointPO;
    }

    /**
     * 不推荐使用轻转换器
     * <p>
     *     这其实是在内部实现的轻转换器，其并不能实现DTO对象对PO对象的完全转换，请使用
     *     {@link InterestPointPoConverter#convert(InterestPointDTO)}进行
     *     完全的转换
     * </p>
     * @param dto {@link InterestPointDTO 兴趣点DTO对象}
     * @return 转换后的 {@link InterestPointPO 兴趣点PO对象}
     * @see InterestPointPoConverter#convert(InterestPointDTO)
     */
    @Deprecated
    @Mappings({
            @Mapping(target = "name",source = "name"),
            @Mapping(target = "nameEN",source = "nameEN"),
            @Mapping(target = "address",source = "address"),
            @Mapping(target = "addressEN",source = "addressEN"),
            @Mapping(target = "categoryId",source = "categoryId"),
            @Mapping(target = "administrativeAreaId",source = "administrativeAreaId"),
            @Mapping(target = "businessLicense",source = "businessLicense"),
            @Mapping(target = "tels",source = "tels"),
            @Mapping(target = "images",source = "images"),
            @Mapping(target = "consumptionPerPerson",source = "consumptionPerPerson"),
            @Mapping(target = "consumptionCurrency",source = "consumptionCurrency"),
            @Mapping(target = "supportedCurrencies",source = "supportedCurrencies"),
            @Mapping(target = "tags",source = "tags"),
            @Mapping(target = "publisher",source = "publisher"),
            @Mapping(target = "pageviews",source = "pageviews"),
            @Mapping(target = "collections",source = "collections"),
            @Mapping(target = "createTime",source = "createTime"),
            @Mapping(target = "updateTime",source = "updateTime"),
    })
    InterestPointPO liteConvert(InterestPointDTO dto);

}
