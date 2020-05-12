package com.letgo.ruapp.Services;

import android.webkit.CookieManager;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.CookieCache;
import com.franmontiel.persistentcookiejar.persistence.CookiePersistor;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;


public class MyPersistentCookieJar extends PersistentCookieJar {
    private CookieManager WebViewCookies = CookieManager.getInstance();

    private CookieCache cache;
    private CookiePersistor persistor;

    public MyPersistentCookieJar(CookieCache cache, CookiePersistor persistor) {
        super(cache, persistor);
    }

    @Override
    synchronized public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        String urlString = url.toString();

        for (Cookie cookie : cookies) {
            WebViewCookies.setCookie(urlString, cookie.toString());
        }
        super.saveFromResponse(url,cookies);
    }
}
