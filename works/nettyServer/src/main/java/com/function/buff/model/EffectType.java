package com.function.buff.model;

/**
 * @author Catherine
 * @create 2020-09-14 21:42
 */
public enum EffectType {
    //造成伤害
    DAMAGE(1),
    //血量buff
    HP_BUFF(2),
    //攻击力buff
    ATK_BUFF(3),
    //眩晕
    VERTIGO(4),
    //
    SUMMON(5),
    ;
    int type;

    EffectType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
