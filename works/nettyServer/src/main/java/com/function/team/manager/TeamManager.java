package com.function.team.manager;

import com.function.team.model.Team;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catherine
 * @create 2020-08-24 15:04
 */
@Component
public class TeamManager {

    private Map<Long, Team> teamCache = new ConcurrentHashMap<>();

    public Map<Long, Team> getTeamCache() {
        return teamCache;
    }
}
