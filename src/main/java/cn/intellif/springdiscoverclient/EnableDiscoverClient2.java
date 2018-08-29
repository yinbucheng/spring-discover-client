package cn.intellif.springdiscoverclient;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(DiscoverBeanRegister.class)
public @interface EnableDiscoverClient2 {
    String value() default "EUREKA";
}
