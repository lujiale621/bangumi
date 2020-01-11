package com.lc.bangumidemo.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;

import com.lc.bangumidemo.Myreadview.Constants;
import com.lc.bangumidemo.Myreadview.LoadBitmapTask;
import com.lc.bangumidemo.Myreadview.PageFlipView;
import com.lc.bangumidemo.R;

public class FlipReadActivity extends Activity implements GestureDetector.OnGestureListener {
    PageFlipView mPageFlipView;
    GestureDetector mGestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageFlipView = new PageFlipView(this);
        setContentView(mPageFlipView);
        mGestureDetector = new GestureDetector(this, this);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            mPageFlipView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }
    private void showAbout() {
        View aboutView = getLayoutInflater().inflate(R.layout.about, null,
                false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setView(aboutView);
        builder.create();
        builder.show();
    }
    @Override
    public boolean onDown(MotionEvent e) {
        mPageFlipView.onFingerDown(e.getX(), e.getY());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mPageFlipView.onFingerMove(e2.getX(), e2.getY());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenus, menu);

        int duration = mPageFlipView.getAnimateDuration();
        if (duration == 1000) {
            menu.findItem(R.id.animation_1s).setChecked(true);
        }
        else if (duration == 2000) {
            menu.findItem(R.id.animation_2s).setChecked(true);
        }
        else if (duration == 5000) {
            menu.findItem(R.id.animation_5s).setChecked(true);
        }

        if (mPageFlipView.isAutoPageEnabled()) {
            menu.findItem(R.id.auoto_page).setChecked(true);
        }
        else {
            menu.findItem(R.id.single_page).setChecked(true);
        }

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        int pixels = pref.getInt("MeshPixels", mPageFlipView.getPixelsOfMesh());
        switch (pixels) {
            case 2:
                menu.findItem(R.id.mesh_2p).setChecked(true);
                break;
            case 5:
                menu.findItem(R.id.mesh_5p).setChecked(true);
                break;
            case 10:
                menu.findItem(R.id.mesh_10p).setChecked(true);
                break;
            case 20:
                menu.findItem(R.id.mesh_20p).setChecked(true);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean isHandled = true;
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        switch (item.getItemId()) {
            case R.id.animation_1s:
                mPageFlipView.setAnimateDuration(1000);
                editor.putInt(Constants.PREF_DURATION, 1000);
                break;
            case R.id.animation_2s:
                mPageFlipView.setAnimateDuration(2000);
                editor.putInt(Constants.PREF_DURATION, 2000);
                break;
            case R.id.animation_5s:
                mPageFlipView.setAnimateDuration(5000);
                editor.putInt(Constants.PREF_DURATION, 5000);
                break;
            case R.id.auoto_page:
                mPageFlipView.enableAutoPage(true);
                editor.putBoolean(Constants.PREF_PAGE_MODE, true);
                break;
            case R.id.single_page:
                mPageFlipView.enableAutoPage(false);
                editor.putBoolean(Constants.PREF_PAGE_MODE, false);
                break;
            case R.id.mesh_2p:
                editor.putInt(Constants.PREF_MESH_PIXELS, 2);
                break;
            case R.id.mesh_5p:
                editor.putInt(Constants.PREF_MESH_PIXELS, 5);
                break;
            case R.id.mesh_10p:
                editor.putInt(Constants.PREF_MESH_PIXELS, 10);
                break;
            case R.id.mesh_20p:
                editor.putInt(Constants.PREF_MESH_PIXELS, 20);
                break;
            case R.id.about_menu:
                showAbout();
                return true;
            default:
                isHandled = false;
                break;
        }

        if (isHandled) {
            item.setChecked(true);
            editor.apply();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mPageFlipView.onFingerUp(event.getX(), event.getY());
            return true;
    }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPageFlipView.onPause();
        LoadBitmapTask.get(this).stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadBitmapTask.get(this).start();
        mPageFlipView.onResume();
    }
}
