package com.tequila.net;

import android.os.Handler;

/**
 * Created by lalo on 2017/3/4.
 */

public class NetworkTask {

    public boolean cancel = false;
    public NetworkParam param;
    public Handler handler;

    public NetworkTask(NetworkParam p, Handler handler) {
        this.param = p;
        this.handler = handler;
    }
}
