package com.jaiaxn.adaptation.utils.assembler;

import com.google.common.collect.Lists;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: wangjx@tisntergy.com
 * @date: 2020年10月20日
 * @description: 对象（集合）转换工具类
 **/
public class Assembler {
    private static final ConcurrentMap<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<>();

    public static BeanCopier getBeanCopier(Class<?> source, Class<?> target, boolean useConverter) {
        String beanCopierKey = source.getSimpleName() + "@" + target.getSimpleName() + "@useConverter" + useConverter;
        BeanCopier beanCopier = beanCopierMap.getOrDefault(beanCopierKey, BeanCopier.create(source, target, false));
        beanCopierMap.putIfAbsent(beanCopierKey, beanCopier);
        return beanCopier;
    }

    /**
     * 只对target与source间的字段转换
     *
     * @param target target
     * @param source source
     * @return target类型的JavaBean
     */
    public static <T> T assemble(Class<T> target, Object source) {
        return assemble(target, source, null, null);
    }

    /**
     * target与source间的字段转换，提供对特殊字段的类型转换
     *
     * @param target target
     * @param source source
     * @param converter 特殊字段的类型转换
     * @return target类型的JavaBean
     */
    public static <T> T assemble(Class<T> target, Object source, Converter converter) {
        return assemble(target, source, null, converter);
    }

    /**
     * target与source间的字段转换，提供钩子函数
     *
     * @param target target
     * @param source source
     * @param callBack 回调钩子函数
     * @return target类型的JavaBean
     */
    public static <T> T assemble(Class<T> target, Object source, AssemblerCallBack callBack) {
        return assemble(target, source, callBack, null);
    }

    /**
     * target与source间的字段转换，提供钩子函数和类型转换
     *
     * @param target target
     * @param source source
     * @param callBack 回调钩子函数
     * @param converter 特殊字段的类型转换
     * @return target类型的JavaBean
     */
    public static <T> T assemble(Class<T> target, Object source, AssemblerCallBack callBack, Converter converter) {
        if (source == null) {
            return null;
        }
        try {
            BeanCopier beanCopier;
            if (converter == null) {
                beanCopier = getBeanCopier(source.getClass(), target, false);
            } else {
                beanCopier = getBeanCopier(source.getClass(), target, true);
            }
            T t = target.newInstance();
            beanCopier.copy(source, t, converter);
            if (callBack != null) {
                callBack.afterAssemble(t, source);
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException("create object fail, class:" + target.getName() + " ", e);
        }
    }

    /**
     * 将集合sources中的对象转换为target类型
     *
     * @param target target
     * @param sources sources
     * @return List
     */
    public static <T> List<T> multiAssemble(Class<T> target, Collection<?> sources) {
        return multiAssemble(target, sources, null, null);
    }

    /**
     * 将集合sources中的对象转换为target类型，提供对特殊字段的类型转换
     *
     * @param target target
     * @param sources sources
     * @param converter 特殊字段的类型转换
     * @return List
     */
    public static <T> List<T> multiAssemble(Class<T> target, Collection<?> sources, Converter converter) {
        return multiAssemble(target, sources, null, converter);
    }

    /**
     * 将集合sources中的对象转换为target类型，提供钩子函数
     *
     * @param target target
     * @param sources sources
     * @param callBack 回调钩子函数
     * @return List
     */
    public static <T> List<T> multiAssemble(Class<T> target, Collection<?> sources, AssemblerCallBack callBack) {
        return multiAssemble(target, sources, callBack, null);
    }

    /**
     * 将集合sources中的对象转换为target类型，提供钩子函数和类型转换
     *
     * @param target target
     * @param sources sources
     * @param callBack 回调钩子函数
     * @param converter 特殊字段的类型转换
     * @return List
     */
    public static <T> List<T> multiAssemble(Class<T> target, Collection<?> sources, AssemblerCallBack callBack, Converter converter) {
        List<T> targets = Lists.newArrayList();
        if (CollectionUtils.isEmpty(sources)) {
            return targets;
        }
        try {
            BeanCopier beanCopier;
            if (converter == null) {
                beanCopier = getBeanCopier(sources.toArray()[0].getClass(), target, false);
            } else {
                beanCopier = getBeanCopier(sources.toArray()[0].getClass(), target, true);
            }
            for (Object object : sources) {
                T t = target.newInstance();
                beanCopier.copy(object, t, converter);
                if (callBack != null) {
                    callBack.afterAssemble(t, object);
                }
                targets.add(t);
            }
        } catch (Exception e) {
            throw new RuntimeException("create object fail, class:" + target.getName() + " ", e);
        }
        return targets;
    }
}
