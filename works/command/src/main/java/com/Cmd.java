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
    //修理装备
    EQUIP_FIX("equip_fix", 10105),
    //查看商店系统
    SHOW_SHOP("show_shop", 1012),
    //买东西
    BUY("buy", 10121),
    //卖东西
    SELL("sell", 10122),
    //私聊玩家
    WHISPER("whisper", 1013),
    //全服喊话
    SPEAK("speak", 1014),
    //收件箱
    LIST_EMAIL("list_email", 1015),
    //查看邮件
    CHECK_EMAIL("check_email", 10151),
    //发送邮件
    SEND_EMAIL("send_email", 10152),
    //收取礼物
    RECEIVE_EMAIL("receive", 10153),
    //查看已有小队
    LIST_TEAM("list_team", 1016),
    //创建小队
    CREATE_TEAM("create_team", 10160),
    //查看自己所在小队
    CHECK_TEAM("check_team", 10161),
    //邀请加入小队
    INVITE_PLAYER("invite_player", 10162),
    //申请加入小队
    APPLY_TEAM("apply_team", 10163),
    //同意申请
    AGREE_APPLY("agree_apply", 10164),
    //接受组队邀请
    ACCEPT_TEAM("accept_team", 10165),
    //退出小队
    LEAVE_TEAM("leave_team", 10166),
    //查看人物状态属性
    PLAYER_STATE("player_state", 888),
    //查看已穿戴装备
    EQUIP_LIST("equip_list", 999),
    LOG_OUT("log_out", 666),
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
