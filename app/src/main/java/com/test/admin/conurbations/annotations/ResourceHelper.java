package com.test.admin.conurbations.annotations;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by waly6 on 2015/10/24.
 */
public class ResourceHelper {

    private static final String TAG = "ResourceHelper";

    private static ResourceHelper resourceHelper;
    private static String packageName;
    private static Class layoutClass;
    private static Class drawableClass;
    private static Class idClass;
    private static Class stringClass;
    private static Class attrClass;
    private static Class colorClass;

    public static ResourceHelper getInstance(Context context) {
        if (resourceHelper == null) {
            synchronized (ResourceHelper.class) {
                packageName = context.getPackageName();
                resourceHelper = new ResourceHelper();
            }
        }
        return resourceHelper;
    }

    private ResourceHelper() {
        try {
            layoutClass = Class.forName(packageName + ".R$layout");
            drawableClass = Class.forName(packageName + ".R$drawable");
            idClass = Class.forName(packageName + ".R$id");
            stringClass = Class.forName(packageName + ".R$string");
            attrClass = Class.forName(packageName + ".R$attr");
            colorClass = Class.forName(packageName + ".R$color");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int getResId(Class cls, String resName) {
        if (cls == null) {
            Log.e(TAG, "Resource Class is not initialized.");
        }
        else {
            try {
                Field field = cls.getField(resName);
                return field.getInt(resName);
            }
            catch (Exception e) {
                Log.e(TAG, " Resource not found: " + resName + ".");
            }
        }
        return -1;
    }

    public int getLayoutId(String resName) {
        return getResId(layoutClass, resName);
    }

    public int getDrawableId(String resName) {
        return getResId(drawableClass, resName);
    }

    public int getViewId(String resName) {
        return getResId(idClass, resName);
    }

    public int getStringId(String resName) {
        return getResId(stringClass, resName);
    }

    public int getAttrId(String resName) {
        return getResId(attrClass, resName);
    }

    public int getColorId(String resName) {
        return getResId(colorClass, resName);
    }
}
