package engine;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Sound {

    public static double volume;
    String filepath;
    Media backingtrack;
    MediaPlayer mediaPlayer;

    public Sound(String filepath) {
        this.filepath = filepath;
        String file = new File(filepath).toURI().toString();
        backingtrack = new Media(file);
        try {
            mediaPlayer = new MediaPlayer(backingtrack);
        } catch (Exception ignored) {
            System.out.println("Failed to create mediaplayer, are you running Linux?");
        }
    }


    public void play() {
        try {
            mediaPlayer.play();
            //loops backing track
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.play();
                }
            });
        } catch (Exception ignored) {
            System.out.println("Sound failed to play, are you running Linux?");
        }
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double vol) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(vol); //allows volume to be set
            volume = mediaPlayer.getVolume(); //retrieves current volume
        }
    }
}
