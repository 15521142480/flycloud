package com.fly.test.collection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ConcurrentHashMap
 *
 * @author lxs
 * @date 2023/2/10
 */
public class ConcurrentHashMapTest {


    static ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String,Integer>();
    static ExecutorService executorService = Executors.newFixedThreadPool(100);
    static boolean isNewMap = false;


    public static synchronized void getAndSetMap(String key) {
         int newKey = map.get(key) + 1;
         map.put(key, newKey);
    }

    public static void main(String[] args) throws InterruptedException {

        // 1. 有线程安全的问题
        // 在step1跟step2中，都只是调用ConcurrentHashMap的方法，各自都是原子操作，是线程安全的。但是他们组合在一起的时候就会有问题了，A线程在进入方法后，通过map.get("key")拿到key的值，刚把这个值读取出来还没有加1的时候，线程B也进来了，那么这导致线程A和线程B拿到的key是一样的
//        map.put("key", 1);
//        for (int i = 0; i < 1000; i++) {
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    int key = map.get("key") + 1; //step 1
//                    map.put("key", key);//step 2
//                }
//            });
//        }

        // 2. 无线程安全的问题; 使用synchronized, 但效率慢
//        map.put("key", 1);
//        for (int i = 0; i < 1000; i++) {
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    getAndSetMap("key"); //step 1 & step 2
//                }
//            });
//        }

        // 3. 无线程安全的问题; 使用原子类, 效率快
        isNewMap = true;
        ConcurrentHashMap<String, AtomicInteger> new_map = new ConcurrentHashMap<String,AtomicInteger>();
        AtomicInteger integer = new AtomicInteger(1);
        new_map.put("key", integer);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    new_map.get("key").incrementAndGet();
                }
            });
        }

        Thread.sleep(3000); //模拟等待执行结束
        if (isNewMap) {
            System.out.println("------" + new_map.get("key") + "------");
        } else {
            System.out.println("------" + map.get("key") + "------");
        }
        executorService.shutdown();




    }

}
