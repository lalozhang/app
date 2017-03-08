package com.tequila.net;

import android.os.Handler;
import android.os.Message;
import com.alibaba.fastjson.JSONObject;
import com.tequila.cache.ResponseMemCache;
import com.tequila.cache.disk.ResponseDiskCache;
import com.tequila.callbacks.FileCallback;
import com.tequila.callbacks.NetworkCallback;
import com.tequila.model.BaseParam;
import com.tequila.model.BaseResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lalo on 2017/3/4.
 */

class OkHttpManager {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient okHttpClient;
    private static volatile OkHttpManager singleInstance;
    private final LinkedList<NetworkTask> listSequence = new LinkedList<>();

    private OkHttpManager() {
        okHttpClient = new OkHttpClient().
                newBuilder().
                addInterceptor(new Interceptor() {

                    @Override
                    public Response intercept(Chain chain) {
                        try {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("X-Platform", "android")
                                    .addHeader("X-App-Id", "com.huapu.huafen")
                                    .build();
                            return chain.proceed(newRequest);
                        } catch (IOException e) {
                            return null;
                        }
                    }
                }).
                addNetworkInterceptor(new Interceptor() {

                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return null;
                    }
                }).
                connectTimeout(20, TimeUnit.SECONDS).
                readTimeout(30, TimeUnit.SECONDS).
                writeTimeout(10, TimeUnit.SECONDS).
                build();

    }

    public static OkHttpManager getInstance(){

        if(singleInstance==null){
            synchronized (OkHttpManager.class){
                if(singleInstance==null){
                    singleInstance = new OkHttpManager();
                }
            }
        }

        return singleInstance;
    }

    public void addTask(NetworkParam networkParam,Handler handler){
        boolean isRepeat = false;
        NetworkTask task = new NetworkTask(networkParam,handler);
        synchronized (listSequence){
            Iterator<NetworkTask> listSequenceIterator = listSequence.iterator();
            while (listSequenceIterator.hasNext()){
                NetworkTask networkTask = listSequenceIterator.next();
                NetworkParam tmp = networkTask.param;
                if(tmp.equals(networkParam)){
                    isRepeat = true;
                }
            }

            List<Call> runningCalls = okHttpClient.dispatcher().runningCalls();
            for (Call runCall:runningCalls){
                if(runCall.request().tag() instanceof NetworkParam){
                    NetworkParam runParam = (NetworkParam) runCall.request().tag();
                    if(runParam.equals(networkParam)){
                        isRepeat = true;
                    }
                }
            }

            List<Call> queuedCalls = okHttpClient.dispatcher().queuedCalls();
            for (Call queuedCall:queuedCalls){
                if(queuedCall.request().tag() instanceof NetworkParam){
                    NetworkParam queuedParam = (NetworkParam) queuedCall.request().tag();
                    if(queuedParam.equals(networkParam)){
                        isRepeat = true;
                    }
                }
            }

            if (isRepeat){
                return;
            }

            Message msg = handler.obtainMessage(NetworkStatus.NET_START, networkParam);
            handler.sendMessage(msg);

            int addType = networkParam.addType;

            if(addType == AddType.NET_INSERT2HEAD){//插入队列最前
                listSequence.add(0,task);
            }else if(addType ==AddType.NET_CANCEL_SAME){//取消相同
                this.cancel(networkParam);
            }else if(addType == AddType.NET_CANCEL_PRE){//取消之前队列所有请求
                this.cancelAll();
                listSequence.add(0,task);
            }else if(addType == AddType.NET_IN_ORDER){//按照顺序添加
                listSequence.add(task);
            }
        }

        tasksEnqueue();
    }

    private void tasksEnqueue(){

        synchronized (listSequence){
            Iterator<NetworkTask> listSequenceIterator = listSequence.iterator();

            while (listSequenceIterator.hasNext()){
                NetworkTask task = listSequenceIterator.next();
                listSequenceIterator.remove();
                if(task.param.memCache&& ResponseMemCache.containsKey(task.param)){
                    BaseResult result = ResponseMemCache.get(task.param);
                    if(task.handler!=null&&result!=null){
                        task.param.setResult(result);
                        Message msg = task.handler.obtainMessage(NetworkStatus.NET_COMPLETE, task.param);
                        task.handler.sendMessage(msg);
                    }
                }else{
                    if(task.param.diskCache&& ResponseDiskCache.containsKey(task.param)){
                        BaseResult result = ResponseDiskCache.get(task.param);
                        if(task.handler!=null&&result!=null){
                            task.param.setResult(result);
                            Message msg = task.handler.obtainMessage(NetworkStatus.CACHE_HIT, task.param);
                            task.handler.sendMessage(msg);
                        }
                    }
                    newCallByRequestType(task);
                }
            }
        }
    }

    private void newCallByRequestType(NetworkTask task){
        if(task == null||task.param == null||task.handler == null){
            return ;
        }
        NetworkParam networkParam = task.param;

        final IServiceMap key = networkParam.key;
        BaseParam param = networkParam.param;
        int requestType = networkParam.requestType;

        Request.Builder builder = new Request.Builder().
                url(key.getT()).
                tag(networkParam);

        if(requestType == RequestType.POST){
            FormBody.Builder formBuilder = new FormBody.Builder();

            Map<String, Object> tmp = (Map<String, Object>) com.alibaba.fastjson.JSON.toJSON(param);
            if(tmp!=null&&tmp.isEmpty()){
                Set<Map.Entry<String, Object>> entrySet = tmp.entrySet();
                Iterator<Map.Entry<String, Object>> set = entrySet.iterator();
                while (set.hasNext()){
                    Map.Entry<String, Object> var = set.next();
                    String k = var.getKey();
                    String v = String.valueOf(var.getValue());
                    formBuilder.add(k,v);
                }
            }

            FormBody formBody = formBuilder.build();
            builder.post(formBody);
        }else if(requestType == RequestType.POST_JSON){
            RequestBody requestBody = RequestBody.create(JSON, JSONObject.toJSONString(param));
            builder.post(requestBody);
        }else if(requestType == RequestType.GET){
            builder.get();
        }else if(requestType == RequestType.PUT){

        }

        okHttpClient.newCall(builder.build()).enqueue(new NetworkCallback(networkParam,task.handler));
    }

    public void cancel(NetworkParam networkParam){
        Iterator<NetworkTask> itt = listSequence.iterator();
        while (itt.hasNext()){
            NetworkTask networkTask = itt.next();
            NetworkParam tmp = networkTask.param;
            if(tmp.toString().equals(networkParam.toString())&&tmp.cancelAble){
                itt.remove();
            }
        }

        List<Call> runningCalls = okHttpClient.dispatcher().runningCalls();
        if(runningCalls!=null&&!runningCalls.isEmpty()){
            for (Call runCall:runningCalls){
                if(runCall.request().tag() instanceof NetworkParam){
                    NetworkParam runParam = (NetworkParam) runCall.request().tag();
                    if(runParam.key == networkParam.key&&runParam.cancelAble){
                        runCall.cancel();
                    }
                }
            }
        }

        List<Call> queuedCalls = okHttpClient.dispatcher().queuedCalls();
        if(queuedCalls!=null&&!queuedCalls.isEmpty()){
            for (Call queuedCall:queuedCalls){
                if(queuedCall.request().tag() instanceof NetworkParam){
                    NetworkParam queuedParam = (NetworkParam) queuedCall.request().tag();
                    if(queuedParam.key == networkParam.key&&queuedParam.cancelAble){
                        queuedCall.cancel();
                    }
                }
            }
        }
    }

    public void cancelAll(){
        listSequence.clear();
        List<Call> runningCalls = okHttpClient.dispatcher().runningCalls();
        if(runningCalls!=null&&!runningCalls.isEmpty()){
            for (Call runCall:runningCalls){
                runCall.cancel();
            }
        }

        List<Call> queuedCalls = okHttpClient.dispatcher().queuedCalls();
        if(queuedCalls!=null&&!queuedCalls.isEmpty()){
            for (Call queuedCall:queuedCalls){
                queuedCall.cancel();
            }
        }

    }

    public void cancelWithHandler(Handler handler){

        Iterator<NetworkTask> itt = listSequence.iterator();
        while (itt.hasNext()){
            NetworkTask tmp = itt.next();
            if(tmp.handler == handler){
                itt.remove();
            }
        }

        List<Call> runningCalls = okHttpClient.dispatcher().runningCalls();
        if(runningCalls!=null&&!runningCalls.isEmpty()){
            for (Call runCall:runningCalls){
                if(runCall instanceof NetworkCallback){
                    NetworkCallback var1 = (NetworkCallback) runCall;
                    if(var1.getHandler() == handler){
                        runCall.cancel();
                    }
                }
            }
        }


        List<Call> queuedCalls = okHttpClient.dispatcher().queuedCalls();
        if(queuedCalls!=null&&!queuedCalls.isEmpty()){
            for (Call queuedCall:queuedCalls){
                if(queuedCall instanceof NetworkCallback){
                    NetworkCallback var2 = (NetworkCallback) queuedCall;
                    if(var2.getHandler() == handler){
                        queuedCall.cancel();
                    }
                }
            }
        }

    }

    public void destroy() {
        if (singleInstance != null) {
            singleInstance.cancelAll();
        }
        singleInstance = null;
    }


    public void downLoad(String url , FileCallback callback){
        Request request = new Request.Builder().
                url(url).
                build();

        okHttpClient.newCall(request).enqueue(callback);
    }


}
