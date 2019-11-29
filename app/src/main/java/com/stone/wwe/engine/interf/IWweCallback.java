package com.stone.wwe.engine.interf;

/**
 * Callback for notify wwe detect status
 */
public interface IWweCallback {

    /**
     * key word detected
     */
    void onKeyWordDetect(int count);

    /**
     * detect error : unknown error
     */
    void onDetectError();

    /**
     * not found speech
     */
    void onNoSpeech();

    /**
     * speeching but not detect key word
     */
    void onSpeeching();
}
