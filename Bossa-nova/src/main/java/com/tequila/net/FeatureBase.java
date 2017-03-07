package com.tequila.net;

/**
 * Created by lalo on 2017/3/7.
 */

public enum FeatureBase {
    INSERT_IN_ORDER{

        @Override
        public int getCode() {
            return AddType.NET_IN_ORDER;
        }
    },
    INSERT2HEAD{

        @Override
        public int getCode() {
            return AddType.NET_INSERT2HEAD;
        }
    },
    CANCEL_PRE{

        @Override
        public int getCode() {
            return AddType.NET_CANCEL_PRE;
        }
    },

    CANCEL_SAME{
        @Override
        public int getCode() {
            return AddType.NET_CANCEL_SAME;
        }
    },
    CANCELABLE,
    DISK_CACHE,
    MEM_CACHE,


    ;


    public int getCode(){
        return -1;
    }

}
