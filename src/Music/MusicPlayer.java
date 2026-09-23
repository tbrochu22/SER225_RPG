package Music;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicPlayer {

    private Clip clip;

    public void playMusic(String musicLocation) {

        try {
            File musicPath = new File(musicLocation);

            System.out.println("Looking for: " + musicPath.getAbsolutePath());

            if (!musicPath.exists()) {
                System.out.println("Music file not found!");
                return;
            }

            try (AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath)) {
                clip = AudioSystem.getClip();
                clip.open(audioInput);
            }

            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void startMusic() {
        if (clip != null) {
            clip.start();
        }
    }

    public void closeMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
