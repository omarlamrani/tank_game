package engine;

//This whole class may not be needed. Alternative is javaFX animationTimer.
//My thinking was that this allows ticking to be run on a different thread but there might be an easier way of doing this.

import engine.handler.Handler;

public class GameLoop implements Runnable {
    private final int TPS = 60;
    private final double timePerFrame = 1000000000 / ((double) TPS); //The target number of nanoseconds between ticks.
    private final Handler handler;
    Thread thread;
    private volatile boolean running = true;


    public GameLoop(Handler handler) {
        this.handler = handler;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        double sinceLastFrame;
        double adjust = 0;
        double lastFrame = System.nanoTime();
        int ticks = 0;
        double lastSec = System.nanoTime();
        double sinceLastSec;
        double secAdjust = 0;

        while (running) {
            sinceLastFrame = System.nanoTime() - lastFrame + adjust;
            sinceLastSec = System.nanoTime() - lastSec + secAdjust;

            if (sinceLastFrame >= timePerFrame) {
                lastFrame = System.nanoTime();
                adjust = sinceLastFrame - timePerFrame;
                ticks++;
                handler.tickList();
            }

            if (sinceLastSec >= 1000000000) {
                secAdjust = sinceLastSec - 1000000000;
                System.out.println("TPS: " + ticks + ", RenderComponents: " + handler.renderComponents.size());
                ticks = 0;
                lastSec = System.nanoTime();
            }
        }

    }

    public void stop() {
        this.running = false;
    }

}
