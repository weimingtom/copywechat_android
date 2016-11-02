
package com.anjuke.android.commonutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortUtil {

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>> void mergeSort(List<T> arr) {
        T[] tmpArr = arr.toArray((T[]) new Comparable[arr.size()]);
        mergeSort(tmpArr);
        arr.clear();
        arr.addAll(Arrays.asList(tmpArr));
    }

    /**
     * 归并排序两个已升序完的列表
     * 
     * @param list1 已升序列表1
     * @param list2 已升序列表2
     * @return 升序后的新列表
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>> List<T> mergeSort(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        if (list1 == null || list1.size() <= 0) {
            if (list2 != null && list2.size() > 0)
                list.addAll(list2);
            return list;
        }

        if (list2 == null || list2.size() <= 0) {
            list.addAll(list1);
            return list;
        }

        // 转成数组，更快，效率更高
        T[] arr1 = list1.toArray((T[]) new Comparable[list1.size()]);
        T[] arr2 = list2.toArray((T[]) new Comparable[list2.size()]);

        T[] arr = (T[]) new Comparable[arr1.length + arr2.length];

        int i = 0;
        int i1 = 0;
        int i2 = 0;
        while (i1 < arr1.length && i2 < arr2.length) {
            if (arr1[i1].compareTo(arr2[i2]) <= 0)
                arr[i++] = arr1[i1++];
            else
                arr[i++] = arr2[i2++];
        }
        while (i1 < arr1.length)
            arr[i++] = arr1[i1++];
        while (i2 < arr2.length)
            arr[i++] = arr2[i2++];

        return Arrays.asList(arr);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>> void mergeSort(T[] arr) {
        T[] tmpArr = (T[]) new Comparable[arr.length];
        mergeSort(arr, tmpArr, 0, arr.length - 1);
    }

    private static <T extends Comparable<? super T>> void mergeSort(T[] arr, T[] tmpArr,
                                                                    int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(arr, tmpArr, left, center);
            mergeSort(arr, tmpArr, center + 1, right);
            merge(arr, tmpArr, left, center + 1, right);
        }
    }

    private static <T extends Comparable<? super T>> void merge(T[] arr, T[] tmpArr,
                                                                int lPos, int rPos, int rEnd) {
        int lEnd = rPos - 1;
        int tPos = lPos;
        int leftTmp = lPos;

        while (lPos <= lEnd && rPos <= rEnd) {
            if (arr[lPos].compareTo(arr[rPos]) <= 0)
                tmpArr[tPos++] = arr[lPos++];
            else
                tmpArr[tPos++] = arr[rPos++];
        }

        while (lPos <= lEnd)
            tmpArr[tPos++] = arr[lPos++];
        while (rPos <= rEnd)
            tmpArr[tPos++] = arr[rPos++];

        for (; rEnd >= leftTmp; rEnd--)
            arr[rEnd] = tmpArr[rEnd];
    }
}
