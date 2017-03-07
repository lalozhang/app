package com.tequila.net;

/**
 * Created by lalo on 2017/3/6.
 */

public class NetworkStatus {

    public final static int NET_START = 0;

    public final static int NET_RUNNING = NET_START + 1;

    public final static int NET_COMPLETE = NET_RUNNING + 1;

    public final static int NET_ERROR = NET_COMPLETE + 1;

    public final static int CACHE_HIT = NET_ERROR + 1;

    public final static int NET_END = CACHE_HIT + 1;


}
