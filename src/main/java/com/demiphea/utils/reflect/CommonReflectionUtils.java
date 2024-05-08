package com.demiphea.utils.reflect;

import com.demiphea.exception.utils.reflect.FieldNotFoundException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * CommonReflectionUtils
 * 基本反射工具类
 *
 * @author demiphea
 * @since 17.0.9
 */
public class CommonReflectionUtils extends ReflectionUtils {
    /**
     * 设置字段值
     *
     * @param clazz  类
     * @param name   字段名称
     * @param type   字段类型
     * @param target 目标对象，静态字段时为null
     * @param value  字段值
     * @author demiphea
     */
    public static void setFieldValue(Class<?> clazz, String name, Class<?> type, Object target, Object value) {
        Field field = findField(clazz, name, type);
        if (field == null) {
            throw new FieldNotFoundException();
        }
        makeAccessible(field);
        setField(field, target, value);
    }

    /**
     * 设置字段值
     *
     * @param clazz  类
     * @param name   字段名称
     * @param target 目标对象，静态字段时为null
     * @param value  字段值
     * @author demiphea
     */
    public static void setFieldValue(Class<?> clazz, String name, Object target, Object value) {
        setFieldValue(clazz, name, null, target, value);
    }

    /**
     * 设置字段值
     *
     * @param clazz  类
     * @param target 目标对象，静态字段时为null
     * @param value  字段值
     * @author demiphea
     */
    public static void setFieldValue(Class<?> clazz, Class<?> type, Object target, Object value) {
        setFieldValue(clazz, null, type, target, value);
    }

    /**
     * 设置静态字段值
     *
     * @param clazz 类
     * @param name  字段名称
     * @param type  字段类型
     * @param value 字段值
     * @author demiphea
     */
    public static void setStaticFieldValue(Class<?> clazz, String name, Class<?> type, Object value) {
        setFieldValue(clazz, name, type, null, value);
    }

    /**
     * 设置静态字段值
     *
     * @param clazz 类
     * @param name  字段名称
     * @param value 字段值
     * @author demiphea
     */
    public static void setStaticFieldValue(Class<?> clazz, String name, Object value) {
        setFieldValue(clazz, name, null, null, value);
    }

    /**
     * 设置静态字段值
     *
     * @param clazz 类
     * @param type  字段类型
     * @param value 字段值
     * @author demiphea
     */
    public static void setStaticFieldValue(Class<?> clazz, Class<?> type, Object value) {
        setFieldValue(clazz, null, type, null, value);
    }


    /**
     * 获取字段值
     *
     * @param clazz  类
     * @param name   字段名称
     * @param type   字段类型
     * @param target 目标对象，静态字段时为null
     * @return {@link Object} 字段值
     * @author demiphea
     */
    public static Object getFieldValue(Class<?> clazz, String name, Class<?> type, Object target) {
        Field field = findField(clazz, name, type);
        if (field == null) {
            throw new FieldNotFoundException();
        }
        makeAccessible(field);
        return getField(field, target);
    }

    /**
     * 获取字段值
     *
     * @param clazz  类
     * @param name   字段名称
     * @param target 目标对象，静态字段时为null
     * @return {@link Object} 字段值
     * @author demiphea
     */
    public static Object getFieldValue(Class<?> clazz, String name, Object target) {
        return getFieldValue(clazz, name, null, target);
    }

    /**
     * 获取字段值
     *
     * @param clazz  类
     * @param type   字段类型
     * @param target 目标对象，静态字段时为null
     * @return {@link Object} 字段值
     * @author demiphea
     */
    public static Object getFieldValue(Class<?> clazz, Class<?> type, Object target) {
        return getFieldValue(clazz, null, type, target);
    }

    /**
     * 获取静态字段值
     *
     * @param clazz 类
     * @param name  字段名称
     * @param type  字段类型
     * @return {@link Object} 字段值
     * @author demiphea
     */
    public static Object getStaticFieldValue(Class<?> clazz, String name, Class<?> type) {
        return getFieldValue(clazz, name, type, null);
    }

    /**
     * 获取静态字段值
     *
     * @param clazz 类
     * @param name  字段名称
     * @return {@link Object} 字段值
     * @author demiphea
     */
    public static Object getStaticFieldValue(Class<?> clazz, String name) {
        return getFieldValue(clazz, name, null, null);
    }

    /**
     * 获取静态字段值
     *
     * @param clazz 类
     * @param type  字段类型
     * @return {@link Object} 字段值
     * @author demiphea
     */
    public static Object getStaticFieldValue(Class<?> clazz, Class<?> type) {
        return getFieldValue(clazz, null, type, null);
    }

}
