package com.function.skill.manager;

import com.function.buff.excel.BuffResource;
import com.function.buff.model.Buff;
import com.function.skill.excel.SkillExcel;
import com.function.skill.model.Skill;
import org.springframework.stereotype.Component;

/**
 * @author Catherine
 * @create 2020-09-04 10:50
 */
@Component
public class SkillManager {
    public Skill initSkill(SkillExcel skillExcel) {
        Skill skill = new Skill();
        skill.setSkillId(skillExcel.getId());
        skillExcel.getBuffId().forEach(buffId -> {
            Buff buff = new Buff();
            buff.setId(BuffResource.getBuffById(buffId).getId());
            skill.getBuffList().add(buff);
        });
        return skill;
    }
}
