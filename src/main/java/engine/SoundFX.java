package engine;

import javafx.scene.media.AudioClip;

import java.io.File;

public class SoundFX {

    String filepath;
    AudioClip clip;

    public SoundFX(String filepath) {
        this.filepath = filepath;
        String file = new File(filepath).toURI().toString();
        clip = new AudioClip(file);

    }

    public void play() {
        try {
            clip.setVolume(Sound.volume);
            clip.play();
        } catch (Exception ignored) {
            System.out.println("Sound failed to play, are you running Linux?");
        }
    }


    public void stop() {
        clip.stop();
    }


}
