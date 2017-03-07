package com.tequila.callbacks;

import android.os.Handler;
import android.os.Message;
import com.tequila.net.NetworkStatus;
import com.tequila.net.NetworkParam;

/**
 * Created by admin on 2017/3/4.
 */

public class HandlerCallbacks {


    public static abstract class CommonCallback implements Handler.Callback {

        private Handler.Callback mCallback;

        public CommonCallback() {
            this(null);
        }

        public CommonCallback(Handler.Callback callback) {
            super();
            this.mCallback = callback;
        }

        @Override
        public boolean handleMessage(Message message) {
            if(this.mCallback!=null){
                mCallback.handleMessage(message);
            }
            return false;
        }

        public void removeCalback(){
            mCallback = null;
        }
    }


    public static class ActivityCallback extends CommonCallback{

        private NetworkListener networkListener;

        public ActivityCallback(NetworkListener networkListener) {
            this(null,networkListener);
        }

        public ActivityCallback(Handler.Callback callback, NetworkListener networkListener) {
            super(callback);
            if(networkListener!=null){
                this.networkListener = networkListener;
            }else{
                throw new NullPointerException("NetworkListener must not be null");
            }
        }

        @Override
        public boolean handleMessage(Message message) {
            if(message.obj instanceof NetworkParam){
                int what = message.what;
                NetworkParam param = (NetworkParam) message.obj;
                switch (what){
                    case NetworkStatus.NET_START:
                        synchronized (this){
                            if(networkListener!=null){
                                networkListener.onNetworkStart(param);
                            }
                        }

                        break;
                    case NetworkStatus.NET_COMPLETE:
                        synchronized (this){
                            if(networkListener!=null){
                                networkListener.onNetworkComplete(param);
                            }
                        }

                        break;
                    case NetworkStatus.NET_ERROR:
                        synchronized (this){
                            if(networkListener!=null){
                                networkListener.onError(param);
                            }
                        }
                        break;
                    case NetworkStatus.CACHE_HIT:
                        synchronized (this){
                            if(networkListener!=null){
                                networkListener.onCache(param);
                            }
                        }

                        break;

                    default:
                        break;
                }

            }
            return super.handleMessage(message);
        }
    }

    public final static class FragmentCallback extends ActivityCallback {

        public FragmentCallback(NetworkListener listener) {
            super(listener);
        }

        public FragmentCallback(NetworkListener listener, Handler.Callback callback) {
            super(callback, listener);
        }
    }


}
