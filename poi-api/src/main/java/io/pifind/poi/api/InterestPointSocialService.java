package io.pifind.poi.api;

import io.pifind.common.response.R;

import javax.validation.constraints.NotNull;

/**
 * 兴趣点社交服务服务
 * <p>
 *     社交服务包括浏览、收藏、评分等服务，因为这些服务都属于高频服务，
 *     所以这里需要进行单独列出
 * </p>
 */
public interface InterestPointSocialService {

    /**
     * 兴趣点的浏览量增加 1
     * @param username 用户名
     * @param id 兴趣点ID
     * @return 无返回值
     * <ul>
     *     <li><b>浏览量增加成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>浏览量增加失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    R<Void> browse(String username,@NotNull Long id);

    /**
     * 兴趣点的收藏量增加 1
     * @param username 用户名
     * @param id 兴趣点ID
     * @return 无返回值
     * <ul>
     *     <li><b>收藏量增加成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>收藏量增加失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    R<Void> collect(String username,@NotNull Long id);

    /**
     * 对兴趣点进行评价
     * @param username 用户名
     * @param id 兴趣点ID
     * @param score 用户的评分
     * @return 无返回值
     * <ul>
     *     <li><b>收藏量增加成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>收藏量增加失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    R<Void> evaluate(String username,@NotNull Long id,@NotNull Double score);

}
