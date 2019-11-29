package com.stone.wwe.engine;

import android.media.MediaPlayer;
import android.util.Log;

import com.stone.wwe.engine.interf.IEngine;
import com.stone.wwe.engine.interf.IWweCallback;

import java.io.IOException;

/**
 * Base calss of WWE(Wake Word Engine)
 * A new WWE should extend this class, and Users should use this class to use key word detect.
 * the relationship between WakeWordEngine ane XXXWakeWordEngine:
 * WakeWordEngine define the architecture for upper user and lower developer
 * XXXWakeWordEngine is a specific realize using a special library like snowboy and so on;
 * WakeWordEngine is for upper developer, add this to your logic.
 */
public abstract class WakeWordEngine implements IEngine, IWweCallback {
    private static final String TAG = WakeWordEngine.class.getName();

    protected IWweCallback mWweCallback;

    private MediaPlayer mMediaPlayer = null;

    public WakeWordEngine(){}

    public void setCallback(IWweCallback callback){
        this.mWweCallback = callback;
    }

    public void setNotifySoundResource(String resource){
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(resource);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetectError() {
        if (mWweCallback == null){
            Log.v(TAG, "key word detect error.....");
            return;
        }
        mWweCallback.onDetectError();
    }

    @Override
    public void onKeyWordDetect(int count) {
        if (mWweCallback == null){
            Log.v(TAG, "key word detect.....:" + count);
            return;
        }
        mWweCallback.onKeyWordDetect(count);

        if (mMediaPlayer != null){
            Log.v(TAG, "play ding..................................");
            mMediaPlayer.start();
        }
    }

    @Override
    public void onNoSpeech() {
        if (mWweCallback == null){
            Log.v(TAG, "no speech.....");
            return;
        }
        mWweCallback.onNoSpeech();
    }

    @Override
    public void onSpeeching() {
        if (mWweCallback == null){
            Log.v(TAG, "speeching but not detect keyword....");
            return;
        }
        mWweCallback.onSpeeching();
    }
}
