package engine;

import engine.handler.Handler;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;

public class RenderLoop extends AnimationTimer {
    final Handler handler;
    final Stage stage;

    long[] frames = new long[60];
    int fTracker = 0;

    public RenderLoop(Handler handler, Stage stage) {
        this.stage = stage;
        this.handler = handler;

    }

    @Override
    public void handle(long l) {
        handler.renderList();

        if (fTracker != 60) {
            frames[fTracker] = l;
            fTracker++;
        } else {
            //Do fps calculations
            long totalTime = 0;
            for (int i = 1; i < 60; i++) {
                totalTime += (frames[i] - frames[i - 1]);
            }

            double avgTPF = totalTime / 59.0D;
            double fps = 1000000000.0 / avgTPF;
            stage.setTitle("TANK DESTROYER" + ", FPS: " + (int) fps);
            fTracker = 0;

        }

    }
}
