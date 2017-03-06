package com.tequila.callbacks;

import com.tequila.net.NetworkParam;

/**
 * Created by admin on 2017/3/4.
 */

public interface NetworkListener {

    void onNetworkStart(NetworkParam param);

    void onNetworkRunning(NetworkParam param);

    void onNetworkComplete(NetworkParam param);

    void onNetworkError(NetworkParam param);

    void onCache(NetworkParam param);
}
