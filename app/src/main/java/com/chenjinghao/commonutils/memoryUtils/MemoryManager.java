package com.chenjinghao.commonutils.memoryUtils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by chenjinghao on 2017/3/26.
 */

public class MemoryManager {
    public static int getCommonMemoryValue(Context context){
        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
    }

    public static int getLargeMemoryValue(Context context){
        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getLargeMemoryClass();
    }
}
