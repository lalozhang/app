package com.tequila;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tequila.callbacks.ActFragImpl;

/**
 * Created by admin on 2017/3/4.
 */

public abstract class BaseActivity extends AppCompatActivity implements
                                                                        View.OnClickListener,
                                                                        ActFragImpl{

    protected Bundle mBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onClick(View view) {

    }
}

