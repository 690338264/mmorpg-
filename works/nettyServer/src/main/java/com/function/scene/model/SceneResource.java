package com.function.scene.model;

import com.function.monster.model.Monster;
import com.function.monster.model.MonsterResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.excel.ExcelUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SceneResource{

    private static Map<Integer,Scene> sceneMap = new HashMap<Integer, Scene>();

    @PostConstruct
    private void init(){
        File file = new File("C:\\Users\\Dell\\Desktop\\mmorpg-\\works\\nettyServer\\src\\main\\resources\\excels\\scene.xlsx");
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            List<Scene> list = ExcelUtils.readExcelToEntity(Scene.class,in,file.getName());
            for(int i = 0;i<list.size();i++) {
                sceneMap.put(list.get(i).getId(),list.get(i));
                String str = list.get(i).getMonster();
                String[] strs = str.split(",");
                for (int j = 0;j<strs.length;j++){
                    int monsterId = Integer.parseInt(strs[j]);
                    Monster monster = new Monster();
                    monster.setId(monsterId);
                    monster.setSceneId(j);
                    monster.setStatus(1);
                    monster.setSelfHp(monster.getMonsterExcel().getHp());
                    list.get(i).getMonsters().put(j, monster);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static Scene getSceneById(int id){
        return sceneMap.get(id);
    }
}
