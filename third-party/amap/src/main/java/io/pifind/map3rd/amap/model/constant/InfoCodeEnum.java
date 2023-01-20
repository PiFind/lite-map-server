package io.pifind.map3rd.amap.model.constant;

import io.pifind.common.annotation.ErrorCode;
import io.pifind.common.response.StandardCode;
import io.pifind.map3rd.amap.AmapConstants;
import io.pifind.map3rd.error.MapApiCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 错误码
 */
@ErrorCode(translate = false,messagePrefix = AmapConstants.PLATFORM_NAME + ":")
public enum InfoCodeEnum {

    OK(StandardCode.SUCCESS,"10000","请求正常"),
    INVALID_USER_KEY(MapApiCode.AUTHENTICATION_ERROR,"10001","key不正确或过期"),
    SERVICE_NOT_AVAILABLE(MapApiCode.SERVER_REJECT_ERROR,"10002","没有权限使用相应的服务或者请求接口的路径拼写错误"),
    DAILY_QUERY_OVER_LIMIT(MapApiCode.QUOTA_LIMIT_EXCEEDED,"10003","访问已超出日访问量"),
    ACCESS_TOO_FREQUENT(MapApiCode.SERVER_REJECT_ERROR,"10004","单位时间内访问过于频繁"),
    INVALID_USER_IP(MapApiCode.SERVER_REJECT_ERROR,"10005","IP白名单出错，发送请求的服务器IP不在IP白名单内"),
    INVALID_USER_DOMAIN(MapApiCode.SERVER_REJECT_ERROR,"10006","绑定域名无效"),
    INVALID_USER_SIGNATURE(MapApiCode.SIGNATURE_ERROR,"10007","数字签名未通过验证"),
    INVALID_USER_SCODE(MapApiCode.AUTHENTICATION_ERROR,"10008","MD5安全码未通过验证"),
    USERKEY_PLAT_NOMATCH(MapApiCode.AUTHENTICATION_ERROR,"10009","请求key与绑定平台不符"),
    IP_QUERY_OVER_LIMIT(MapApiCode.QUOTA_LIMIT_EXCEEDED,"10010","IP访问超限"),
    NOT_SUPPORT_HTTPS(MapApiCode.UNSUPPORTED_SERVICE_ERROR,"10011","服务不支持https请求"),
    INSUFFICIENT_PRIVILEGES(MapApiCode.SERVER_REJECT_ERROR,"10012","权限不足，服务请求被拒绝"),
    USER_KEY_RECYCLED(MapApiCode.AUTHENTICATION_ERROR,"10013","Key被删除"),
    QPS_HAS_EXCEEDED_THE_LIMIT(MapApiCode.QUOTA_LIMIT_EXCEEDED,"10014","云图服务QPS超限"),
    GATEWAY_TIMEOUT(MapApiCode.QUOTA_LIMIT_EXCEEDED,"10015","受单机QPS限流限制"),
    SERVER_IS_BUSY(MapApiCode.SERVER_REJECT_ERROR,"10016","服务器负载过高"),
    RESOURCE_UNAVAILABLE(MapApiCode.SERVER_REJECT_ERROR,"10017","所请求的资源不可用"),
    CQPS_HAS_EXCEEDED_THE_LIMIT(MapApiCode.QUOTA_LIMIT_EXCEEDED,"10019","使用的某个服务总QPS超限"),
    CKQPS_HAS_EXCEEDED_THE_LIMIT(MapApiCode.QUOTA_LIMIT_EXCEEDED,"10020","某个Key使用某个服务接口QPS超出限制"),
    CUQPS_HAS_EXCEEDED_THE_LIMIT(MapApiCode.QUOTA_LIMIT_EXCEEDED,"10021","账号使用某个服务接口QPS超出限制"),
    INVALID_REQUEST(MapApiCode.AUTHENTICATION_ERROR,"10026","账号处于被封禁状态"),
    ABROAD_DAILY_QUERY_OVER_LIMIT(MapApiCode.QUOTA_LIMIT_EXCEEDED,"10029","某个Key的QPS超出限制"),
    NO_EFFECTIVE_INTERFACE(MapApiCode.AUTHENTICATION_ERROR,"10041","请求的接口权限过期"),
    USER_DAILY_QUERY_OVER_LIMIT(MapApiCode.QUOTA_LIMIT_EXCEEDED,"10044","账号维度日调用量超出限制"),
    USER_ABROAD_DAILY_QUERY_OVER_LIMIT(MapApiCode.QUOTA_LIMIT_EXCEEDED,"10045","账号维度海外服务日调用量超出限制"),
    INVALID_PARAMS(MapApiCode.PARAMETER_ERROR,"20000","请求参数非法"),
    MISSING_REQUIRED_PARAMS(MapApiCode.PARAMETER_ERROR,"20001","缺少必填参数"),
    ILLEGAL_REQUEST(MapApiCode.PARAMETER_ERROR,"20002","请求协议非法"),
    UNKNOWN_ERROR(MapApiCode.UNKNOWN_ERROR,"20003","其他未知错误"),
    INSUFFICIENT_ABROAD_PRIVILEGES(MapApiCode.AUTHENTICATION_ERROR,"20011","查询坐标或规划点（包括起点、终点、途经点）在海外，但没有海外地图权限"),
    ILLEGAL_CONTENT(MapApiCode.PARAMETER_ERROR,"20012","查询信息存在非法内容"),
    OUT_OF_SERVICE(MapApiCode.PARAMETER_ERROR,"20800","规划点（包括起点、终点、途经点）不在中国陆地范围内"),
    NO_ROADS_NEARBY(MapApiCode.INVALID_RESULT,"20801","划点（起点、终点、途经点）附近搜不到路"),
    ROUTE_FAIL(MapApiCode.INVALID_RESULT,"20802","路线计算失败，通常是由于道路连通关系导致"),
    OVER_DIRECTION_RANGE(MapApiCode.PARAMETER_ERROR,"20803","起点终点距离过长。"),
    ENGINE_RESPONSE_DATA_ERROR(MapApiCode.UNKNOWN_ERROR,"^3[0-9]{4}$","服务响应失败。"),
    QUOTA_PLAN_RUN_OUT(MapApiCode.QUOTA_LIMIT_EXCEEDED,"40000","余额耗尽"),
    GEOFENCE_MAX_COUNT_REACHED(MapApiCode.PARAMETER_ERROR,"40001","围栏个数达到上限"),
    SERVICE_EXPIRED(MapApiCode.QUOTA_LIMIT_EXCEEDED,"40002","购买服务到期"),
    ABROAD_QUOTA_PLAN_RUN_OUT(MapApiCode.QUOTA_LIMIT_EXCEEDED,"40003","海外服务余额耗尽"),
    ;

