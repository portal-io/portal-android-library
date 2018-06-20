package com.whaley.core.utils;

import java.util.HashSet;
import java.util.Set;

/*
 * listen to if it can save cache on wifi enviroment or not
 */
public class WifiCacheObserver {
    public interface WifiCacheListener{
        void onCacheEnabled();
        void onCacheDisabled();
    }
    private static Set<WifiCacheListener> listeners = new HashSet<WifiCacheListener>();
    
    /**
     * register listner
     * @author xiexiaojian
     * @param listener
     */
    public synchronized static void registerListener(WifiCacheListener listener){
        if(! listeners.contains(listener)){
            listeners.add(listener);
        }
    }
    
    
    /**
     * unregister listner
     * @author xiexiaojian
     * @param listener
     */
    public synchronized static void unregisterListener(WifiCacheListener listener){
        if(listeners.contains(listener)){
            listeners.remove(listener);
        }
    }
    
    /**
     * notice all listners the switch is open
     * @author xiexiaojian
     * @param listener
     */
    public synchronized static void callCacheEnabled(){
        for(WifiCacheListener listener : listeners){
            listener.onCacheEnabled();
        }
    }

    /**
     * notice all listners the switch is closed
     * @author xiexiaojian
     * @param listener
     */
    public synchronized static void callCacheDisabled(){
        for(WifiCacheListener listener : listeners){
            listener.onCacheDisabled();
        }
    }
}
