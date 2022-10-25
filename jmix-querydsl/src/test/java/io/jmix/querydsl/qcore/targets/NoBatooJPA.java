package io.jmix.querydsl.qcore.targets;

import java.lang.annotation.Target;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface NoBatooJPA {

    io.jmix.querydsl.qcore.targets.Target[] value() default {};
}
