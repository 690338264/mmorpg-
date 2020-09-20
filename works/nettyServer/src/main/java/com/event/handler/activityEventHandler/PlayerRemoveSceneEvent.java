package com.event.handler.activityEventHandler;

import com.event.EventHandler;
import com.event.EventManager;
import com.event.model.playerEvent.RemoveSceneEvent;
import com.function.player.model.Player;
import com.function.trade.manager.TradeManager;
import com.function.trade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Catherine
 * @create 2020-09-20 17:48
 */
@Component
public class PlayerRemoveSceneEvent implements EventHandler<RemoveSceneEvent> {
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
    }
}
