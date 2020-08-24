package com.function.communicate.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.function.communicate.model.Email;
import com.function.item.model.Item;
import com.function.item.service.ItemService;
import com.function.player.model.Player;
import com.function.scene.manager.SceneMap;
import com.function.scene.service.NotifyScene;
import com.function.user.map.PlayerMap;
import com.function.user.map.UserMap;
import com.jpa.dao.PlayerDAO;
import com.jpa.entity.TPlayer;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author Catherine
 * @create 2020-08-20 17:32
 */
@Component
public class CommunicateService {
    @Autowired
    private PlayerMap playerMap;
    @Autowired
    private UserMap userMap;
    @Autowired
    private SceneMap sceneMap;
    @Autowired
    private PlayerDAO playerDAO;
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private ItemService itemService;

    /**
     * 玩家私聊
     */
    public void whisper(Player player, long playerId, String text) {
        ChannelHandlerContext ctxPlayer = playerMap.getCtxPlayer(playerId);
        if (ctxPlayer == null) {
            notifyScene.notifyPlayer(player, "玩家不在线或不存在！\n");
            return;
        }
        Player toTalk = playerMap.getPlayerCtx(ctxPlayer);

        notifyScene.notifyPlayer(toTalk, MessageFormat.format("{0}对你说：{1}\n", player.getTPlayer().getName(), text));
    }

    /**
     * 全服喊话
     */
    public void speak(Player player, String text) {
        String s = MessageFormat.format("{0}说：{1}\n", player.getTPlayer().getName(), text);
        for (int key : sceneMap.getSceneCache().keySet()) {
            notifyScene.notifyScene(sceneMap.getSceneCache().get(key), s);
        }

    }

    /**
     * 发送邮件
     */
    public void sendEmail(Player player, String text, Long playerId, String[] items, String[] num) {
        Email email = new Email(player, text);
        email.setText(text);
        //有礼物送出
        if (Integer.parseInt(items[0]) != -1) {
            IntStream.range(0, items.length).forEach(i -> {
                int index = Integer.parseInt(items[i]);
                int n = Integer.parseInt(num[i]);
                Item item = player.getBag().getItemMap().get(index);
                //将物品从背包移除
                if (!itemService.removeItem(index, n, player)) {
                    return;
                }
                //药物
                if (item.getItemById().getType() == 1) {
                    Item gift = new Item();
                    gift.setId(item.getId());
                    gift.setNum(n);
                    email.getGifts().put(i, gift);
                } else {
                    //装备
                    email.getGifts().put(i, item);
                }
            });
        }
        //玩家被加载
        if (findPlayer(playerId).getClass() == Player.class) {
            Player receiver = findPlayer(playerId);
            int emailId = receiver.getEmailMap().size();
            receiver.getEmailMap().put(emailId, email);
            if (player.getChannelHandlerContext() != null) {
                notifyScene.notifyPlayer(receiver, "您有一封新邮件 请查收\n");
            }
        } else {
            //玩家未被加载
            TPlayer receiver = findPlayer(playerId);
            String emails = receiver.getEmail();
            if (email == null) {
                emails = "{}";
            }
            Map<Integer, Email> m = JSON.parseObject(emails, new TypeReference<Map<Integer, Email>>() {
            });
            m.put(m.size(), email);
            String json = JSON.toJSONString(m);
            player.getTPlayer().setEmail(json);
            playerDAO.save(player.getTPlayer());
        }

    }

    /**
     * 查看邮件
     */
    public void showEmail(Player player) {
        String e = "收件箱：\n";
        player.getEmailMap().forEach((k, v) -> {
            Email email = player.getEmailMap().get(k);
            String state;
            if (email.getState() == 1) {
                state = "[已读]";
            } else {
                state = "[未读]";
            }
            String result = MessageFormat.format("{0}{1}{2}来自{3}的邮件\n",
                    k, state, email.getSender().getTPlayer().getName());

            notifyScene.notifyPlayer(player, result);
        });
    }

    /**
     * 查看具体邮件
     */
    public void showDetail(Player player, int emailId) {
        Email email = player.getEmailMap().get(emailId);
        StringBuilder content = new StringBuilder();
        content.append(email.getSender().getTPlayer().getName()).append(":\n").append(email.getText()).append('\n');
        if (email.getGifts() != null) {
            email.getGifts().forEach((k, v) -> {
                Item gift = email.getGifts().get(k);
                content.append(gift.getItemById().getName()).append("[").append(gift.getNum()).append("]\n");
            });
        }
        email.setState(1);
        notifyScene.notifyPlayer(player, content);
    }

    /**
     * 收取礼物
     */
    public void receive(Player player, int emailId) {
        Email email = player.getEmailMap().get(emailId);
        email.getGifts().forEach((k, v) -> {
            Item item = email.getGifts().get(k);
            IntStream.range(0, item.getNum()).forEach(i -> {
                if (!itemService.getItem(item, player)) {
                    return;
                }
                email.getGifts().remove(k);
            });
        });
    }

    public <T> T findPlayer(Long playerId) {
        if (userMap.getPlayers(playerId) != null) {
            return (T) userMap.getPlayers(playerId);
        }
        return (T) playerDAO.findByRoleId(playerId);
    }
}
