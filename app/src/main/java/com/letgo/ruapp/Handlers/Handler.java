package com.letgo.ruapp.Handlers;

import android.content.Context;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

public abstract class Handler {
    protected Context context;
    protected WebView view;
    protected String js;

    public Handler() {}
    public Handler(Context context, WebView view, String jsLoc) {
        this.context=context;
        this.view = view;
        js=loadJs(jsLoc);
    }


    private String loadJs(String js){
        String str = "";
        try {
            InputStream is = context.getAssets().open(js);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return str;
    }
    public abstract boolean isDone();
    public abstract void update(Object ... object);
}
