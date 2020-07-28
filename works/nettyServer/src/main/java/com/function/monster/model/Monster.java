package com.function.monster.model;

import lombok.Data;

@Data
public class Monster {
    private int id;

    private String name;

    private int hp;

    private int mp;

    private int aggr;

    private int skill;

    private int reviveTime;

    private int drop;

    private int status;
}
