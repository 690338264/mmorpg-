package com.function.npc.excel;

import lombok.Data;

/**
 * @author Catherine
 */
@Data
public class NpcExcel {
    private Integer id;

    private String name;
    /**
     * 交谈文本
     */
    private String text;

    private Integer status;
}
