package ca.cmfly.controller.programs;

import java.util.concurrent.Callable;

public class LightShowTimer implements Callable<String> {
 
    private long showTime;
     
    public LightShowTimer(int timeInMillis){
        this.showTime = timeInMillis;
    }
    
    @Override
    public String call() throws Exception {
        Thread.sleep(showTime);
        //return the thread name executing this callable task
        return Thread.currentThread().getName();
    }
 
}