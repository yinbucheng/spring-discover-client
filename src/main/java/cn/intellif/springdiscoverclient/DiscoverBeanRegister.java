package cn.intellif.springdiscoverclient;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.eureka.RibbonEurekaAutoConfiguration;
import org.springframework.cloud.zookeeper.ZookeeperAutoConfiguration;
import org.springframework.cloud.zookeeper.discovery.RibbonZookeeperAutoConfiguration;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryAutoConfiguration;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryClientConfiguration;
import org.springframework.cloud.zookeeper.discovery.dependency.DependencyFeignClientAutoConfiguration;
import org.springframework.cloud.zookeeper.discovery.dependency.DependencyRestTemplateAutoConfiguration;
import org.springframework.cloud.zookeeper.discovery.dependency.DependencyRibbonAutoConfiguration;
import org.springframework.cloud.zookeeper.discovery.watcher.DependencyWatcherAutoConfiguration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperAutoServiceRegistrationAutoConfiguration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistryAutoConfiguration;
import org.springframework.cloud.zookeeper.support.CuratorServiceDiscoveryAutoConfiguration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DiscoverBeanRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        if(annotationMetadata.hasAnnotation(EnableDiscoverClient2.class.getName())) {
           Map<String,Object> objectMap = annotationMetadata.getAnnotationAttributes(EnableDiscoverClient2.class.getName());
           String type = getType(objectMap).toUpperCase();
            if (type.equals("EUREKA")) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>注册EUREKA");
                registerEureka(beanDefinitionRegistry);
            } else if (type.equals("ZOOKEEPER")) {
                System.out.println(">>>>>>>>>>>>>>>>>注册ZOOKEEPER");
                registerZookeeper(beanDefinitionRegistry);
            }
        }
    }

    private void registerEureka(BeanDefinitionRegistry registry){
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,EurekaClientAutoConfiguration.class.getName(),EurekaClientAutoConfiguration.class);
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,RibbonEurekaAutoConfiguration.class.getName(),RibbonEurekaAutoConfiguration.class);
    }

    private void registerZookeeper(BeanDefinitionRegistry registry){
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,ZookeeperDiscoveryAutoConfiguration.class.getName(),ZookeeperDiscoveryAutoConfiguration.class);
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,ZookeeperDiscoveryClientConfiguration.class.getName(),ZookeeperDiscoveryClientConfiguration.class);
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,CuratorServiceDiscoveryAutoConfiguration.class.getName(),CuratorServiceDiscoveryAutoConfiguration.class);
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,ZookeeperServiceRegistryAutoConfiguration.class.getName(),ZookeeperServiceRegistryAutoConfiguration.class);
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,ZookeeperAutoConfiguration.class.getName(),ZookeeperAutoConfiguration.class);
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,ZookeeperAutoServiceRegistrationAutoConfiguration.class.getName(),ZookeeperAutoServiceRegistrationAutoConfiguration.class);
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,DependencyRibbonAutoConfiguration.class.getName(),DependencyRibbonAutoConfiguration.class);
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,DependencyFeignClientAutoConfiguration.class.getName(),DependencyFeignClientAutoConfiguration.class);
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,DependencyRestTemplateAutoConfiguration.class.getName(),DependencyRestTemplateAutoConfiguration.class);
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,DependencyWatcherAutoConfiguration.class.getName(),DependencyWatcherAutoConfiguration.class);
        //这里如果抽取出来跟踪链是无法正常运行
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry, RibbonZookeeperAutoConfiguration.class.getName(),RibbonZookeeperAutoConfiguration.class);
    }

    private String getType(Map<String,Object> objectMap){
      String value = null;
      //从apollo中获取
      Config config =  ConfigService.getAppConfig();
      value = config.getProperty("register.type", null);
     //从app.properties中获取
      if(value==null){
          List<String> result =BeanUtils.loadProperties("META-INF/app.properties").getOrDefault("register.type",null);
          if(result!=null)
              value = result.get(0);
      }
     //从注解中获取
     if(value==null) {
         value = (String) objectMap.get("value");
     }
        return value;
    }
}
