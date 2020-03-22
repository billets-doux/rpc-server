package org.example;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)// 作用于类和接口
@Retention(RetentionPolicy.RUNTIME)//保留到运行时
@Component
public @interface RpcService {
    Class<?> value(); // 拿到服务类和接口

    String version() default "";

}
