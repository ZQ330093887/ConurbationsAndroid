package com.test.admin.conurbations.utils;

import android.util.Log;
import android.view.View;

import com.test.admin.conurbations.activitys.BaseActivity;
import com.test.admin.conurbations.annotations.DynamicHandler;
import com.test.admin.conurbations.annotations.FindView;
import com.test.admin.conurbations.annotations.IDHelper;
import com.test.admin.conurbations.annotations.SetLayout;
import com.test.admin.conurbations.annotations.ViewNamingRuleConfig;
import com.test.admin.conurbations.annotations.events.ListenerMethod;
import com.test.admin.conurbations.annotations.events.OnCheck;
import com.test.admin.conurbations.annotations.events.OnClick;
import com.test.admin.conurbations.annotations.events.OnLongClick;
import com.test.admin.conurbations.annotations.events.OnRefresh;
import com.test.admin.conurbations.annotations.events.OnTouch;
import com.test.admin.conurbations.fragments.BaseFragment;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by waly6 on 2015/10/24.
 */
public class InjectUtil {

    private static final String TAG = "InjectUtil";

    private static List<Class<? extends Annotation>> methodAnnotations = AnnotationTypeListGeneration.create(
            OnClick.class,
            OnLongClick.class,
            OnRefresh.class,
            OnCheck.class,
            OnTouch.class);

    private static class AnnotationTypeListGeneration {
        @SafeVarargs
        public static List<Class<? extends Annotation>> create(Class<? extends Annotation>... classes) {
            List<Class<? extends Annotation>> list = new ArrayList<>();
            for (Class<? extends Annotation> cls : classes) {
                list.add(cls);
            }
            return list;
        }
    }

    public static void inject(Object object) {
        if (object instanceof BaseActivity) {
            injectActivity((BaseActivity) object);
        }
        else if (object instanceof BaseFragment) {
            injectFragment((BaseFragment) object);
        }
    }

    public static void injectActivity(BaseActivity baseActivity) {
        String activityName = StringUtil.deleteAllPackageName(baseActivity.getClass().getName());

        if (!StringUtil.isActivityName(activityName)) {
            Log.e(TAG, "Make sure the injected view is an activity.");
            return;
        }

        if (baseActivity.getClass().isAnnotationPresent(SetLayout.class)) {
            if (!baseActivity.getClass().getAnnotation(SetLayout.class).value()) {
                Log.d(TAG, activityName + " doesn't need to set layout automatically.");
                return;
            }

            String[] activityWords = StringUtil.splitCamelCase(activityName);
            String activityNameWithUnderline = StringUtil.concatStringsWithUnderline(activityWords, activityWords.length - 1);
            String layoutName = "activity_" + activityNameWithUnderline;

            baseActivity.setContentView(IDHelper.getLayout(baseActivity.getApplicationContext(), layoutName));
            Log.d(TAG, "Set " + activityName + "'s layout success.");

            Field[] fields = baseActivity.getClass().getDeclaredFields();

            try {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(FindView.class)) {
                        String fieldName = field.getName();
                        String viewNameAbbrev = StringUtil.getViewNameAbbrev(fieldName);
                        String idName;

                        if (!StringUtil.isEmpty(viewNameAbbrev)) {
                            idName = viewNameAbbrev + "_" + activityNameWithUnderline;
                        }
                        else {
                            String[] viewNameSplit = StringUtil.splitViewName(fieldName);

                            if (StringUtil.isEmpty(viewNameSplit[0]) || StringUtil.isEmpty(viewNameSplit[1])) {
                                Log.e(TAG, "Failed to parse the view name. " + fieldName + " doesn't comply with the ViewNamingRule.");
                                continue;
                            }

                            idName = viewNameSplit[1] + "_" + activityNameWithUnderline + "_" + StringUtil.camelCaseToUnderlineRemoveIndex(viewNameSplit[0]);
                        }

                        View view = baseActivity.findViewById(IDHelper.getViewId(baseActivity.getApplicationContext(), idName));

                        field.setAccessible(true);
                        field.set(baseActivity, view);
                        baseActivity.views.put(fieldName, view);

                        Log.d(TAG, "Success to initial view: " + fieldName + ".");
                    }
                }
            }
            catch (IllegalAccessException e) {
                Log.e(TAG, "Failed to inject " + activityName + ". Illegal access.");
                e.printStackTrace();
            }
            catch (Exception e) {
                CommonUtil.exit();
            }

