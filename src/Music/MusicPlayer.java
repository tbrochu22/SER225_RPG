package Music;

import java.io.File;
import javax.sound.sampled.*;

public class MusicPlayer {

    private static Clip clip;

    public static void playMusic(String musicLocation) {
        stopMusic();

        try {
            File musicPath = new File(musicLocation);

            AudioInputStream audioInput =
                    AudioSystem.getAudioInputStream(musicPath);

            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (Exception e) {
            System.out.println("Could not play music: " + e.getMessage());
        }
    }

    public static void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }
}