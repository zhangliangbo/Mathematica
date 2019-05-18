package mcivicm.mathematica

import mcivicm.mathematica.function.Function

import org.junit.Test

import java.util.ArrayList
import java.util.Arrays
import java.util.List

import static mcivicm.mathematica.Insert.insert
import static mcivicm.mathematica.Nest.nest
import static mcivicm.mathematica.BaseTest.printList

/**
 * Created by zhang on 2017/9/3.
 */

class InsertTest {
    @Test
    void name() throws Exception {
        printList(nest(new Function<List<Integer>, List<Integer>>() {
            @Override
            List<Integer> apply(List<Integer> list) {
                return insert(list, 1, 3)
            }
        }, Arrays.asList(1, 2, 3, 4, 5), 5))

        printList(nest(new Function<List<Integer>, List<Integer>>() {
            @Override
            List<Integer> apply(List<Integer> list) {
                return insert(list, 1, 0)
            }
        }, Arrays.asList(1, 2, 3, 4, 5), 5))

        printList(nest(new Function<List<Integer>, List<Integer>>() {
            @Override
            List<Integer> apply(List<Integer> list) {
                return insert(list, 1, list.size())
            }
        }, Arrays.asList(1, 2, 3, 4, 5), 5))

        printList(nest(new Function<List<Integer>, List<Integer>>() {
            @Override
            List<Integer> apply(List<Integer> list) {
                return insert(list, 1, -3)
            }
        }, Arrays.asList(1, 2, 3, 4, 5), 5))

        printList(nest(new Function<List<Integer>, List<Integer>>() {
            @Override
            List<Integer> apply(List<Integer> list) {
                return insert(list, 1, -1)
            }
        }, Arrays.asList(1, 2, 3, 4, 5), 5))

        printList(nest(new Function<List<Integer>, List<Integer>>() {
            @Override
            List<Integer> apply(List<Integer> list) {
                return insert(list, 1, -list.size())
            }
        }, Arrays.asList(1, 2, 3, 4, 5), 5))

    }

    @Test
    void name2() throws Exception {
        printList(insert(Arrays.asList(1, 2, 3, 4, 5), 0, new ArrayList<Integer>()))
        printList(insert(Arrays.asList(1, 2, 3, 4, 5), 0, Arrays.asList(-1)))
        printList(insert(Arrays.asList(1, 2, 3, 4, 5), 0, Arrays.asList(3, 1, 5, 4)))
        printList(insert(Arrays.asList(1, 2, 3, 4, 5), 0, Arrays.asList(0, 1, 2, 3, 4, 5)))
    }

}
