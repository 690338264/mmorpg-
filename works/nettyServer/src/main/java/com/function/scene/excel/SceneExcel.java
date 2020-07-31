package com.function.scene.excel;

import com.function.monster.model.Monster;
import com.function.player.model.PlayerModel;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SceneExcel {

    private Integer id;

    private String name;

    private String neighbor;

    private String npc;

    private String monster;

    private Map<Long, PlayerModel> players = new HashMap<Long, PlayerModel>();

    private Map<Integer, Monster> monsters = new HashMap<Integer, Monster>();


}
