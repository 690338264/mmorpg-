package com.function.monster.excel;

import com.function.item.excel.ItemResource;
import com.function.skill.model.Skill;
import com.manager.ExcelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.excel.ClassName;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author Catherine
 */
@Component
public class MonsterResource {
    @Autowired
    private ExcelManager excelManager;

    private static final Map<Integer, MonsterExcel> monsterMap = new HashMap<>();

    @PostConstruct
    private void init() {
        int num = excelManager.getMap().get(ClassName.Monster.name()).size();
        for (int i = 0; i < num; i++) {
            MonsterExcel monster = (MonsterExcel) excelManager.getMap().get(ClassName.Monster.name()).get(i);
            monsterMap.put(monster.getId(), monster);
            String str = monster.getSkill();
            String[] strs = str.split(",");
            String[] drops = monster.getDrop().split(",");
            for (int j = 0; j < strs.length; j++) {
                int skillId = Integer.parseInt(strs[j]);
                Skill skill = new Skill(skillId);
                monster.getMonsterSkill().put(j, skill);
            }
            IntStream.range(0, drops.length).forEach(j
                    -> monster.getItemList().add(ItemResource.getItemById(Integer.parseInt(drops[j]))));

        }
    }

    public static MonsterExcel getMonById(int id) {
        return monsterMap.get(id);
    }
}
