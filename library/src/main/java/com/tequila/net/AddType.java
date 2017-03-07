package com.tequila.net;

/**
 * Created by admin on 2017/3/4.
 */

public class AddType {

    public final static int NET_IN_ORDER = 0;
    public final static int NET_INSERT2HEAD = NET_IN_ORDER+1;
    public final static int NET_CANCEL_SAME = NET_INSERT2HEAD+1;
    public final static int NET_CANCEL_PRE = NET_CANCEL_SAME+1;
}
