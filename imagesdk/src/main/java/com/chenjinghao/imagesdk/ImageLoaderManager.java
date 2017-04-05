package com.chenjinghao.imagesdk;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * 初始化UniverImagerLoader，并用来加载图片
 * Created by chenjinghao on 2017/4/5.
 */

public class ImageLoaderManager {
    /**
     * 默认的参数配置
     */
    private static final int THREAD_COUNT = 4;
    private static final int PRIORITY = 2;
    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private static final int CONNECTION_TIME_OUT = 5 * 1000;
    private static final int READ_TIME_OUT = 30 * 1000;

    private static ImageLoaderManager mInstance = null;
    private static ImageLoader mImagerLoader = null;
    private DisplayImageOptions defaultOption;

    private ImageLoaderManager(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(THREAD_COUNT)//设置图片下载的线程
                .threadPriority(Thread.NORM_PRIORITY - PRIORITY)//设置图片下载的优先级比系统默认优先级的略低
                .denyCacheImageMultipleSizesInMemory()//禁止缓存不同的尺寸到内存中
                .memoryCache(new WeakMemoryCache())//使用弱引用内存缓存
                .diskCacheSize(DISK_CACHE_SIZE)//分配磁盘缓存大小
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//使用MD5命名文件
                .tasksProcessingOrder(QueueProcessingType.LIFO)//图片下载顺序根据队列为后进先出
                .defaultDisplayImageOptions(getDefaultOption())//使用默认的图片加载options
                //设置图片下载器
                .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT, READ_TIME_OUT))
                .writeDebugLogs()//debug环境下会输出日志
                .build();
        ImageLoader.getInstance().init(configuration);
        mImagerLoader = ImageLoader.getInstance();
    }

    private static ImageLoaderManager getInstance(Context context){
        if (mInstance == null){
            synchronized (ImageLoaderManager.class){
                if (mInstance == null){
                    mInstance = new ImageLoaderManager(context);
                }
            }
        }
        return mInstance;
    }

    public DisplayImageOptions getDefaultOption() {

        return defaultOption;
    }
}