            Method[] methods = baseActivity.getClass().getDeclaredMethods();

            for (Method method : methods) {
                for (Class<? extends Annotation> methodAnnotation : methodAnnotations) {
                    if (method.isAnnotationPresent(methodAnnotation)) {
                        method.setAccessible(true);

                        try {
                            Annotation annotation = method.getAnnotation(methodAnnotation);
                            String[] viewNames = (String[]) annotation.annotationType().getDeclaredMethod("value").invoke(annotation);
                            String listenerMethod = annotation.annotationType().getAnnotation(ListenerMethod.class).value();
                            String listenerClassName;

                            if (StringUtil.isEmpty(listenerMethod)) {
                                listenerClassName = "android.view.View$" + StringUtil.deleteAllPackageName(methodAnnotation.getName()) + "Listener";
                            }
                            else {
                                listenerClassName = listenerMethod;
                            }

                            Class listenerClass = Class.forName(listenerClassName);
                            Object proxy = Proxy.newProxyInstance(listenerClass.getClassLoader(), new Class[]{listenerClass}, new DynamicHandler(baseActivity, method));
                            String setListenerMethodName = "set" + StringUtil.toBigCamelCase(StringUtil.getInnerClassSimpleName(listenerClassName));

                            for (String viewName : viewNames) {
                                Object view = baseActivity.views.get(viewName);
                                Method Method = view.getClass().getMethod(setListenerMethodName, listenerClass);
                                Method.invoke(view, proxy);
                                Log.d(TAG, "Success to bind method " + method.getName() + " on " + viewName + ".");
                            }
                        }
                        catch (ClassNotFoundException e) {
                            Log.e(TAG, "Class not found while using Class.forName().");
                        } catch (NoSuchMethodException e) {
                            Log.e(TAG, "Method not found in class.");
                        } catch (IllegalAccessException e) {
                            Log.e(TAG, "Failed to access field or method.");
                        } catch (InvocationTargetException e) {
                            Log.e(TAG, "Failed to invoke method on target.");
                        }
                    }
                }
            }
        }
    }

    private static void injectFragment(BaseFragment baseFragment) {
        String fragmentName = StringUtil.deleteAllPackageName(baseFragment.getClass().getName());

        if (!StringUtil.isFragmentName(fragmentName)) {
            Log.e(TAG, "Make sure the injected view is an fragment.");
            return;
        }

        if (baseFragment.getClass().isAnnotationPresent(SetLayout.class)) {
            if (!baseFragment.getClass().getAnnotation(SetLayout.class).value()) {
                Log.d(TAG, fragmentName + " doesn't need to set layout automatically.");
                return;
            }

            String[] fragmentWords = StringUtil.splitCamelCase(fragmentName);
            String fragmentNameWithUnderline = StringUtil.concatStringsWithUnderline(fragmentWords, fragmentWords.length - 1);
            String layoutName = "fragment_" + fragmentNameWithUnderline;

            baseFragment.setRootView(IDHelper.getLayout(baseFragment.getApplicationContext(), layoutName));
            Log.d(TAG, "Set " + fragmentName + "'s layout success.");

            Field[] fields = baseFragment.getClass().getDeclaredFields();

            try {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(FindView.class)) {
                        String fieldName = field.getName();
                        String viewNameAbbrev = StringUtil.getViewNameAbbrev(fieldName);
                        String idName;

                        if (!StringUtil.isEmpty(viewNameAbbrev)) {
                            idName = viewNameAbbrev + "_" + fragmentNameWithUnderline;
                        }
                        else {
                            String[] viewNameSplit = StringUtil.splitViewName(fieldName);

                            if (StringUtil.isEmpty(viewNameSplit[0]) || StringUtil.isEmpty(viewNameSplit[1])) {
                                Log.e(TAG, "Failed to parse the view name. " + fieldName + " doesn't comply with the ViewNamingRule.");
                                continue;
                            }

                            idName = viewNameSplit[1] + "_" + fragmentNameWithUnderline + "_" + StringUtil.camelCaseToUnderlineRemoveIndex(viewNameSplit[0]);
                        }

                        View view = baseFragment.getRootView().findViewById(IDHelper.getViewId(baseFragment.getApplicationContext(), idName));

                        field.setAccessible(true);
                        field.set(baseFragment, view);
                        baseFragment.views.put(fieldName, view);

                        Log.d(TAG, "Success to initial view: " + fieldName + ".");
                    }
                }
            }
            catch (IllegalAccessException e) {
                Log.e(TAG, "Failed to inject " + fragmentName + ". Illegal access.");
                e.printStackTrace();
            }
            catch (Exception e) {
                CommonUtil.exit();
            }

            Method[] methods = baseFragment.getClass().getDeclaredMethods();

            for (Method method : methods) {
                for (Class<? extends Annotation> methodAnnotation : methodAnnotations) {
                    if (method.isAnnotationPresent(methodAnnotation)) {
                        method.setAccessible(true);

                        try {
                            Annotation annotation = method.getAnnotation(methodAnnotation);
                            String[] viewNames = (String[]) annotation.annotationType().getDeclaredMethod("value").invoke(annotation);
                            String listenerMethod = annotation.annotationType().getAnnotation(ListenerMethod.class).value();
                            String listenerClassName;

                            if (StringUtil.isEmpty(listenerMethod)) {
                                listenerClassName = "android.view.View$" + StringUtil.deleteAllPackageName(methodAnnotation.getName()) + "Listener";
                            }
                            else {
                                listenerClassName = listenerMethod;
                            }

                            Class listenerClass = Class.forName(listenerClassName);
                            Object proxy = Proxy.newProxyInstance(listenerClass.getClassLoader(), new Class[]{listenerClass}, new DynamicHandler(baseFragment, method));
                            String setListenerMethodName = "set" + StringUtil.toBigCamelCase(StringUtil.getInnerClassSimpleName(listenerClassName));

                            for (String viewName : viewNames) {
                                Object view = baseFragment.views.get(viewName);
                                Method Method = view.getClass().getMethod(setListenerMethodName, listenerClass);
                                Method.invoke(view, proxy);
                                Log.d(TAG, "Success to bind method " + method.getName() + " on " + viewName + ".");
                            }
                        }
                        catch (ClassNotFoundException e) {
                            Log.e(TAG, "Class not found while using Class.forName().");
                        } catch (NoSuchMethodException e) {
                            Log.e(TAG, "Method not found in class.");
                        } catch (IllegalAccessException e) {
                            Log.e(TAG, "Failed to access field or method.");
                        } catch (InvocationTargetException e) {
                            Log.e(TAG, "Failed to invoke method on target.");
                        }
                    }
                }
            }
        }
    }

    public static View injectViews(BaseActivity baseActivity, String layoutName, String... viewsName) {
        View container = View.inflate(baseActivity.getContext(), IDHelper.getLayout(baseActivity.getApplicationContext(), layoutName), null);

        String[] layoutNameWords = layoutName.split("_");
        String containerName = "";

        if (layoutNameWords.length < 2) {
            Log.d(TAG, "Invalid layout name: " + layoutName);
            return container;
        }

        for (String string : ViewNamingRuleConfig.CONTAINERS) {
            if (string.toLowerCase().equals(layoutNameWords[0])) {
                containerName = layoutNameWords[0];
                break;
            }
        }

        if (StringUtil.isEmpty(containerName)) {
            Log.d(TAG, "Invalid layout name: " + layoutName);
            return container;
        }

        Field[] fields = baseActivity.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                String fieldName = field.getName();
                boolean isParam = false;

                for (String string : viewsName) {
                    if (fieldName.equals(string)) {
                        isParam = true;
                        break;
                    }
                }

                if (isParam) {
                    String[] viewNameSplit = StringUtil.splitViewName(fieldName);

                    if (StringUtil.isEmpty(viewNameSplit[0]) || StringUtil.isEmpty(viewNameSplit[1])) {
                        Log.e(TAG, "Failed to parse the view name. " + fieldName + " doesn't comply with the ViewNamingRule.");
                        continue;
                    }

                    String idName = viewNameSplit[1] + "_" +
                            StringUtil.concatStringsWithUnderline(layoutNameWords, 1, layoutNameWords.length) +
                            "_" + StringUtil.camelCaseToUnderline(viewNameSplit[0]);

                    field.setAccessible(true);
                    field.set(baseActivity, container.findViewById(IDHelper.getViewId(baseActivity.getApplicationContext(), idName)));
                    Log.d(TAG, "Success to initial view: " + fieldName + ".");
                }
            }
        }
        catch (IllegalAccessException e) {
            Log.e(TAG, "Failed to inject " + StringUtil.deleteAllPackageName(baseActivity.getClass().getName()) + ". Illegal Access.");
            e.printStackTrace();
        }

        return container;
    }
}
