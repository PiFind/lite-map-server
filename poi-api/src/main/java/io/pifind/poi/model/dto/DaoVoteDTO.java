package io.pifind.poi.model.dto;

import lombok.Data;

/**
 * DAO 投票 DTO
 */
@Data
public class DaoVoteDTO {

    /**
     * 兴趣点 ID
     */
    private Long interestPointId;

    /**
     * 是否同意
     */
    private Boolean agree;

    /**
     * 备注
     */
    private String note;

}
