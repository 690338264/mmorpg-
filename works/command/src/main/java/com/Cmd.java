package com;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Cmd {
    USER_CREATE("user_create",1000),
    USER_LOGIN("user_login",1001),
    USER_LISTPLAYER("user_listPlayer",1002),
    PLAYER_LOG("player_log",1003),
    PLAYER_CREATE("player_create",1004),
    AOI("aoi",1005),
    MOVE_TO("move_to",1006),
    BAG_LIST("bag_list",1007),
    BAG_INORDER("bag_inorder",1008),
    ITEM_USE("item_use",1009),
    ITEM_DROP("item_drop",1010),
    ITEM_SELL("item_sell",1011),
    UNKNOWN("unknown",9999);

    private String cmd;
    private Integer cmdID;

    private static final Map<Integer,Cmd> id_Map = new HashMap<>();
    private static final Map<String, Cmd> COMMAND_MAP = new  HashMap<>();
    Cmd(String cmd,Integer cmdID){
        this.cmd = cmd;
        this.cmdID = cmdID;
    }

    //将字符串命令与枚举对象通过map关联
    static {
        for(Cmd e : EnumSet.allOf(Cmd.class)){
            COMMAND_MAP.put(e.cmd,e);
            id_Map.put(e.cmdID,e);
        }
    }

    public static Cmd find(int cmdID,Cmd defaultvalue){
        Cmd value = id_Map.get(cmdID);
        if(value == null){
            return defaultvalue;
        }
        return value;
    }
    public String getcmd(){
        return cmd;
    }
    public Integer getcmdID(){
        return cmdID;
    }

    @Override
    public String toString(){
        return "Cmd{"+"cmd = [" + cmd + ']' +",cmdID = " + cmdID+'}';
    }
}
