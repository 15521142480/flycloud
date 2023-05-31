package com.fly.test.java.singleton;

/**
 * 线程安全的单例 (通过静态内部类实现)
 *
 * @author lxs
 * @date 2023/5/25
 */
public class Syn01Singleton {


    public Syn01Singleton () {

    }

    /**
     * 静态内部类实现模式（线程安全，调用效率高，可以延时加载)
     * 用来记录感知是否进行实例化
     *
     */
    private static class Syn01SingletonInst {

        private static Syn01Singleton syn01Singleton = new Syn01Singleton();
    }


    /**
     * 获取单例
     *
     */
    public static Syn01Singleton getSyn01Singleton () {
        return Syn01SingletonInst.syn01Singleton;
    }


}
