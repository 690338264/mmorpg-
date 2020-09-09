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
    //角色创建
    PLAYER_CREATE("player_create", 1003),
    //角色登录
    PLAYER_LOG("player_log", 1004),
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
    //使用物品
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
    //查看副本列表
    LIST_INSTANCE("list_instance", 1017),
    //申请个人副本
    PERSONAL_INSTANCE("apply_personal", 10170),
    //申请小队副本
    TEAM_INSTANCE("team_instance", 10171),
    //进入副本
    INTO_INSTANCE("into_instance", 10172),
    //创建交易
    CREATE_TRADE("create_trade", 10180),
    //同意交易
    AGREE_TRADE("agree_trade", 10181),
    //提交物品
    PUT_CHANGE("put_change", 10182),
    //提交金币
    PUT_MONEY("put_money", 10183),
    //查看交易面板
    LIST_TRADE("list_trade", 10184),
    //提交交易
    COMMIT_TRADE("commit_trade", 10185),
    //取消交易
    CANCEL_TRADE("cancel_trade", 10186),
    //攻击玩家
    PVP("pvp", 1019),
    //查看自己所在工会
    CHECK_SECT("check_sect", 1020),
    //建立公会
    CREATE_SECT("create_sect", 10200),
    //申请加入公会
    REQUEST_JOIN("request", 10201),
    //接受申请
    ACCEPT_SECT("accept", 10202),
    //拒绝申请
    REJECT_SECT("reject", 10203),
    //从公会仓库拿东西
    GET_WAREHOUSE("get_warehouse", 10204),
    //向公会仓库捐献东西
    PUT_WAREHOUSE("put_warehouse", 10205),
    //更改职位
    SET_POSITION("set_position", 10206),
    //退出公会
    QUIT_SECT("quit_sect", 10207),
    //列出所有工会
    LIST_SECT("list_sect", 10208),
    //列出一口价拍卖
    LIST_FIXED("list_fixed", 10210),
    //列出竞价拍卖
    LIST_COMPETITION("list_competition", 10211),
    //创建拍卖
    CREATE_AUCTION("create_auction", 10212),
    //购买一口价
    BUY_FIXED("buy_fixed", 10213),
    //参与竞拍
    JOIN_COMPETITION("buy_competition", 10214),
    //好友列表
    LIST_FRIEND("list_friend", 10220),
    //添加好友
    ADD_FRIEND("add_friend", 10221),
    //接受好友申请
    ACCEPT_FRIEND("accept_friend", 10222),
    //拒绝好友申请
    REFUSE_FRIEND("refuse_friend", 10223),
    //查看人物状态属性
    PLAYER_STATE("player_state", 8888),

    //查看已穿戴装备
    EQUIP_LIST("equip_list", 7777),

    LOG_OUT("log_out", 6666),

    UNKNOWN("unknown", 9999);

    private final String cmd;
    private final Integer cmdId;

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
