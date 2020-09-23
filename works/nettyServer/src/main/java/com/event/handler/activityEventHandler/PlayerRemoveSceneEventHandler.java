package com.event.handler.activityEventHandler;

import com.event.EventHandler;
import com.event.EventManager;
import com.event.model.playerEvent.RemoveSceneEvent;
import com.function.player.model.Player;
import com.function.player.model.SceneObjectTask;
import com.function.scene.model.SceneObject;
import com.function.scene.model.SceneObjectType;
import com.function.summon.model.Summon;
import com.function.trade.manager.TradeManager;
import com.function.trade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catherine
 * @create 2020-09-20 17:48
 */
@Component
public class PlayerRemoveSceneEventHandler implements EventHandler<RemoveSceneEvent> {
    @Autowired
    private TradeManager tradeManager;
    @Autowired
    private TradeService tradeService;

    {
        EventManager.putEvent(RemoveSceneEvent.class, this);
    }

    @Override
    public void handle(RemoveSceneEvent event) {
        Player player = event.getPlayer();
        if (!Objects.isNull(player.getTradeBoard())) {
            tradeService.cancelTrade(player.getTradeBoard());
            return;
        }
        long playerId = player.getTPlayer().getRoleId();
        if (tradeManager.getTradeBoardMap().containsKey(playerId)) {
            tradeService.cancelTrade(tradeManager.getTradeBoardMap().get(playerId));
        }

        Map<Long, SceneObject> summonMap = event.getOldScene().getSceneObjectMap().get(SceneObjectType.SUMMON);
        Summon summon = (Summon) summonMap.get(playerId);
        if (summon != null) {
            ScheduledFuture<?> attackTask = summon.getTaskMap().get(SceneObjectTask.ATTACK);
            if (attackTask != null) {
                attackTask.cancel(true);
                summon.getTaskMap().remove(SceneObjectTask.ATTACK);
            }
            summonMap.remove(playerId);
            summon.setNowScene(player.getNowScene());
            player.getNowScene().getSceneObjectMap().get(SceneObjectType.SUMMON).put(playerId, summon);
        }
    }
}
