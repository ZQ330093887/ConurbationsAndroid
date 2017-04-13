package com.test.admin.conurbations.annotations;

import android.content.Context;

/**
 * Created by waly6 on 2015/10/24.
 */
public class IDHelper {

    public static int getLayout(Context context, String layoutName) {
        return ResourceHelper.getInstance(context).getLayoutId(layoutName);
    }

    public static int getViewId(Context context, String idName) {
        return ResourceHelper.getInstance(context).getViewId(idName);
    }

    public static int getDrawable(Context context, String drawableName) {
        return ResourceHelper.getInstance(context).getDrawableId(drawableName);
    }

    public static int getString(Context context, String stringName) {
        return ResourceHelper.getInstance(context).getStringId(stringName);
    }

    public static int getAttr(Context context, String attrName) {
        return ResourceHelper.getInstance(context).getAttrId(attrName);
    }

    public static int getColor(Context context, String colorName) {
        return ResourceHelper.getInstance(context).getColorId(colorName);
    }
}
