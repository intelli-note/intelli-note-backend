package com.demiphea.model.vo.user;

import com.demiphea.model.vo.note.NoteOverviewVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * BillVo
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillVo {
    private Long id;
    private Integer type;
    private BigDecimal amount;
    private NoteOverviewVo note;
    @JsonProperty("create_time")
    private LocalDateTime createTime;
}
