package com.stone.wwe.engine;

import com.stone.wwe.engine.interf.IEngine;
import com.stone.wwe.engine.interf.IWweCallback;

/**
 * Base calss of WWE(Wake Word Engine)
 * A new WWE should extend this class, and Users should use this class to use key word detect.
 * the relationship between WakeWordEngine ane XXXWakeWordEngine:
 * WakeWordEngine define the architecture for upper user and lower developer
 * XXXWakeWordEngine is a specific realize using a special library like snowboy and so on;
 * WakeWordEngine is for upper developer, add this to your logic.
 */
public abstract class WakeWordEngine implements IEngine {

    protected IWweCallback mWweCallback;

    public WakeWordEngine(){}

    public void setCallback(IWweCallback callback){
        this.mWweCallback = callback;
    }
}
