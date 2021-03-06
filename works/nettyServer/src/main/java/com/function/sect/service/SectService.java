package com.function.sect.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.event.model.playerEvent.SectAddEvent;
import com.function.item.model.Item;
import com.function.item.model.ItemType;
import com.function.item.service.ItemService;
import com.function.occ.excel.OccResource;
import com.function.player.manager.PlayerManager;
import com.function.player.model.Player;
import com.function.player.model.PlayerInfo;
import com.function.player.service.PlayerData;
import com.function.quest.model.Quest;
import com.function.quest.model.QuestState;
import com.function.quest.model.QuestType;
import com.function.scene.service.NotifyScene;
import com.function.sect.manager.SectManager;
import com.function.sect.model.Sect;
import com.function.sect.model.SectPosition;
import com.function.user.map.PlayerMap;
import com.function.user.map.UserMap;
import com.jpa.dao.PlayerDAO;
import com.jpa.entity.TPlayer;
import com.jpa.entity.TPlayerInfo;
import com.jpa.entity.TSect;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Catherine
 * @create 2020-09-05 02:17
 */
@Component
public class SectService {
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private SectManager sectManager;
    @Autowired
    private UserMap userMap;
    @Autowired
    private PlayerDAO playerDAO;
    @Autowired
    private ItemService itemService;
    @Autowired
    private PlayerManager playerManager;
    @Autowired
    private PlayerData playerData;
    @Autowired
    private PlayerMap playerMap;

    private static final int MAX_SIZE = 60;

    public static AtomicLong incSectId = new AtomicLong();


    /**
     * 所有工会
     */
    public void listSect(Player player) {
        StringBuilder content = new StringBuilder();
        sectManager.getSectMap().forEach((id, sect) ->
                content.append(MessageFormat.format("公会id：{0}名称:{1}人数:{2}\n",
                        id, sect.gettSect().getName(), sect.getMembers().size()))
        );
        notifyScene.notifyPlayer(player, content);
    }

    /**
     * 工会创建
     */
    public void createSect(Player player, String name) {
        if (player.getTPlayer().getSectId() != null) {
            notifyScene.notifyPlayer(player, "您已加入公会\n");
            return;
        }
        if (sectManager.getSectNameSet().contains(name)) {
            notifyScene.notifyPlayer(player, "会名重复\n");
            return;
        }
        TSect tSect = new TSect(name);
        Sect newSect = new Sect(tSect);
        synchronized (this) {
            if (sectManager.getSectNameSet().contains(name)) {
                notifyScene.notifyPlayer(player, "会名重复\n");
                return;
            }
            sectManager.getSectNameSet().add(name);
        }
        sectManager.getSectMap().put(incSectId.incrementAndGet(), newSect);
        //公会数据持久化
        newSect.getMembers().add(player.getTPlayer().getRoleId());
        tSect.setSectId(incSectId.longValue());
        sectManager.getSectMap().put(tSect.getSectId(), newSect);
        sectManager.updateSect(newSect);
        //人物数据持久化
        player.getTPlayer().setSectId(tSect.getSectId());
        player.getTPlayer().setSectPosition(SectPosition.PRESIDENT.getType());
        PlayerInfo playerInfo = playerManager.getPlayerInfoMap().get(player.getTPlayer().getRoleId());
        playerInfo.gettPlayerInfo().setSectPosition(SectPosition.PRESIDENT.getType());
        playerData.updatePlayer(player);
        playerData.updatePlayerInfo(playerInfo);
        notifyScene.notifyPlayer(player, MessageFormat.format("创建公会成功,公会id:{0}\n", tSect.getSectId()));
        player.asynchronousSubmitEvent(new SectAddEvent());
    }

    /**
     * 申请加入公会
     */
    public void joinRequest(Player player, Long sectId) {
        if (player.getTPlayer().getSectId() != null) {
            notifyScene.notifyPlayer(player, "您已加入公会\n");
            return;
        }
        Sect sect = sectManager.getSectMap().get(sectId);
        if (sect == null) {
            notifyScene.notifyPlayer(player, "输入公会号有误\n");
            return;
        }

        sect.getJoinRequest().add(player.getTPlayer().getRoleId());
        sectManager.updateSect(sect);
        notifyScene.notifyPlayer(player, "申请成功\n");
    }

