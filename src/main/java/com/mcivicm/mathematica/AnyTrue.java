package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Predicate;

import java.util.List;

/**
 * 任一为真
 */

public class AnyTrue {
    /**
     * 如果 test[Subscript[e, i]] 对于任意一个 Subscript[e, i] 为 True，则生成 True.
     *
     * @param list
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> boolean anyTrue(List<T> list, Predicate<T> predicate) {
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(predicate, "predicate");

        for (T t : list) {
            if (predicate.test(t)) {
                return true;
            }
        }

        return false;
    }
}