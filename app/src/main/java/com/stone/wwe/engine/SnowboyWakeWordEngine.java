package com.stone.wwe.engine;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ai.kitt.snowboy.SnowboyDetect;

/**
 * Use Snowboy library
 */
public class SnowboyWakeWordEngine extends WakeWordEngine implements Runnable {
    private static final String TAG = SnowboyWakeWordEngine.class.getName();

    private SnowboyDetect mSnowboyDetect;
    private AudioRecord mAudioRecord;
    private byte[] mAudioBuffer;
    private boolean isDetecting = false;

    public SnowboyWakeWordEngine(String resource, String model){
        super();
        mSnowboyDetect = new SnowboyDetect(resource, model);

        initAudioRecord();
    }

    @Override
    public void startDetection() {
        new Thread(this).start();
    }

    @Override
    public void stopDetection() {
        isDetecting = false;
    }

    private void initAudioRecord(){
        int buffersize = (int) (16000 * 2 * 0.1);
        mAudioBuffer = new byte[buffersize];
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, 16000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, mAudioBuffer.length);

        mAudioRecord.getState();
    }

    /**
     * load shared library 'libsnowboy-detect-android.so'
     */
    static {
        System.loadLibrary("snowboy-detect-android");
    }

    @Override
    public void run() {
        isDetecting = true;
        mAudioRecord.startRecording();

        while (isDetecting){
            mAudioRecord.read(mAudioBuffer, 0, mAudioBuffer.length);
            short[] data = new short[mAudioBuffer.length / 2];
            ByteBuffer.wrap(mAudioBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(data);

            int detectResult = mSnowboyDetect.RunDetection(data, data.length);

            switch (detectResult){
                case -2:
                    this.onNoSpeech();
                    break;
                case -1:
                    this.onDetectError();
                    break;
                case 0:
                    this.onSpeeching();
                    break;
                default:
                    this.onKeyWordDetect(detectResult);
                    break;
            }
        }
        mAudioRecord.stop();
    }
}
