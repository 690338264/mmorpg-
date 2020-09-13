package com.function.quest.controller;

import com.Cmd;
import com.function.player.model.Player;
import com.function.quest.service.QuestService;
import com.handler.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.Msg;
import util.ParamNumCheck;

/**
 * @author Catherine
 * @create 2020-09-13 16:20
 */
@Component
public class QuestController {
    @Autowired
    private QuestService questService;

    {
        ControllerManager.add(Cmd.LIST_QUEST, this::listQuest);
        ControllerManager.add(Cmd.ACCEPT_QUEST, this::acceptQuest);
        ControllerManager.add(Cmd.GIVE_UP_QUEST, this::giveUpQuest);
        ControllerManager.add(Cmd.COMMIT_QUEST, this::commitQuest);
    }

    private void listQuest(Player player, Msg msg) {
        questService.showQuest(player);
    }

    private void acceptQuest(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int questId = Integer.parseInt(params[1]);
        questService.acceptQuest(player, questId);
    }

    private void giveUpQuest(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int questId = Integer.parseInt(params[1]);
        questService.giveUpQuest(player, questId);
    }

    private void commitQuest(Player player, Msg msg) {
        String[] params = ParamNumCheck.numCheck(player, msg, 2);
        int questId = Integer.parseInt(params[1]);
        questService.commitQuest(player, questId);
    }
}
