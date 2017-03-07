package com.tequila.net;

import android.os.Handler;

/**
 * Created by lalo on 2017/3/7.
 */

public class Blues {



    public static void cancelWithHandler(Handler handler){
        OkHttpManager.getInstance().cancelWithHandler(handler);
    }


}
