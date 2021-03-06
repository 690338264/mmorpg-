package com.function.trade.manager;

import com.function.trade.model.TradeBoard;
import com.function.trade.service.TradeService;
import com.manager.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catherine
 * @create 2020-09-04 12:44
 */
@Component
public class TradeManager {
    @Autowired
    private TradeService tradeService;
    /**
     * 交易接收者的id为key
     */
    private final Map<Long, TradeBoard> tradeBoardMap = new ConcurrentHashMap<>();

    private static final long TRADE_LAST = 300000;

    private static final long JUMP = 5000;

    public Map<Long, TradeBoard> getTradeBoardMap() {
        return tradeBoardMap;
    }

    @PostConstruct
    private void check() {
        ThreadPoolManager.loopThread(
                () -> tradeBoardMap.forEach((playerId, trade) -> {
                    long lastTime = System.currentTimeMillis() - trade.getStartTime();
                    if (lastTime > TRADE_LAST) {
                        if (trade.getIsTrading().compareAndSet(false, true)) {
                            tradeService.cancelTrade(trade);
                        }
                    }
                }), 0, JUMP, getClass().hashCode()
        );
    }
}
