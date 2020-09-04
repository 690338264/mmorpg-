package com.function.trade.manager;

import com.function.trade.model.TradeBoard;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catherine
 * @create 2020-09-04 12:44
 */
@Component
public class TradeManager {
    /**
     * 交易接收者的id为key
     */
    private Map<Long, TradeBoard> tradeBoardMap = new ConcurrentHashMap<>();

    public Map<Long, TradeBoard> getTradeBoardMap() {
        return tradeBoardMap;
    }


}
