package com.tequila.net;

import com.tequila.model.BaseResult;

/**
 * Created by lalo on 2017/3/4.
 */

public interface IServiceMap {

    String getT();
    Class<? extends BaseResult> getClazz();
    int getType();
}
