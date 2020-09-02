package com.function.email.service;

import com.alibaba.fastjson.JSON;
import com.function.email.model.Email;
import com.function.email.model.EmailState;
import com.function.item.model.Item;
import com.function.item.model.ItemType;
import com.function.item.service.ItemService;
import com.function.player.model.Player;
import com.function.scene.service.NotifyScene;
import com.function.user.map.UserMap;
import com.jpa.dao.EmailDAO;
import com.jpa.dao.PlayerDAO;
import com.jpa.entity.TEmail;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Catherine
 * @create 2020-09-02 15:45
 */
@Component
public class EmailService {
    @Autowired
    private ItemService itemService;
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private UserMap userMap;
    @Autowired
    private PlayerDAO playerDAO;
    @Autowired
    private EmailDAO emailDAO;

    public Email createEmail(Player player, Long playerId, String text) {
        Email email = new Email();
        TEmail tEmail = email.gettEmail();
        tEmail.setState(EmailState.UNREAD.getType());
        tEmail.setPlayerId(playerId);
        tEmail.setSender(player.getTPlayer().getRoleId());
        tEmail.setText(text);
        return email;
    }

    public void toReceiver(Long playerId, Email email) {
        Player player = findPlayer(playerId);
        if (player != null) {
            player.getEmails().add(email);
            notifyScene.notifyPlayer(player, "您收到一封邮件，请及时查收\n");
        } else {
            email.gettEmail().setGift(JSON.toJSONString(email.getGifts()));
            ThreadPoolManager.immediateThread(() -> emailDAO.save(email.gettEmail()), playerId.intValue());
        }
    }

    /**
     * 发送邮件仅文字
     */
    public void sendOnlyText(Player player, Long playerId, String text) {
        Email email = createEmail(player, playerId, text);
        toReceiver(playerId, email);
    }

    /***/
    public void sendHasGift(Player player, Long playerId, String text, List<Integer> indexs, List<Integer> num) {
        Email email = createEmail(player, playerId, text);
        IntStream.range(0, indexs.size()).forEach((i) -> {
            Item item = player.getBag().getItemMap().get(indexs.get(i));
            if (!itemService.removeItem(item.getItemId(), indexs.get(i), num.get(i), player)) {
                return;
            }
            if (item.getItemById().getType() == ItemType.MEDICINAL.getType()) {
                email.getGifts().add(itemService.copyItem(item, num.get(i)));
            } else {
                email.getGifts().add(item);
            }
        });
        toReceiver(playerId, email);
    }

//    /**
//     * 发送邮件
//     */
//    public void sendEmail(Player player, String text, Long playerId, String[] items, String[] num) {
//        Emails email = new Emails(player, text);
//        email.setText(text);
//        //有礼物送出
//        if (Integer.parseInt(items[0]) != -1) {
//            IntStream.range(0, items.length).forEach(i -> {
//                int index = Integer.parseInt(items[i]);
//                int n = Integer.parseInt(num[i]);
//                Item item = player.getBag().getItemMap().get(index);
//                //将物品从背包移除
//                if (!itemService.removeItem(item.getItemId(), index, n, player)) {
//                    return;
//                }
//                //药物
//                if (item.getItemById().getType() == 1) {
//                    Item gift = new Item();
//                    gift.setId(item.getId());
//                    gift.setNum(n);
//                    email.getGifts().put(i, gift);
//                } else {
//                    //装备
//                    email.getGifts().put(i, item);
//                }
//            });
//        }
//        //玩家被加载
//        if (findPlayer(playerId).getClass() == Player.class) {
//            Player receiver = findPlayer(playerId);
//            int emailId = receiver.getEmailMap().size();
//            receiver.getEmailMap().put(emailId, email);
//            if (player.getChannelHandlerContext() != null) {
//                notifyScene.notifyPlayer(receiver, "您有一封新邮件 请查收\n");
//            }
//        } else {
//            //玩家未被加载
//            TPlayer receiver = findPlayer(playerId);
//            String emails = receiver.getEmail();
//            if (email == null) {
//                emails = "{}";
//            }
//            Map<Integer, Emails> m = JSON.parseObject(emails, new TypeReference<Map<Integer, Emails>>() {
//            });
//            m.put(m.size(), email);
//            String json = JSON.toJSONString(m);
//            player.getTPlayer().setEmail(json);
//            playerDAO.save(player.getTPlayer());
//        }
//
//    }

    /**
     * 查看邮件
     */
    public void showEmail(Player player) {
        String e = "收件箱：\n";
        IntStream.range(0, player.getEmails().size()).forEach((index) -> {
            Email email = player.getEmails().get(index);
            String state;
            if (email.gettEmail().getState() == EmailState.READ.getType()) {
                state = EmailState.READ.getOut();
            } else {
                state = EmailState.UNREAD.getOut();
            }
            Player sender = userMap.getPlayers(email.gettEmail().getPlayerId());
            String result = MessageFormat.format("{0}{1}来自{2}的邮件\n",
                    index, state, sender.getTPlayer().getName());

            notifyScene.notifyPlayer(player, e + result);
        });
    }

    /**
     * 查看具体邮件
     */
    public void showDetail(Player player, int index) {
        Email email = player.getEmails().get(index);
        Player sender = userMap.getPlayers(email.gettEmail().getPlayerId());
        notifyScene.notifyPlayer(player, MessageFormat.format("来自  {0}:\n{1}\n礼物:\n",
                sender.getTPlayer().getName(), email.gettEmail().getText()));
        email.getGifts().forEach((gift) ->
                notifyScene.notifyPlayer(player, MessageFormat.format("{0}[1]\n", gift.getItemById().getName(), gift.getNum())));
        email.gettEmail().setState(EmailState.READ.getType());
    }

    /**
     * 收取礼物
     */
    public void receive(Player player, int index) {
        Email email = player.getEmails().get(index);
        Iterator<Item> iterator = email.getGifts().iterator();
        while (iterator.hasNext()) {
            Item gift = iterator.next();
            IntStream.range(0, gift.getNum()).forEach(i -> {
                if (!itemService.getItem(gift, player)) {
                    return;
                }
                iterator.remove();
            });
        }
    }

    public Player findPlayer(Long playerId) {
        return userMap.getPlayers(playerId);

    }
}
