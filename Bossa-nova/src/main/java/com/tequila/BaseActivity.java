package com.tequila;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.tequila.callbacks.ActFragImpl;
import com.tequila.callbacks.HandlerCallbacks;
import com.tequila.callbacks.NetworkListener;
import com.tequila.net.Blues;
import com.tequila.net.NetworkParam;

/**
 * Created by lalo on 2017/3/4.
 */

public abstract class BaseActivity extends AppCompatActivity implements

                                                                        View.OnClickListener,
                                                                        ActFragImpl,
                                                                        NetworkListener {

    public static final String EXTRA_FROM_ACTIVITY = "__FROM_ACTIVITY__";
    protected Bundle mBundle;
    protected Handler mHandler ;
    private HandlerCallbacks.ActivityCallback acb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler(acb = new HandlerCallbacks.ActivityCallback(genCallback(),this));

        mBundle = savedInstanceState == null?getIntent().getExtras():savedInstanceState;
        if(mBundle==null){
            mBundle = new Bundle();
        }
    }


    @Override
    public void onClick(View view) {

    }

    protected Handler.Callback genCallback(){
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mBundle!=null){
            outState.putAll(mBundle);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNetworkStart(NetworkParam param) {

    }

    @Override
    public void onNetworkRunning(NetworkParam param) {

    }

    @Override
    public void onNetworkComplete(NetworkParam param) {

    }

    @Override
    public void onError(NetworkParam param) {

    }

    @Override
    public void onCache(NetworkParam param) {

    }


    @Override
    public void finish() {
        super.finish();
        Blues.cancelWithHandler(mHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Blues.cancelWithHandler(mHandler);
        if(acb!=null){
            acb.removeCalback();
        }

    }

    @Override
    public void hStartActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this,clazz);
        intent.putExtras(mBundle);
        intent.putExtra(EXTRA_FROM_ACTIVITY,this.getClass().getSimpleName());
        startActivity(intent);
    }

    @Override
    public void hStartActivityForResult(Class<? extends Activity> clazz, int requestCode) {
        Intent intent = new Intent(this,clazz);
        intent.putExtras(mBundle);
        intent.putExtra(EXTRA_FROM_ACTIVITY,this.getClass().getSimpleName());
        startActivityForResult(intent,requestCode);
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * Return if the activity is that invoked this activity. This is who the data in setResult() will be sent to.
     * Note: if the calling activity is not expecting a result (that is it did not use the startActivityForResult form
     * that includes a request code), then this method will return false
     * @param clazz
     * @return
     */
    public boolean fromActivity(Class<? extends Activity> clazz) {
        try {
            return clazz.getName().equals(((Activity) getContext()).getCallingActivity().getClassName());
        } catch (Exception e) {
            return false;
        }
    }
}

