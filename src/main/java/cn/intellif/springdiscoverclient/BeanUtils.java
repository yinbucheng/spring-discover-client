package cn.intellif.springdiscoverclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public abstract class BeanUtils {
    public static Map<String,List<String>> loadProperties(String path){
        try {
            Enumeration<URL> datas = ClassLoader.getSystemResources(path);
            Map<String,List<String>> result = new HashMap<>();
            while (datas.hasMoreElements()) {
                URL data = datas.nextElement();
                Properties properties = new Properties();
                properties.load(getInputStream(data));
                Set<Object> keys = properties.keySet();
                for(Object temp:keys){
                    String key = (String)temp;
                    String value = properties.getProperty(key);
                    List<String> tempValue = result.get(key);
                    if(tempValue==null){
                        List<String> list = new LinkedList<>();
                        list.add(value);
                        result.put(key,list);
                    }else{
                        tempValue.add(value);
                    }
                }
            }
            return result;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }



    public static InputStream getInputStream(URL url){
        URLConnection con = null;
        try {
            con = url.openConnection();
            con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
            return con.getInputStream();
        } catch (IOException e) {
            if (con instanceof HttpURLConnection) {
                ((HttpURLConnection)con).disconnect();
            }
            throw new RuntimeException(e);
        }

    }
}
