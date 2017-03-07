package com.tequila.util;

import java.util.Collection;
import java.util.Map;

/**
 * Created by lalo on 2016/10/2.
 */
public class CheckUtils {

    private CheckUtils() {
    }

    public static boolean isEmpty(Object obj) {
        if(obj == null) {
            return true;
        } else if(obj instanceof Collection) {
            return ((Collection)obj).isEmpty();
        } else if(obj instanceof Map) {
            return ((Map)obj).isEmpty();
        } else if(obj instanceof CharSequence) {
            return ((CharSequence)obj).length() == 0;
        } else {
            if(obj.getClass().isArray()) {
                if(obj instanceof Object[]) {
                    return ((Object[])((Object[])obj)).length == 0;
                }

                if(obj instanceof int[]) {
                    return ((int[])((int[])obj)).length == 0;
                }

                if(obj instanceof long[]) {
                    return ((long[])((long[])obj)).length == 0;
                }

                if(obj instanceof short[]) {
                    return ((short[])((short[])obj)).length == 0;
                }

                if(obj instanceof double[]) {
                    return ((double[])((double[])obj)).length == 0;
                }

                if(obj instanceof float[]) {
                    return ((float[])((float[])obj)).length == 0;
                }

                if(obj instanceof boolean[]) {
                    return ((boolean[])((boolean[])obj)).length == 0;
                }

                if(obj instanceof char[]) {
                    return ((char[])((char[])obj)).length == 0;
                }

                if(obj instanceof byte[]) {
                    return ((byte[])((byte[])obj)).length == 0;
                }
            }
            return false;
        }
    }



}
