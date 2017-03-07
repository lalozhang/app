package com.tequila.net;

import android.nfc.NfcEvent;
import android.os.Handler;

import com.tequila.model.BaseParam;

/**
 * Created by lalo on 2017/3/7.
 */

public class Blues {

    private final static FeatureBase[] DEFAULT_FEATURE = new FeatureBase[]{};


    public static void startRequest(IServiceMap key, BaseParam param,Handler handler,FeatureBase...featureBases){
        NetworkParam networkParam = genNetworkParam(key,param,featureBases);


    }

    private static NetworkParam genNetworkParam(IServiceMap key, BaseParam param,FeatureBase...featureBases){

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

            }
        }
        return null;

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
