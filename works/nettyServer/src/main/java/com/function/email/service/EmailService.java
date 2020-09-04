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

    /**
     * 发送邮件仅文字
     */
    public void sendOnlyText(Player player, Long playerId, String text) {
        Email email = createEmail(player, playerId, text);
        toReceiver(playerId, email);
    }

    /**
     * 发送礼物
     */
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
                notifyScene.notifyPlayer(player, MessageFormat.format("{0}{1}\n", gift.getItemById().getName(), gift.getNum())));
        email.gettEmail().setState(EmailState.READ.getType());
        updateEmail(email);
    }

    /**
     * 收取礼物
     */
    public void receive(Player player, int index) {
        Email email = player.getEmails().get(index);
        itemService.getItem(player, email.getGifts());
        updateEmail(email);
    }

    public Email createEmail(Player player, Long playerId, String text) {
        Email email = new Email();
        TEmail tEmail = new TEmail();
        tEmail.setState(EmailState.UNREAD.getType());
        tEmail.setPlayerId(playerId);
        tEmail.setSender(player.getTPlayer().getRoleId());
        tEmail.setText(text);
        email.settEmail(tEmail);
        return email;
    }

    public void toReceiver(Long playerId, Email email) {
        Player player = findPlayer(playerId);
        if (player != null) {
            player.getEmails().add(email);
            notifyScene.notifyPlayer(player, "您收到一封邮件，请及时查收\n");
            updateEmail(email);
        } else {
            email.gettEmail().setGift(JSON.toJSONString(email.getGifts()));
            ThreadPoolManager.immediateThread(() -> emailDAO.save(email.gettEmail()), playerId.intValue());
        }
    }

    public Player findPlayer(Long playerId) {
        return userMap.getPlayers(playerId);

    }

    public void updateEmail(Email email) {
        String json = JSON.toJSONString(email.getGifts());
        email.gettEmail().setGift(json);
        emailDAO.save(email.gettEmail());
    }
}
