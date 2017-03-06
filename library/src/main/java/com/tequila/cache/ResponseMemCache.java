package com.tequila.cache;

import android.support.v4.util.LruCache;
import com.tequila.model.BaseResult;
import com.tequila.net.NetworkParam;

/**
 * Created by admin on 2016/10/2.
 */
public class ResponseMemCache {

    static LruCache<NetworkParam, BaseResult> cache = new LruCache<NetworkParam, BaseResult>(10);

    public static void put(NetworkParam key, BaseResult value) {
        if (key == null || value == null) {
            return;
        }
        cache.put(key, value);
    }

    public static BaseResult get(NetworkParam key) {
        return cache.get(key);
    }

    public static boolean containsKey(NetworkParam key) {
        return cache.get(key) != null;
    }
}
