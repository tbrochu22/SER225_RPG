package Music;

//file.Separator will be file operating system agnostic

public class LoopMusic {

    public static void main(String[] args) {
        String filePath = "src\\music\\initial_d___deja_vu.wav";

        MusicPlayer musicObject = new MusicPlayer();
        musicObject.playMusic(filePath);
    }
}