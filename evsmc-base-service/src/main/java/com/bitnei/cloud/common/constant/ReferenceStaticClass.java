package com.bitnei.cloud.common.constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface ReferenceStaticClass {

    Class value();
}