    /**
     * 查看自己所在工会
     */
    public void checkSect(Player player) {
        Sect sect = getSect(player);
        if (sect == null) {
            return;
        }
        notifyScene.notifyPlayer(player, MessageFormat.format("公会id:{0}  公会名称:{1}  公会人数:{2}\n成员列表:\n",
                sect.gettSect().getSectId(), sect.gettSect().getName(), sect.getMembers().size()));
        sect.getMembers().forEach((playerId) -> {
            TPlayerInfo memberInfo = playerManager.getPlayerInfoMap().get(playerId).gettPlayerInfo();
            notifyScene.notifyPlayer(player, MessageFormat.format("{0}职位[{1}]姓名:{2},门派:{3}\n",
                    playerId, SectPosition.values()[memberInfo.getSectPosition() - 1].getRole(),
                    memberInfo.getName(), OccResource.getOccById(memberInfo.getOccupation()).getName()));
        });
        notifyScene.notifyPlayer(player, "申请列表:\n");
        sect.getJoinRequest().forEach((playerId) -> {
            TPlayerInfo memberInfo = playerManager.getPlayerInfoMap().get(playerId).gettPlayerInfo();
            notifyScene.notifyPlayer(player, MessageFormat.format("{0}姓名:{1},门派:{2}\n",
                    playerId, memberInfo.getName(), OccResource.getOccById(memberInfo.getOccupation()).getName()));
        });
        notifyScene.notifyPlayer(player, "公会仓库:\n");
        sect.getWareHouse().forEach((index, item) -> notifyScene.notifyPlayer(player,
                MessageFormat.format("{0}:{1}[{2}]\n",
                        index, item.getItemById().getName(), item.getNum())));
    }

    /**
     * 同意入会申请
     */
    public void agreeJoin(Player player, Long playerId) {
        Sect sect = getSect(player);
        if (sect == null) {
            return;
        }
        if (player.getTPlayer().getSectPosition() > SectPosition.VICE_PRESIDENT.getType()) {
            notifyScene.notifyPlayer(player, "您无该权限\n");
            return;
        }
        if (!sect.getJoinRequest().contains(playerId)) {
            notifyScene.notifyPlayer(player, "无该玩家申请\n");
            return;
        }
        if (userMap.getPlayers(playerId) != null) {
            Player request = userMap.getPlayers(playerId);
            if (request.getTPlayer().getSectId() != null) {
                notifyScene.notifyPlayer(player, "玩家已加入其他公会\n");
                return;
            }
            request.getTPlayer().setSectId(sect.gettSect().getSectId());
            request.getTPlayer().setSectPosition(SectPosition.NORMAL_MEMBER.getType());
            playerData.updatePlayer(request);
            notifyScene.notifyPlayer(request, "加入公会\n");
            request.asynchronousSubmitEvent(new SectAddEvent());
        } else {
            playerUnLine(sect, playerId);
        }
        //更新工会信息
        sect.getJoinRequest().remove(playerId);
        sect.getMembers().add(playerId);
        sectManager.updateSect(sect);
        //更新人物信息  刚入会默认普通会员
        PlayerInfo playerInfo = playerManager.getPlayerInfoMap().get(playerId);
        playerInfo.gettPlayerInfo().setSectPosition(SectPosition.NORMAL_MEMBER.getType());
        playerData.updatePlayerInfo(playerInfo);
        notifyScene.notifyPlayer(player, "同意成功\n");
    }

    /**
     * 拒绝申请
     */
    public void rejectJoin(Player player, Long playerId) {
        Sect sect = getSect(player);
        if (sect == null) {
            return;
        }
        if (player.getTPlayer().getSectPosition() > SectPosition.VICE_PRESIDENT.getType()) {
            notifyScene.notifyPlayer(player, "您无该权限\n");
            return;
        }
        sect.getJoinRequest().remove(playerId);
        sectManager.updateSect(sect);
    }

    /**
     * 捐献物品
     */
    public void donateItem(Player player, int index, int num) {
        Sect sect = getSect(player);
        if (sect == null) {
            return;
        }
        Item item = player.getBag().getItemMap().get(index);
        if (!itemService.removeItem(item.getItemId(), index, num, player)) {
            return;
        }
        if (item.getItemById().getType() == ItemType.MEDICINAL.getType()) {
            item = itemService.copyItem(item, num);
        }
        Item copyItem = item;
        ThreadPoolManager.immediateThread(() -> {
            if (putItem(copyItem, sect)) {
                sectManager.updateSect(sect);
                notifyScene.notifyPlayer(player, "捐献成功！\n");
            } else {
                notifyScene.notifyPlayer(player, "公会仓库已满\n");
                itemService.getItem(copyItem, player);
            }
        }, sect.gettSect().getSectId().intValue());

    }

    /**
     * 退出公会
     */
    public void quitSect(Player player) {
        Sect sect = getSect(player);
        if (sect == null) {
            return;
        }
        //会长要先转让公会
        if (player.getTPlayer().getSectPosition() == SectPosition.PRESIDENT.getType()) {
            notifyScene.notifyPlayer(player, "你是会长还不能退出\n");
            return;
        }
        player.getTPlayer().setSectId(null);
        sect.getMembers().remove(player.getTPlayer().getRoleId());
        sectManager.updateSect(sect);
        playerData.updatePlayer(player);
        notifyScene.notifyPlayer(player, "退出成功\n");
    }

