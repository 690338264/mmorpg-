package util.excel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

@Slf4j
public class FileRead {
    public static File getFile(String fileName){
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath"+fileName);
        }catch (FileNotFoundException e){
            log.info("文件不存在！");
            e.printStackTrace();
        }
        if(Objects.isNull(file)){
            log.info("文件为空");
        }
        return file;
    }

    public static String getFilePath(String fileName){return fileName;};
}
