package com.dempe.forest.rpc;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/11
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public class ReflectionUtils {

    /**
     * Perform the given callback operation on all matching methods of the
     * given class and superclasses.
     * <p>The same named method occurring on subclass and superclass will
     * appear twice, unless excluded by a {@link MethodFilter}.
     *
     * @param targetClass class to start looking at
     * @param mc          the callback to invoke for each method
     * @see #doWithMethods(Class, MethodCallback, MethodFilter)
     */
    public static void doWithMethods(Class<?> targetClass, MethodCallback mc) throws IllegalArgumentException {
        doWithMethods(targetClass, mc, null);
    }

    /**
     * Perform the given callback operation on all matching methods of the
     * given class and superclasses.
     * <p>The same named method occurring on subclass and superclass will
     * appear twice, unless excluded by the specified {@link MethodFilter}.
     *
     * @param targetClass class to start looking at
     * @param mc          the callback to invoke for each method
     * @param mf          the filter that determines the methods to apply the callback to
     */
    public static void doWithMethods(Class<?> targetClass, MethodCallback mc, MethodFilter mf)
            throws IllegalArgumentException {

        // Keep backing up the inheritance hierarchy.
        do {
            Method[] methods = targetClass.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                if (mf != null && !mf.matches(methods[i])) {
                    continue;
                }
                try {
                    mc.doWith(methods[i]);
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException(
                            "Shouldn't be illegal to access method '" + methods[i].getName() + "': " + ex);
                }
            }
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null);
    }

    /**
     * @param cls
     * @return
     */
    public static boolean isVoid(Class<?> cls) {
        if (cls == Void.class || cls == void.class) {
            return true;
        }
        return false;
    }

    /**
     * Action to take on each method.
     */
    public static interface MethodCallback {

        /**
         * Perform an operation using the given method.
         *
         * @param method the method to operate on
         */
        void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
    }


    /**
     * Callback optionally used to method fields to be operated on by a method callback.
     */
    public static interface MethodFilter {

        /**
         * Determine whether the given method matches.
         *
         * @param method the method to check
         */
        boolean matches(Method method);
    }
}
