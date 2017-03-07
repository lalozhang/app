package com.tequila.cache.disk;

import android.content.Context;

import com.tequila.Constant;
import com.tequila.model.BaseResult;
import com.tequila.net.NetworkParam;
import java.io.Serializable;

/**
 * Created by admin on 2017/3/7.
 */

public class ResponseDiskCache {

    private static Context mContext;

    private ResponseDiskCache() {

    }

    public static void initialize(Context context){
        mContext = context.getApplicationContext();
    }

    public static boolean containsKey(NetworkParam networkParam) {
        BaseResult result = get(networkParam);
        return result!=null;
    }

    public static BaseResult get(NetworkParam networkParam) {
        try {
            DiskLruCacheHelper diskLruCacheHelper = new DiskLruCacheHelper(mContext,"responseCache",50* Constant.MB);
            Serializable obj = diskLruCacheHelper.getSerializable(networkParam.newCachKey());
            if(obj instanceof BaseResult){
                return (BaseResult)obj;
            }
        }catch (Exception e){

        }

        return null;
    }

    public static void put(NetworkParam networkParam, BaseResult value) {
        try {
            DiskLruCacheHelper diskLruCacheHelper = new DiskLruCacheHelper(mContext,"responseCache",50* Constant.MB);
            if(networkParam!=null){
                String key = networkParam.newCachKey();
                diskLruCacheHelper.put(key,value);
            }
        }catch (Exception e){

        }
    }



}
