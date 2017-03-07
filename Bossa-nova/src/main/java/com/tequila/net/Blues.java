package com.tequila.net;

import android.os.Handler;
import com.tequila.model.BaseParam;
import java.io.Serializable;

/**
 * Created by lalo on 2017/3/7.
 */

public class Blues {

    private final static FeatureBase[] DEFAULT_FEATURE = new FeatureBase[]{FeatureBase.CANCELABLE,FeatureBase.CANCEL_SAME};


    public static void startRequest(BaseParam param, IServiceMap key, Serializable extra,Handler handler, FeatureBase...featureBases){
        NetworkParam networkParam = genNetworkParam(param,key,featureBases);
        if(extra!=null){
            networkParam.extra = extra;
        }
        OkHttpManager.getInstance().addTask(networkParam,handler);
    }

    public static void startRequest(BaseParam param, IServiceMap key,Handler handler, FeatureBase...featureBases){
        startRequest(param,key,null,handler,featureBases);
    }

    public static void startRequest(BaseParam param, IServiceMap key, Serializable extra,Handler handler){
        startRequest(param,key,extra,handler,null);
    }

    public static void startRequest(BaseParam param, IServiceMap key,Handler handler){
        startRequest(param,key,null,handler);
    }

    private static NetworkParam genNetworkParam(BaseParam param,IServiceMap key,FeatureBase...featureBases){

        if(featureBases==null||featureBases.length==0){
            featureBases=DEFAULT_FEATURE;
        }
        NetworkParam networkParam = new NetworkParam(key,param);

        for(FeatureBase featureBase:featureBases){
            if(featureBase == FeatureBase.CANCEL_PRE||
                    featureBase == FeatureBase.CANCEL_SAME||
                    featureBase == FeatureBase.INSERT2HEAD||
                    featureBase == FeatureBase.INSERT_IN_ORDER){
                networkParam.addType = featureBase.getCode();
            }else if(featureBase == FeatureBase.MEM_CACHE){
                networkParam.memCache = true;
            }else if(featureBase == FeatureBase.DISK_CACHE){
                networkParam.diskCache = true;
            }else if(featureBase == FeatureBase.CANCELABLE){
                networkParam.cancelAble = true;
            }
        }
        return networkParam;

    }

    public static void cancelWithHandler(Handler handler){
        OkHttpManager.getInstance().cancelWithHandler(handler);
    }

    public static void cancel(NetworkParam networkParam){
        OkHttpManager.getInstance().cancel(networkParam);
    }

    public static void cancelAll(){
        OkHttpManager.getInstance().cancelAll();
    }


}
