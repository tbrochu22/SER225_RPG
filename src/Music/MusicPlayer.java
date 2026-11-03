package Music;

import java.io.File;
import javax.sound.sampled.*;

public class MusicPlayer {

    private static Clip clip;

    // Controls the music volume
    private static float musicVolume = -40.0f;

    public static void playMusic(String musicLocation) {
        stopMusic();

        try {
            File musicPath = new File(musicLocation);

            AudioInputStream audioInput =
                    AudioSystem.getAudioInputStream(musicPath);

            clip = AudioSystem.getClip();
            clip.open(audioInput);

            // Lower the music volume
            FloatControl volume =
                    (FloatControl) clip.getControl(
                            FloatControl.Type.MASTER_GAIN);

            volume.setValue(musicVolume);

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