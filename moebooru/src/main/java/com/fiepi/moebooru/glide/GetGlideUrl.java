package com.fiepi.moebooru.glide;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.fiepi.moebooru.AppConfig;

/**
 * Created by fiepi on 11/24/17.
 */

public class GetGlideUrl {
    public GetGlideUrl(){

    }
    public GlideUrl makeGlideUrl(String url){
        LazyHeaders.Builder builder = new LazyHeaders.Builder()
                .addHeader(AppConfig.HEADER_USER_AGENT, AppConfig.HEADER_USER_AGENT_INFO);
        return new GlideUrl(url, builder.build());
    }
}