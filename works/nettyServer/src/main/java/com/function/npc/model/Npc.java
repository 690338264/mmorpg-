package com.function.npc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.MessageFormat;

@Data
@EqualsAndHashCode(callSuper = true)
public class Npc extends SceneLife implements Life{
    public String display(){
        return MessageFormat.format("id:{0}  name:{1} {2}"
                ,this.getId(),this.getName(), this.getState()==-1?"死亡":"存活");
    }
}
