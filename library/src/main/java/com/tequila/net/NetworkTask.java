package com.tequila.net;

import android.os.Handler;

/**
 * Created by admin on 2017/3/4.
 */

public class NetworkTask {

    public boolean cancel = false;
    public final NetworkParam param;
    public final Handler handler;

    public NetworkTask(NetworkParam p, Handler handler) {
        this.param = p;
        this.handler = handler;
    }
}
