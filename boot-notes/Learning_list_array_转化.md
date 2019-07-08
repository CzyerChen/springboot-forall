### list --> Array
```text
  //这边并不能直接用，这个T就是泛指你需要转化的类，因为在new T[]数组建立的时候，必须要是实例化，不能抽象
    public static <T> T[] list2Array(List<T> list){
        return list.toArray(new T[list.size()]);
    }
}
```

### Array --> List
```text

    public static <T> List<T> array2List(T[] array){
        //如果是单纯的Arrays.asList(array) ，返回值List, 不是ArrayList,并没有arrayList的全部方法
        return new ArrayList<>(Arrays.asList(array));
    }
```