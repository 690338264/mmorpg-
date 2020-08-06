package com;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 */

public enum Cmd {
    //用户注册
    USER_CREATE("user_create", 1000),
    //用户登录
    USER_LOGIN("user_login", 1001),
    //角色列表
    USER_LISTPLAYER("user_listPlayer", 1002),
    //角色登录
    PLAYER_LOG("player_log", 1003),
    //角色创建
    PLAYER_CREATE("player_create", 1004),
    //查看场景
    AOI("aoi", 1005),
    //和NPC谈话
    TALK_TO("talk_to", 1006),
    //查看相邻地图
    WHERE_CAN_GO("where_can_go", 1007),
    //转移场景
    MOVE_TO("move_to", 1008),
    //攻击
    ATTACK("attack", 1009),
    //查看背包
    BAG_LIST("bag_list", 1010),
    //整理背包
    BAG_INORDER("bag_inorder", 1011),
    //穿戴装备
    EQUIP_ON("equip_on", 10101),
    //使用药品
    ITEM_USE("item_use", 10102),
    //丢弃物品
    ITEM_DROP("item_drop", 10103),
    //拖下装备
    EQUIP_OFF("equip_off", 10104),
    //查看人物状态属性
    PLAYER_STATE("player_state", 888),
    //查看已穿戴装备
    EQUIP_LIST("equip_list", 999),
    UNKNOWN("unknown", 9999);

    private String cmd;
    private Integer cmdId;

    private static final Map<Integer, Cmd> id_Map = new HashMap<>();
    private static final Map<String, Cmd> COMMAND_MAP = new HashMap<>();

    Cmd(String cmd, Integer cmdId) {
        this.cmd = cmd;
        this.cmdId = cmdId;
    }

    //将字符串命令与枚举对象通过map关联
    static {
        for (Cmd e : EnumSet.allOf(Cmd.class)) {
            COMMAND_MAP.put(e.cmd, e);
            id_Map.put(e.cmdId, e);
        }
    }

    public static Cmd find(int cmdId, Cmd defaultvalue) {
        Cmd value = id_Map.get(cmdId);
        if (value == null) {
            return defaultvalue;
        }
        return value;
    }

    public String getCmd() {
        return cmd;
    }

    public Integer getCmdId() {
        return cmdId;
    }

    @Override
    public String toString() {
        return "Cmd{" + "cmd = [" + cmd + ']' + ",cmdID = " + cmdId + '}';
    }
}
