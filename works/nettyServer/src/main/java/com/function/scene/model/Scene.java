package com.function.scene.model;

import com.database.entity.Player;
import com.function.npc.model.Npc;
import lombok.Data;
import util.excel.EntityName;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Scene {
    @EntityName(column = "Id")
    private Integer id;

    @EntityName(column = "名称")
    private String name;

    @EntityName(column = "相邻场景")
    private String neighbour;

    @EntityName(column = "场景NPC")
    private String sceneNPC;

    @EntityName(column = "场景类型")
    private String sceneType;

    private Map<Long, Player> playerMap = new ConcurrentHashMap<>();

    private Map<Long, Npc> npcMap = new ConcurrentHashMap<>();


    public String display(){
        return MessageFormat.format("id:{0}  name:{1}",this.getId(),this.getName());
    }
}
