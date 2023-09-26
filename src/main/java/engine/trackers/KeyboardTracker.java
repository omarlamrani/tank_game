package engine.trackers;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class KeyboardTracker {

    public boolean W, A, S, D;

    public KeyboardTracker(Scene scene) {
        scene.setOnKeyPressed(e -> {
            updateKey(e.getCode(), true);
        });

        scene.setOnKeyReleased(e -> {
            updateKey(e.getCode(), false);
        });

    }

    private void updateKey(KeyCode code, boolean value) {
        switch (code) {
            case W:
                W = value;
                break;
            case A:
                A = value;
                break;
            case S:
                S = value;
                break;
            case D:
                D = value;
                break;
        }

    }


}
