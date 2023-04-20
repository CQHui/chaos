package com.qihui.chaos.demo.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Elliott Chen on 2023/4/19 20:06
 */
public class ConstructorDemo {

    public int status;

    public ConstructorDemo(int status) {
        this.status = status;
    }

    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?>[] constructors = ConstructorDemo.class.getConstructors();
        Constructor<?> constructor = constructors[0];
        ConstructorDemo obj = (ConstructorDemo) constructor.newInstance(1);
        System.out.println(obj.status);

    }
}
