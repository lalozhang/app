package com.tequila.callbacks;

import android.app.Activity;
import android.content.Context;

/**
 * Created by admin on 2017/3/4.
 */

public interface ActFragImpl {

    void hStartActivity(Class<? extends Activity> activity);

    void hStartActivityForResult(Class<? extends Activity> activity ,int requestCode);

    Context getContext();




}
