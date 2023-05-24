package io.pifind.place.api;

import io.pifind.common.response.R;
import io.pifind.place.model.AdministrativeAreaDTO;

import javax.validation.constraints.NotNull;

/**
 * 行政区划服务
 */
public interface IAdministrativeAreaService {

    /**
     * 判断是否存在行政区
     * @param id 行政区ID
     * @return 返回值类型为 {@link Boolean}
     * <ul>
     *     <li><b>存在行政区</b> - 返回 {@code true} </li>
     *     <li><b>不存在行政区</b> - 返回 {@code false} </li>
     * </ul>
     */
    R<Boolean> existAdministrativeAreaById(@NotNull Long id);

    /**
     * 通过ID获取行政区划
     * <p>
     *     通过该方法首先会根据行政区ID找到对应的行政区，然后再以
     *     其为根节点，往下寻找子节点，直到树的高度达到 layerLevel
     *     或者达到当前树的最大高度
     * </p>
     * @param id 行政区ID
     * @param deep 行政区下分深度
     * @return 返回值类型为 {@link AdministrativeAreaDTO}
     * <ul>
     *     <li><b>存在行政区ID</b> - 返回 {@link AdministrativeAreaDTO 行政区实体对象} </li>
     *     <li><b>不存在行政区ID</b> - 返回 {@code null}</li>
     * </ul>
     */
    R<AdministrativeAreaDTO> getAdministrativeAreaById(@NotNull Long id,@NotNull Integer deep);

    /**
     * 获取ID详细地址,例如：
     * <p>
     * 在中文环境下,如果我们使用如下输入：
     * </p>
     * <ul>
     *     <li>{@code id=145010103L;}</li>
     *     <li>{@code separator = "-";}</li>
     * </ul>
     * 获取的行政区名称为 <b>朝阳区</b> ，那么返回结果就是：
     * <b>中国-北京-北京市-朝阳区</b>
     *
     * @param id 行政区ID
     * @param separator 分隔符
     * @return 返回值类型为 {@link String}
     * <ul>
     *     <li><b>存在ID</b> - 返回 {@link String 拼接好的字符串}</li>
     *     <li><b>不存在ID</b> - 返回 {@code null}</li>
     * </ul>
     */
    R<String> getDetailedAddress(@NotNull Long id,@NotNull String separator);

}
