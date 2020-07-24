package com.function.npc.model;

import lombok.Data;
import util.excel.EntityName;
import util.excel.ExcelGetKey;

@Data
public class SceneLife implements ExcelGetKey<Long> {
    @EntityName(column = "id")
    private Long id;
    @EntityName(column = "名字")
    private String name;
    @EntityName(column = "交谈文本")
    private String txt = "";

    @EntityName(column = "状态")
    private Integer state;
    @Override
    public Long getKey(){
        return id;
    }

}
