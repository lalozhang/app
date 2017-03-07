package com.tequila.callbacks;

import android.os.Handler;
import android.os.Message;
import com.alibaba.fastjson.JSON;
import com.tequila.net.NetworkStatus;
import com.tequila.cache.ResponseMemCache;
import com.tequila.cache.disk.ResponseDiskCache;
import com.tequila.model.BaseResult;
import com.tequila.net.NetworkParam;
import com.tequila.net.ErrorCode;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by admin on 2017/3/4.
 */

public class NetworkCallback implements okhttp3.Callback{

    private NetworkParam param ;
    private Handler mHandler;

    public NetworkCallback(NetworkParam param,Handler handler) {
        if(param == null){
            throw new NullPointerException("NetworkCallback's NetworkParam can not be null");
        }
        this.param = param;
        this.mHandler = handler;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if(call!=null&&!call.isCanceled()){//没有被取消
            if(mHandler!=null){
                param.errorCode = ErrorCode.SERVER_ERROR;
                Message msg = mHandler.obtainMessage(NetworkStatus.NET_ERROR, param);
                mHandler.sendMessage(msg);
            }
        }
    }

    @Override
    public void onResponse(Call call, Response response) {
        try {
            if(call!=null&&!call.isCanceled()){//没有被取消

                if(response.isSuccessful()){
                    ResponseBody responseBody = response.body();
                    if(responseBody!=null){
                        String json = responseBody.toString();
                        BaseResult result = JSON.parseObject(json, param.key.getClazz());
                        if(mHandler!=null&&result!=null){
                            param.setResult(result);
                            Message msg = mHandler.obtainMessage(NetworkStatus.NET_COMPLETE, param);
                            mHandler.sendMessage(msg);
                        }

                        if(param.memCache&result!=null){//可以缓存到内存
                            ResponseMemCache.put(param,result);
                        }

                        if(param.diskCache&result!=null){//可以缓存到磁盘
                            ResponseDiskCache.put(param,result);
                        }
                    }else{
                        //server error
                        if(mHandler!=null){
                            param.errorCode = ErrorCode.SERVER_ERROR;
                            Message msg = mHandler.obtainMessage(NetworkStatus.NET_ERROR, param);
                            mHandler.sendMessage(msg);
                        }
                    }

                }else{
                    //server error
                    if(mHandler!=null){
                        param.errorCode = ErrorCode.SERVER_ERROR;
                        Message msg = mHandler.obtainMessage(NetworkStatus.NET_ERROR, param);
                        mHandler.sendMessage(msg);
                    }
                }

            }
        } catch (Exception e) {
            //client error
            if(mHandler!=null){
                param.errorCode = ErrorCode.ERROR;
                Message msg = mHandler.obtainMessage(NetworkStatus.NET_ERROR, param);
                mHandler.sendMessage(msg);
            }

        }
    }

    public Handler getHandler() {
        return mHandler;
    }
}
