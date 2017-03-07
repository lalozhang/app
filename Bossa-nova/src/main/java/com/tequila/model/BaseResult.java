package com.tequila.model;

import java.io.Serializable;

/**
 * Created by lalo on 2017/3/4.
 */

public class BaseResult implements Serializable {

    public int code;

    public int errorLevel = -1;

    public String msg;

    public static class BaseData implements Serializable{

    }
}
