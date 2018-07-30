package com.alice.core.util;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity
 * Created by yuzhijun on 2017/6/16.
 */
public class ActivityStack {
    private static Stack<Activity> activityStack;
    private static ActivityStack instance;

    private ActivityStack() {
    }

    /**

     */
    public static ActivityStack create() {
        if (instance == null) {
            instance = new ActivityStack();
        }
        return instance;
    }

    /**

     */
    public void add(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**

     */
    public void remove(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.remove(activity);
    }

    /**

     */
    public Activity top() {
        return activityStack.lastElement();
    }

    /**

     */
    public void finish() {
        Activity activity = activityStack.lastElement();
        finish(activity);
    }

    /**

     */
    public void finish(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**

     */
    public void finish(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finish(activity);
                break;
            }
        }
    }

    /**

     */
    public void finishAll() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            Activity activity = activityStack.get(i);
            if (null != activity && !activity.isFinishing()) {
                activity.finish();
                activity = null;
            }
        }
        activityStack.clear();
    }

    /**

     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }

    /**

     */
    public void appExit() {
        try {
            finishAll();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
