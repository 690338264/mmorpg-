package com.function.scene.model;

import com.database.entity.Player;

import lombok.Data;
import util.excel.EntityName;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Scene {
    private Integer id;

    private String name;

    private String neighbor;

    private Integer npcs;

    private Integer type;
}
