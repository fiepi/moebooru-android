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
import com.fiepi.moebooru.AppConfig;
import com.fiepi.moebooru.util.SharedPreferencesUtils;

/**
 * Created by fiepi on 11/22/17.
 */

@GlideModule
public final class MoeGlideModule extends AppGlideModule {
    private static final String TAG = MoeGlideModule.class.getSimpleName();

    private static final String mPref = "settings";
    private static final String mCacheMemory = "cache_memory";
    private static final String mCacheDisk = "cache_disk";

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        // default
//        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
        Integer m = new SharedPreferencesUtils().getIntValue(mPref, mCacheMemory);
        if (m == 0){
            m = 256;
        }
        int memoryCacheSizeBytes = 1024 * 1024 * m;
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));

        Integer d = new SharedPreferencesUtils().getIntValue(mPref, mCacheDisk);
        if (d == 0){
            d = 256;
        }
        int diskCacheSizeBytes = 1024 * 1024 * d;
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL));

    }
}
