package com.tequila;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tequila.callbacks.HandlerCallbacks;
import com.tequila.callbacks.NetworkListener;
import com.tequila.net.NetworkParam;

/**
 * Created by admin on 2017/3/7.
 */

public class BaseFragment extends Fragment implements NetworkListener {

    protected Handler mHandler ;
    protected HandlerCallbacks.FragmentCallback fcb;
    protected Bundle mBundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(fcb = new HandlerCallbacks.FragmentCallback(this, genCallback()));
        mBundle = savedInstanceState == null?getArguments():savedInstanceState;
        if(mBundle==null){
            mBundle = new Bundle();
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = createView();
        onViewCreated(root);
        return root;
    }

    protected void onViewCreated(View root) {

    }

    protected View createView() {
        return null;
    }

    public Handler.Callback genCallback(){
        return null;
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
    public void onSaveInstanceState(Bundle outState) {
        if(mBundle!=null){
            outState.putAll(mBundle);
        }
        super.onSaveInstanceState(outState);
    }
}