    /**
     * 设置职位
     */
    public void setPosition(Player player, Long playerId, int position) {
        Sect sect = sectManager.getSectMap().get(player.getTPlayer().getSectId());
        if (!sect.getMembers().contains(playerId)) {
            notifyScene.notifyPlayer(player, "没有该成员\n");
            return;
        }
        if (player.getTPlayer().getSectPosition() > position) {
            notifyScene.notifyPlayer(player, "没有该权利\n");
            return;
        }
        //只能有一个会长
        if (position == SectPosition.PRESIDENT.getType()) {
            player.getTPlayer().setSectPosition(SectPosition.VICE_PRESIDENT.getType());
            playerData.updatePlayer(player);
            PlayerInfo playerInfo = playerManager.getPlayerInfoMap().get(player.getTPlayer().getRoleId());
            playerInfo.gettPlayerInfo().setSectPosition(SectPosition.VICE_PRESIDENT.getType());
            playerData.updatePlayerInfo(playerInfo);
        }
        //对被更改的成员信息进行持久化
        TPlayer member = userMap.getPlayers().containsKey(playerId) ? userMap.getPlayers(playerId).getTPlayer() : playerDAO.findByRoleId(playerId);
        PlayerInfo playerInfo = playerManager.getPlayerInfoMap().get(playerId);
        playerInfo.gettPlayerInfo().setSectPosition(position);
        member.setSectPosition(position);
        playerDAO.save(member);
        playerData.updatePlayerInfo(playerInfo);
        notifyScene.notifyPlayer(player, "更改职位成功\n");
    }

    /**
     * 从公会仓库取出物品
     */
    public void getFromSect(Player player, int index, int num) {
        Sect sect = getSect(player);
        if (sect == null) {
            return;
        }
        ThreadPoolManager.immediateThread(() -> {
            Item item = sect.getWareHouse().get(index);
            if (!removeItem(index, num, sect)) {
                notifyScene.notifyPlayer(player, "获取失败\n");
                return;
            }
            if (item.getItemById().getType() == ItemType.MEDICINAL.getType()) {
                item = itemService.copyItem(item, num);
            }
            if (!itemService.getItem(item, player)) {
                putItem(item, sect);
                notifyScene.notifyPlayer(player, "获取失败\n");
            }
        }, sect.gettSect().getSectId().intValue());
    }

    /**
     * 向公会背包格子中放入物品
     */
    public boolean putItem(Item item, Sect sect) {
        if (item.getNum() < 0) {
            return false;
        }
        if (item.getNum() == 0) {
            return true;
        }
        Map<Integer, Item> itemMap = sect.getWareHouse();
        for (int index = 0; index < MAX_SIZE; index++) {
            Item boxItem = itemMap.get(index);
            if (boxItem != null) {
                if ((item.getId() == boxItem.getId()) && boxItem.getNum() < boxItem.getItemById().getMaxNum()) {
                    int num = boxItem.getNum();
                    int max = item.getItemById().getMaxNum();
                    boxItem.setNum(Math.max(item.getNum() + num, max));
                    if (boxItem.getNum() == max) {
                        item.setNum(item.getNum() - max + num);
                        return putItem(item, sect);
                    }
                    return true;
                }
            } else {
                itemMap.put(index, item);
                return true;
            }
        }
        return false;
    }

    /**
     * 移除物品
     */
    public boolean removeItem(int index, int num, Sect sect) {
        Map<Integer, Item> itemMap = sect.getWareHouse();
        Item item = itemMap.get(index);
        if (item == null || item.getNum() < num || num < 0) {
            return false;
        }
        if (item.getNum() != num) {
            item.setNum(item.getNum() - num);
        } else {
            itemMap.remove(index);
        }
        return true;
    }

    /**
     * 获得玩家所在公会
     */
    public Sect getSect(Player player) {
        Long sectId = player.getTPlayer().getSectId();
        if (sectId == null) {
            notifyScene.notifyPlayer(player, "您还没有加入公会!\n");
            return null;
        }
        return sectManager.getSectMap().get(sectId);
    }

    public void playerUnLine(Sect sect, Long playerId) {
        ThreadPoolManager.immediateThread(() -> {
            TPlayer tPlayer = playerDAO.findByRoleId(playerId);
            Player player = new Player();
            player.setTPlayer(tPlayer);
            player.setQuestMap(JSON.parseObject(tPlayer.getQuest(), new TypeReference<Map<QuestState, List<Integer>>>() {
            }));
            player.setOnDoingQuest(JSON.parseObject(tPlayer.getOnDoingQuest(), new TypeReference<Map<QuestType, Map<Integer, Quest>>>() {
            }));
            player.asynchronousSubmitEvent(new SectAddEvent());
            tPlayer.setSectId(sect.gettSect().getSectId());
            tPlayer.setSectPosition(SectPosition.NORMAL_MEMBER.getType());
            userMap.getPlayers().put(playerId, player);
            playerMap.getPlayerLastUpdate().put(playerId, System.currentTimeMillis());
            playerData.updatePlayer(player);
        }, playerId.intValue());
    }
}

