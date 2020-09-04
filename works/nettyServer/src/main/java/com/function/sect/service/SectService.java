package com.function.sect.service;

import com.function.player.model.Player;
import com.function.scene.service.NotifyScene;
import com.function.sect.manager.SectManager;
import com.function.sect.model.Sect;
import com.jpa.dao.SectDAO;
import com.jpa.entity.TSect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author Catherine
 * @create 2020-09-05 02:17
 */
@Component
public class SectService {
    @Autowired
    private NotifyScene notifyScene;
    @Autowired
    private SectDAO sectDAO;
    @Autowired
    private SectManager sectManager;

    public void createSect(Player player, String name) {
        if (player.getTPlayer().getSectId() == null) {
            notifyScene.notifyPlayer(player, "您已加入公会\n");
            return;
        }
        if (sectDAO.findByName(name) != null) {
            notifyScene.notifyPlayer(player, "创建失败！公会名重复\n");
            return;
        }
        TSect tSect = new TSect(name);
        sectDAO.save(tSect);
        tSect = sectDAO.findByName(name);
        Sect sect = new Sect();
        sect.settSect(tSect);
        sect.getMembers().add(player.getTPlayer().getRoleId());
        sectManager.getSectMap().put(tSect.getSectId(), sect);
        notifyScene.notifyPlayer(player, MessageFormat.format("创建工会成功,工会id:{0}\n", tSect.getSectId()));
    }

}
