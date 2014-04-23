
package com.gizrak.ebook.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String PREF_NAME = "prefreneces_app_ebook";

    public static final SharedPreferences getSharedPrefrences(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        return pref;
    }
}
