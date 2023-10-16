package kv.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListUtils {
    public static <T> List<List<T>> splitList(List<T> originList, int batchSize) {
        List<List<T>> lists = new ArrayList<>((int) Math.ceil(originList.size() / (double) batchSize));

        List<T> curList = null;
        for (int i = 0; i < originList.size(); i++) {
            if (i % batchSize == 0) {
                curList = new ArrayList<>(batchSize);
                lists.add(curList);
            }
            curList.add(originList.get(i));
        }

        return lists;
    }

    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static double[] toDoubleArrayAndSortAsc(List<Double> list) {
        double[] doubles = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            doubles[i] = list.get(i);
        }
        Arrays.sort(doubles);

        return doubles;
    }
}
