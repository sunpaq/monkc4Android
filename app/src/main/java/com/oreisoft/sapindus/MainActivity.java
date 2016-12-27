package com.oreisoft.sapindus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    BEEngineView mView = null;

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mView = new BEEngineView(getApplicationContext());
        setContentView(mView);

        //setContentView(R.layout.activity_main);
        //mView = (BEEngineView) findViewById(R.id.gl_container);
    }

    @Override protected void onNewIntent(Intent i) {
        super.onNewIntent(i);
    }

    @Override protected void onPause() {
        //workaround for save GL context when pause/resume
        mView.setVisibility(View.GONE);
        super.onPause();
    }

    @Override protected void onResume() {
        super.onResume();
        //hideSystemUI();
        mView.onResume();
    }

    //workaround for save GL context when pause/resume
    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mView.getVisibility() == View.GONE) {
            mView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mView.onTouchEvent(e);
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
