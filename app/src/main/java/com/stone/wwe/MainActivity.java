package com.stone.wwe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {

    private static final int AUDIO_PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

    }

    public void startWweService(View view) {
        startWweService();
    }

    private void startWweService(){
        Intent intent = new Intent(this, MainService.class);
        startService(intent);
    }

    private void checkPermissions(){
        int result = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AUDIO_PERMISSION_REQUEST_CODE){
            for (int i = 0; i < permissions.length; i++){
                if (permissions[i].equals(Manifest.permission.RECORD_AUDIO)){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        findViewById(R.id.start_wwe).setEnabled(false);
                    }
                }
            }
        }
    }
}
