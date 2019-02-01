
package com.eladapp.elachat.chat;

import android.app.Activity;
import android.view.View;

/**
 * author:why
 * created on: 2018/9/11 13:34
 * description:
 */
public class Commontools {

    /**
     * to controll the visibility of the Activity's navigator bar
     * @param activity
     */
    public static void setNavbarVisibility(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}