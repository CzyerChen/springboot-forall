package com.stest.leetcode;

import java.util.ArrayDeque;
import java.util.Arrays;

public class SortTest {

    /**
     * n n^2 1
     *
     * 1 l
     * 0 l-1
     * flag true break
     * @param arr
     * @return
     */
    public static int[] bubleSort(int[] arr){
        for(int i = 1 ;i < arr.length;i++){
            boolean flag = true;
            for(int j =0 ; j< arr.length-1 ;j++){
                if(arr[j] > arr[j+1]){
                    int t = arr[j+1];
                    arr[j+1] = arr[j];
                    arr[j]=t;
                    flag = false;
                }
            }
            if(flag){
                break;
            }
        }
        return arr;
    }

    /**
     * n^ 2 n^2 1
     *
     * 0 l-1
     * i+1 l
     * max = i
     * @param arr
     * @return
     */
    public static int[] choiceSort(int[] arr){
        for(int i = 0 ; i <arr.length-1;i++){
            int max = i;
            for(int j = i+1 ; j<arr.length ; j++){
                if(arr[j]<arr[max]){
                    max = j;
                }
            }
            if(max != i){
                int tmp = arr[max];
                arr[max] = arr[i];
                arr[i] = tmp;
            }
        }
        return arr;
    }

    /**
     * n n^2 1
     * i len
     * j =i
     * tmp = arr[i]
     *
     * @param arr
     * @return
     */
    public static int[] insertSort(int[] arr){
        for(int i=1 ;i<arr.length;i++){
            int j = i;
            int tmp = arr[i];
            while(j>0 && tmp < arr[j-1]){
                arr[j] = arr[j-1];
                j--;
            }
            if(j != i){
                arr[j] = tmp;
            }
        }
        return arr;
    }



    /**
     * nlgn nlgn n
     * @param arr
     * @return
     */
    public static void mergeSort(int[] arr,int[] tmp,int l, int r){

        if(l<r) {
            int middle = (l+r)/2;
            mergeSort(arr, tmp, l, middle);
            mergeSort(arr, tmp,middle+1,r);
            merge(arr,tmp,l,r,middle);
        }
    }

    public static void merge(int[] arr,int[] tmp,int l ,int r,int middle){
        int i=l;
        int j =middle+1;
        for(int k = l ; k<=r ; k++){
            if(i >middle){
                tmp[k]= arr[j++];
            }else if(j>r){
                tmp[k] = arr[i++];
            }else if(arr[i] <= arr[j]){
                tmp[k] = arr[i++];
            }else {
                tmp[k] = arr[j++];
            }
        }
        System.arraycopy(tmp,l,arr,l,r-l +1);
    }



    public static void quickSort(int[] arr ,int left ,int right){
        if(left < right){
            int i = left;
            int j = right;
            int x = arr[i];
            while(i<j){
                while( i<j && arr[j] > x){
                    j--;
                }
                if(i<j){
                    arr[i++] = arr[j];
                }

                while( i< j && arr[i] < x){
                    i++;
                }
                if(i<j){
                    arr[j--]= arr[i];
                }
            }
            arr[i]= x;
            quickSort(arr,left,i);
            quickSort(arr,i+1,right);
        }
    }

    public static void main(String[] args) throws Exception {
        int[] arr = new int[]{3,8,4,1,9,5,10};
        quickSort(arr,0,arr.length-1);
        System.out.print(Arrays.toString(arr));
    }
}