    // 标准错误码
    private final int code;
    // 正则表达式
    private final String expression;
    // 描述
    private final String description;

    private static Map<String,InfoCodeEnum> codeMap;
    private static List<InfoCodeEnum> regularInfoCodes;

    InfoCodeEnum(int code,String expression, String description) {
        this.code = code;
        this.expression = expression;
        this.description = description;
    }

    public int code() {
        return code;
    }

    public String expression() {
        return expression;
    }

    public String description() {
        return description;
    }

    public static InfoCodeEnum matches(String code) {
        if (code == null) {
            return null;
        }
        if (codeMap == null || regularInfoCodes == null) {
            codeMap = new HashMap<>();
            regularInfoCodes = new ArrayList<>();
            for (InfoCodeEnum infoCode : InfoCodeEnum.values()) {
                if (infoCode.expression.matches("^[0-9]{5}$")) {
                    codeMap.put(infoCode.expression,infoCode);
                } else {
                    regularInfoCodes.add(infoCode);
                }
            }
        }
        // 先从map里面直接取值
        if (codeMap.containsKey(code)) {
            return codeMap.get(code);
        } else {
            // 如果没有就用正则表达式去匹配
            for (InfoCodeEnum infoCode : regularInfoCodes) {
                if(code.matches(infoCode.expression)) {
                    return infoCode;
                }
            }
        }
        return null;
    }

}
