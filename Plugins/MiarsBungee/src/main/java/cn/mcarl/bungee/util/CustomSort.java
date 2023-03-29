package cn.mcarl.bungee.util;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;

import java.util.Comparator;
import java.util.List;

public class CustomSort {

    /**
     * bean的集合按照指定bean的字段排序
     * @param list 要排序的集合
     * @param filedName 字段名称
     * @param ascFlag 是否升序
     */
    public static void sort(List<?> list, String filedName, boolean ascFlag) {
        if (list.size() == 0 || filedName.equals("")) {
            return;
        }
        Comparator<?> cmp = ComparableComparator.getInstance();
        if (ascFlag) {
            cmp = ComparatorUtils.nullLowComparator(cmp);
        } else {
            cmp = ComparatorUtils.reversedComparator(cmp);

        }
        list.sort(new BeanComparator<Object>(filedName, cmp));
    }
}
