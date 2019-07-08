package com.notes.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by claire on 2019-07-05 - 19:17
 **/
public class ConvertUtils {

    public static <T> List<T> array2List(T[] array){
        //如果是单纯的Arrays.asList(array) ，返回值List, 不是ArrayList,并没有arrayList的全部方法
        return new ArrayList<>(Arrays.asList(array));
    }

    /*public static <T> T[] list2Array(List<T> list){
        return list.toArray(new T[list.size()]);
    }*/
}
