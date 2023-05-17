package com.fly.test.sf;

/**
 * 冒泡排序
 *
 * @author lxs
 * @date 2023/5/4
 */
public class Bubbling {


    public static void main(String[] args) {


        String[] strs = {"2", "23"};

        // 指定一个数组
        int[] numbers = {2,11,4,44,21,123};
        // 接受一下方法冒泡排序后的数组
        int[] numbers2 = BubbleSort(numbers);
        // 遍历打印查看结果
        for (int j = 0; j < numbers2.length; j++) {
            System.out.println(numbers2[j]);
        }
    }

    // 写一个冒泡排序的方法供调用
    public static int[] BubbleSort(int[] numbers){
        // 数组的长度
        int length = numbers.length;
        // 数组中用于交换的中间值的初始值
        int temp = 0;
        // 对数组进行遍历，索引要在数量的基础上进行减一
        for (int i = 0; i < length-1; i++) {

            //对剩余的元素一次进行比较，可以看见剩下的会越来越少
            for (int j = 0; j < length-1-i; j++) {

                //由大往小进行排序
                if (numbers[j+1] > numbers[j]){
                    //如果后面一个元素比前面一个大就进行交换就可以了
                    temp = numbers[j+1];
                    numbers[j+1] = numbers[j];
                    numbers[j] = temp;
                }
            }
        }
        return numbers;
    }

}
