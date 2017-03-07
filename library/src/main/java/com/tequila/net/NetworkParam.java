package com.tequila.net;

import com.tequila.AddType;
import com.tequila.IServiceMap;
import com.tequila.cache.disk.Utils;
import com.tequila.model.BaseParam;
import com.tequila.model.BaseResult;
import com.tequila.util.ErrorCode;

/**
 * Created by admin on 2017/3/4.
 */

public class NetworkParam {

    public IServiceMap key;
    public BaseParam param;
    private BaseResult result;
    public int addType = AddType.NET_IN_ORDER;
    public int errorCode = ErrorCode.SERVER_ERROR;
    public boolean memCache;//内存缓存
    public boolean diskCache;//sdCard缓存
    public int requestType;

    public NetworkParam(IServiceMap serviceMap, BaseParam param) {
        if(serviceMap==null){
            throw new NullPointerException("NetworkParam key can not be null");
        }
        if(param==null){
            param = new BaseParam();
        }
        this.key = serviceMap;
        this.param = param;
    }

    public BaseResult getResult() {
        return result;
    }

    public void setResult(BaseResult result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NetworkParam other = (NetworkParam) obj;
        if (!this.key.equals(other.key)) {
            return false;
        }
        if (this.param == null) {
            if (other.param != null) {
                return false;
            }
        } else if (!this.param.equals(other.param)) {
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.key == null ? 0 : this.key.hashCode());
        result = prime * result + (this.param == null ? 0 : this.param.hashCode());
        return result;
    }

    public String newCachKey(){
        return String.format("NetworkParam [key=%1s, cacheKey=%2s]", this.key, this.param.newCacheKey());
    }





}
