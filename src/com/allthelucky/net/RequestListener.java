package com.allthelucky.net;

public interface RequestListener {
    public final static int OK = 0;
    public final static int ERR = 1;

    void onStart();

    void onCompleted(byte[] data, int statusCode, String description, int actionId);
}
