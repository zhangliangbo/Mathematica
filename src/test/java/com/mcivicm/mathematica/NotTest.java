package com.mcivicm.mathematica;

import org.junit.Test;

import static com.mcivicm.mathematica.And.and;
import static com.mcivicm.mathematica.Not.not;

/**
 * Created by zhang on 2017/9/11.
 */

public class NotTest {
    @Test
    public void name() throws Exception {
        System.out.println(and(new Boolean[0]));
        System.out.println(and(1 > 0, not(0 > 1), 1 + 1 == 2));
        System.out.println(and(1 + 1 == 2, 2 + 2 == 4));
        System.out.println(and(not(1 + 1 == 3), not(2 + 2 == 3)));
        System.out.println(not(not(1 + 1 == 2)));//双重否定等于肯定
        System.out.println(and((Boolean[]) null));
    }
}