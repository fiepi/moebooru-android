package com.fiepi.moebooru.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;


/**
 * Created by fiepi on 11/22/17.
 */

@GlideModule
public final class MoeGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        // default
//        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
        int memoryCacheSizeBytes = 1024 * 1024 * 512;
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        int diskCacheSizeBytes = 1024 * 1024 * 512; // 512 MB
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL));

    }
}
