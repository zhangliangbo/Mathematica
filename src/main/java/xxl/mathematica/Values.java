package xxl.mathematica;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 * 获取值
 */
public class Values {
    /**
     * 值的列表
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> List<V> values(Map<K, V> map) {
        ObjectHelper.requireNonNull(map);
        return new ArrayList<>(map.values());
    }
}