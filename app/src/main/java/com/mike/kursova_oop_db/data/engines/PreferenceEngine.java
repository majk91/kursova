package com.mike.kursova_oop_db.data.engines;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceEngine {

    private final String PREFERENCE_NAME = "PosAppPreferences";
    private final String STORE_USER_ID = "Store1";
    private final String STORE_USER_EMAIL = "Store2";
    private final String STORE_USER_NAME = "Store3";
    private final String STORE_USER_ROLE = "Store4";

    private PreferenceEngine() {}

    private static class Holder {
        static final PreferenceEngine INSTANCE = new PreferenceEngine();
    }

    public static PreferenceEngine getInstance() {
        return Holder.INSTANCE;
    }

    public void storeUserId(final Context context, final Long userId) {
        storeLong(context, STORE_USER_ID, userId);
    }
    public void clearUser(final Context context) {
        storeLong(context, STORE_USER_ID, -1L);
        storeString(context, STORE_USER_NAME, "");
        storeString(context, STORE_USER_EMAIL, "");
    }

    public Long getUserId(final Context context) {
        return getStoredLong(context, STORE_USER_ID);
    }

    public void storeUserName(final Context context, final String userName) {
        storeString(context, STORE_USER_NAME, userName);
    }

    public String getUserName(final Context context) {
        return getString(context, STORE_USER_NAME);
    }

    public void storeUserEmail(final Context context, final String userName) {
        storeString(context, STORE_USER_EMAIL, userName);
    }

    public String getUserEmail(final Context context) {
        return getString(context, STORE_USER_EMAIL);
    }

    public void storeUserRole(final Context context, final String userRole) {
        storeString(context, STORE_USER_ROLE, userRole);
    }

    public String getUserRole(final Context context) {
        if(getString(context, STORE_USER_ROLE) == null || getString(context, STORE_USER_ROLE).equals(""))
            return "GUEST";
        else
            return getString(context, STORE_USER_ROLE);
    }

    private void storeString(final Context context, final String key, final String value) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (preferences != null) {
            preferences.edit().putString(key, value).apply();
        }
    }

    private String getString(final Context context, final String key) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getString(key, "");
        }
        return "";
    }

    private void storeBoolean(final Context context, final String key, final boolean value) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (preferences != null) {
            preferences.edit().putBoolean(key, value).apply();
        }
    }

    private boolean getBoolean(final Context context, final String key) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getBoolean(key, true);
        }
        return true;
    }

    private boolean getBoolean(final Context context, final String key, final Boolean defaultValue) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getBoolean(key, defaultValue);
        }
        return true;
    }

    private void storeInt(final Context context, final String key, final int value) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (preferences != null) {
            preferences.edit().putInt(key, value).apply();
        }
    }

    private int getStoredInt(final Context context, final String key) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getInt(key, 0);
        }
        return 0;
    }

    private void storeLong(final Context context, final String key, final Long value) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (preferences != null) {
            preferences.edit().putLong(key, value).apply();
        }
    }

    private Long getStoredLong(final Context context, final String key) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getLong(key, 0L);
        }
        return 0L;
    }

    private void storeFloat(final Context context, final String key, final float value) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (preferences != null) {
            preferences.edit().putFloat(key, value).apply();
        }
    }

    private float getStoredFloat(final Context context, final String key) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getFloat(key, 0);
        }
        return 0;
    }



}
