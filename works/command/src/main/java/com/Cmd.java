package com;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 */

public enum Cmd {
    //用户注册
    USER_CREATE("用户注册", 1000),
    //用户登录
    USER_LOGIN("用户登录", 1001),
    //角色列表
    USER_LISTPLAYER("角色列表", 1002),
    //角色创建
    PLAYER_CREATE("角色创建", 1003),
    //角色登录
    PLAYER_LOG("角色登录", 1004),
    //查看场景
    AOI("查看场景", 1005),
    //和NPC谈话
    TALK_TO("和Npc谈话", 1006),
    //查看相邻地图
    WHERE_CAN_GO("查看相邻地图", 1007),
    //转移场景
    MOVE_TO("转移场景", 1008),
    //攻击
    ATTACK("攻击", 1009),
    //查看背包
    BAG_LIST("查看背包", 1010),
    //整理背包
    BAG_INORDER("整理背包", 1011),
    //使用物品
    ITEM_USE("使用物品", 10102),
    //丢弃物品
    ITEM_DROP("丢弃物品", 10103),
    //拖下装备
    EQUIP_OFF("脱下装备", 10104),
    //修理装备
    EQUIP_FIX("修理装备", 10105),
    //查看商店系统
    SHOW_SHOP("查看商店", 1012),
    //买东西
    BUY("买东西", 10121),
    //卖东西
    SELL("卖东西", 10122),
    //私聊玩家
    WHISPER("私聊玩家", 1013),
    //全服喊话
    SPEAK("全服喊话", 1014),
    //收件箱
    LIST_EMAIL("收件箱", 1015),
    //查看邮件
    CHECK_EMAIL("查看邮件", 10151),
    //发送邮件
    SEND_EMAIL("发送邮件", 10152),
    //收取礼物
    RECEIVE_EMAIL("收取礼物", 10153),
    //查看已有小队
    LIST_TEAM("查看已有小队", 1016),
    //创建小队
    CREATE_TEAM("创建小队", 10160),
    //查看自己所在小队
    CHECK_TEAM("查看自己所在小队", 10161),
    //邀请加入小队
    INVITE_PLAYER("邀请加入小队", 10162),
    //申请加入小队
    APPLY_TEAM("申请加入小队", 10163),
    //同意申请
    AGREE_APPLY("同意申请", 10164),
    //接受组队邀请
    ACCEPT_TEAM("接受组队邀请", 10165),
    //退出小队
    LEAVE_TEAM("退出小队", 10166),
    //查看副本列表
    LIST_DUNGEON("查看副本列表", 1017),
    //申请个人副本
    PERSONAL_DUNGEON("申请个人副本", 10170),
    //申请小队副本
    TEAM_DUNGEON("申请小队副本", 10171),
    //进入副本
    INTO_DUNGEON("进入副本", 10172),
    //创建交易
    CREATE_TRADE("创建交易", 10180),
    //同意交易
    AGREE_TRADE("同意交易", 10181),
    //提交物品
    PUT_CHANGE("提交物品", 10182),
    //提交金币
    PUT_MONEY("提交金币", 10183),
    //查看交易面板
    LIST_TRADE("查看交易面板", 10184),
    //提交交易
    COMMIT_TRADE("提交交易", 10185),
    //取消交易
    CANCEL_TRADE("取消交易", 10186),
    //攻击玩家
    PVP("pvp", 1019),
    //查看自己所在工会
    CHECK_SECT("查看自己所在公会", 1020),
    //建立公会
    CREATE_SECT("建立公会", 10200),
    //申请加入公会
    REQUEST_JOIN("申请加入公会", 10201),
    //接受申请
    ACCEPT_SECT("接受申请", 10202),
    //拒绝申请
    REJECT_SECT("拒绝申请", 10203),
    //从公会仓库拿东西
    GET_WAREHOUSE("从公会仓库拿东西", 10204),
    //向公会仓库捐献东西
    PUT_WAREHOUSE("向公会仓库捐东西", 10205),
    //更改职位
    SET_POSITION("更改职位", 10206),
    //退出公会
    QUIT_SECT("退出公会", 10207),
    //列出所有工会
    LIST_SECT("列出所有工会", 10208),
    //列出一口价拍卖
    LIST_FIXED("列出一口价拍卖", 10210),
    //列出竞价拍卖
    LIST_COMPETITION("列出竞价拍卖", 10211),
    //创建拍卖
    CREATE_AUCTION("创建拍卖", 10212),
    //购买一口价
    BUY_FIXED("购买一口价", 10213),
    //参与竞拍
    JOIN_COMPETITION("参与竞拍", 10214),
    //好友列表
    LIST_FRIEND("好友列表", 10220),
    //添加好友
    ADD_FRIEND("添加好友", 10221),
    //接受好友申请
    ACCEPT_FRIEND("接受好友申请", 10222),
    //拒绝好友申请
    REFUSE_FRIEND("拒绝好友申请", 10223),
    //查看任务列表
    LIST_QUEST("查看任务列表", 10230),
    //接受任务
    ACCEPT_QUEST("接受任务", 10231),
    //放弃任务
    GIVE_UP_QUEST("放弃任务", 10232),
    //提交任务
    COMMIT_QUEST("提交任务", 10233),
    //查看人物状态属性
    PLAYER_STATE("查看人物状态", 8888),

    //查看已穿戴装备
    EQUIP_LIST("查看已穿戴的装备", 7777),

    LOG_OUT("退出登录", 6666),

    UNKNOWN("unknown", 9999);

    private final String cmd;
    private final Integer cmdId;

    private static final Map<Integer, Cmd> ID_MAP = new HashMap<>();
    private static final Map<String, Cmd> COMMAND_MAP = new HashMap<>();

    Cmd(String cmd, Integer cmdId) {
        this.cmd = cmd;
        this.cmdId = cmdId;
    }

    //将字符串命令与枚举对象通过map关联
    static {
        for (Cmd e : EnumSet.allOf(Cmd.class)) {
            COMMAND_MAP.put(e.cmd, e);
            ID_MAP.put(e.cmdId, e);
        }
    }

    public static Cmd find(int cmdId, Cmd defaultValue) {
        Cmd value = ID_MAP.get(cmdId);
        if (value == null) {
            return defaultValue;
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
