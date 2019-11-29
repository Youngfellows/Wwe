package com.stone.wwe;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.stone.wwe.engine.SnowboyWakeWordEngine;
import com.stone.wwe.engine.WakeWordEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainService extends Service {

    private static final String TAG = MainService.class.getName();

    public static final String SP_NAME = "wwe.xml";
    public static final String SP_KEY_INIT_DATA = "need_init_data";

    private String BASE_DIR;
    private String ALEXA_UMDL;
    private String COMMON_RES;


    private WakeWordEngine mWakeWordEngine;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        // running in the background, avoid to be killed
        startForeground(1003, new Notification.Builder(this, "WWE").build());

        // get common resource and alexa model
        BASE_DIR = getDataDir().getAbsolutePath() + File.separator + "files/snowboy";
        ALEXA_UMDL = BASE_DIR + File.separator + "alexa.umdl";
        COMMON_RES = BASE_DIR + File.separator + "common.res";

        // init data
        if (needInitData()){
            initData();
        }

        mWakeWordEngine = new SnowboyWakeWordEngine(COMMON_RES, ALEXA_UMDL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startKeywordDetect();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startKeywordDetect(){
        if (mWakeWordEngine == null){
            return;
        }

        Log.v(TAG, "start keyword detect .......");
        mWakeWordEngine.startDetection();
    }

    private void stopKeywordDetect(){
        if (mWakeWordEngine == null){
            return;
        }

        Log.v(TAG, "stop keyword detect .......");
        mWakeWordEngine.stopDetection();
    }

    private boolean needInitData(){
        SharedPreferences sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        return sp.getBoolean(SP_KEY_INIT_DATA, true);
    }

    private void initData(){
        Log.v(TAG, "init data ....");
        copyAssetsToData();
        SharedPreferences sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SP_KEY_INIT_DATA, false);
        editor.apply();
    }

    private void copyAssetsToData(){
        String srcDir = "snowboy";
        try {
            copyAssets(srcDir, BASE_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyAssets(String src, String dest) throws IOException {
        File destDirFile = new File(dest);
        if (!destDirFile.exists()){
            destDirFile.mkdirs();
        }

        String[] paths = getAssets().list(src);
        for (String path : paths){
            Log.v(TAG, "path:" +path);
            String newDestPath = dest + File.separator + path;
            String newPath = src + File.separator + path;
            String[] kidsPaths = getAssets().list(newPath);
            if (kidsPaths.length > 0){
                copyAssets(newPath, newDestPath);
            }
            else {
                InputStream inputStream = getAssets().open(newPath);
                FileOutputStream fileOutputStream = new FileOutputStream(newDestPath);

                byte[] input = new byte[1024];
                int count;
                while ((count = inputStream.read(input)) != -1){
                    fileOutputStream.write(input, 0, count);
                }

                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }
        }
    }


}
