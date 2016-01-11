package com.tomylearn.weixinapi.demoWebapp.util;

import java.io.*;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Created by xuefan on 2015/10/25.
 */
public class PropertiesFileUtil {

    private static final Logger log = Logger.getLogger(PropertiesFileUtil.class.getName());

    //读取资源文件,并处理中文乱码
    public static Properties readPropertiesFile(String filename) throws IOException
    {
        if(filename==null){
            return null;
        }
        Properties properties = new Properties();
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(filename);
            properties.load(inputStream);
            return properties;
        } catch (IOException e){
            log.error("read failed, filename:"+filename, e);
            throw e;
        }finally {
            try {
                if(inputStream!=null) {
                    inputStream.close();
                }
            } catch (IOException e1) {
                log.error("close file failed.",e1);
            }
        }
    }

    public static String readValue(String filename, String key){
        if(filename==null||key==null){
            return null;
        }
        Properties prop = null;
        String value = null;
        try {
            prop = readPropertiesFile(filename);
            if(prop!=null){
                value = prop.getProperty(key);
            }
        } catch (IOException e) {
            log.error("read property failed from file:" + filename, e);
        }
        return value;
    }

    //
    public static void writePropertiesFile(String filename, String key, String value)
    {
        if(filename==null||key==null||"".equals(key)||value==null){
            return;
        }
        synchronized (filename){
            Properties properties = new Properties();
            properties.setProperty(key, value);
            OutputStream outputStream = null;
            try
            {
                outputStream = new FileOutputStream(filename);
                properties.store(outputStream, "");
            } catch (IOException e){
                log.error("write file failed,filename:"+filename+",key:"+key+",value:"+value, e);
            }finally {
                try {
                    if(outputStream!=null) {
                        outputStream.close();
                    }
                } catch (IOException e1) {
                    log.error("close file failed.",e1);
                }
            }
        }

    }

    /**
     * check the file existes, if not exists then create it
     * @param fullFileName
     */
    public static void checkAndCreateFile(String fullFileName){
        File file = new File(fullFileName);
        if(file==null || !file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("create file failed, fullFileName:"+fullFileName, e);
            }
        }
    }

}

