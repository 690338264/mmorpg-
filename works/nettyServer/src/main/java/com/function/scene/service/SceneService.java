package com.function.scene.service;

import com.function.player.model.PlayerModel;
import com.function.scene.model.Scene;
import com.function.scene.model.SceneExcel;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import util.excel.FileRead;

import java.util.*;

@Service
public class SceneService {

    private Map<Integer,Scene> sceneMap;
    public Scene getSceneByPlayer(PlayerModel playerModel){
        playerModel.setLoc(playerModel.getNowScene().getId());
        return playerModel.getNowScene();
    }

    public void init(){
        String path = FileRead.getFilePath("excels/scene.xlsx");
        SceneExcel sceneExcelUtil = new SceneExcel(path);
        sceneMap = sceneExcelUtil.getMap();
    }
    public List<Scene> getNeighbor(PlayerModel playerModel){
//        Sheet s = sceneExcelUtil.getSheet();
//        int rowNum = s.getPhysicalNumberOfRows();
        init();
        Scene scene = getSceneByPlayer(playerModel);
        String sceneID = scene.getNeighbour();
        List<Scene> sceneList = new ArrayList<>();
        if(Objects.isNull(sceneID)||sceneID.isEmpty()){
            return sceneList;
        }
        String[] sid = sceneID.split(",");
        int num = sid.length;
        for (int i = 0;i<num;i++){
            Integer id = Integer.valueOf(sid[i]);
            Scene scene1 = sceneMap.get(id);
            sceneList.add(scene1);
        }
        return  sceneList;

    }

    public void moveTo(PlayerModel playerModel,int sceneId){
        Scene targetScene = sceneMap.get(sceneId);
        playerModel.setLoc(sceneId);
        playerModel.setNowScene(targetScene);
    }

}
