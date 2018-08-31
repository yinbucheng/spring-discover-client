package cn.intellif.springdiscoverclient;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanConfigPostProcessor implements EnvironmentPostProcessor{
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>采用手动注入apollo配置文件");
       Config config = ConfigService.getAppConfig();
       Set<String> names = config.getPropertyNames();
       if(names!=null&&names.size()>0){
           Map<String,Object> configMap = new HashMap<>();
           for(String name:names){
              String value = config.getProperty(name,null);
              configMap.put(name,value);
           }
           PropertySource propertySource = new MapPropertySource("default",configMap);
           environment.getPropertySources().addFirst(propertySource);
       }
    }
}
