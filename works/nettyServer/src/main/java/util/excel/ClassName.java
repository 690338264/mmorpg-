package util.excel;

/**
 * @author Catherine
 * @create 2020-09-02 10:12
 */
public enum ClassName {
    //表格加载
    Buff(1),
    Item(2),
    Npc(3),
    Occ(4),
    Scene(5),
    Skill(6),
    ;
    Integer type;

    ClassName(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
