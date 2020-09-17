package com.function.npc.service;

import com.event.model.playerEvent.NpcTalkEvent;
import com.function.npc.excel.NpcExcel;
import com.function.player.model.Player;
import com.function.scene.model.SceneObjectType;
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

    public void talkToNpc(Player player, Long npcId) {
        NpcExcel npc = (NpcExcel) player.getNowScene().getSceneObjectMap().get(SceneObjectType.NPC).get(npcId);
        if (npc != null) {
            StringBuilder talk = new StringBuilder(npc.getName()).append("对你说").append(npc.getText()).append('\n');
            notifyScene.notifyPlayer(player, talk);
        } else {
            StringBuilder noNpc = new StringBuilder("目标指向错误！\n");
            notifyScene.notifyPlayer(player, noNpc);
        }
        player.asynchronousSubmitEvent(new NpcTalkEvent(npcId.intValue()));
    }
}
