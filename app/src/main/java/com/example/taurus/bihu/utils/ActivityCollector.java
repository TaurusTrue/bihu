package com.example.taurus.bihu.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2018/2/20.
 * Activity的集合类
 */

public class ActivityCollector {


    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }
}

