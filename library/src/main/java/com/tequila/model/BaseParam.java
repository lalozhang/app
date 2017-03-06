package com.tequila.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by admin on 2017/3/4.
 */

public class BaseParam implements Serializable{



    public String hashKey(){
        String cacheKey = null;
        try{
            cacheKey = JSONObject.toJSONString(this);
        }catch(Exception e){

        }
        return cacheKey;
    }
}
