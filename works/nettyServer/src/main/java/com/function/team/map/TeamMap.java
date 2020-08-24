package com.function.team.map;

import com.function.team.model.Team;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-24 15:04
 */
@Component
public class TeamMap {

    private Map<Integer, Team> teamCache = new HashMap<>();

    public Map<Integer, Team> getTeamCache() {
        return teamCache;
    }
}
