package com.example.taurus.bihu.utils;

/**
 * Created by Taurus on 2018/2/21.
 * 返回体数据储存
 */

public class Response {
    private int mStatus;
    private String mInfo;
    private String mData;
    private String mToken;


    Response(byte[] response){
        String res = new String(response);
        mStatus = JsonUtil.getStatus(res);
        if(JsonUtil.getString(res,"info") != null){
            mInfo = JsonUtil.getString(res,"info");
        }else{
            mInfo = null;
        }
        if(mStatus==200&&JsonUtil.getString(res,"data") != null){
            mData = JsonUtil.getString(res,"data");
        }else mData = null;

    }

    Response(byte[] respense,int anyNumber){
        String res = new String(respense);
        mStatus = JsonUtil.getStatus(res);
        if(JsonUtil.getString(res,"info")!=null){
            mInfo = JsonUtil.getString(res,"info");
        }else{
            mInfo = null;
        }
        if(mStatus==200&&JsonUtil.getString(res,"token")!=null){
            mToken = JsonUtil.getString(res,"token");
        }else{
            mToken = null;
        }
    }
    public boolean isSuccess(){
        return mStatus==200;
    }

    public String getmToken(){
        return mToken;
    }

    public int getmStatus() {
        return mStatus;
    }

    public String getmInfo() {
        return mInfo;
    }

    public String getmData() {
        return mData;
    }
}
