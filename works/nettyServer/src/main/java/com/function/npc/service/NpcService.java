package com.function.npc.service;

import com.function.npc.excel.NpcExcel;
import com.function.player.model.Player;
import com.function.scene.service.NotifyScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Catherine
 */
@Service
public class NpcService {
    @Autowired
    private NotifyScene notifyScene;

    public void talkToNpc(Player player, int npcId) {
        NpcExcel npc = player.getNowScene().getSceneExcel().getNpcs().get(npcId);
        if (npc != null) {
            StringBuilder talk = new StringBuilder(npc.getName()).append("对你说").append(npc.getText()).append('\n');
            notifyScene.notifyPlayer(player, talk);
        } else {
            StringBuilder noNpc = new StringBuilder("目标指向错误！\n");
        }
    }
}
