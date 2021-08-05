package unsw.loopmania;

import java.io.File;

import javafx.animation.*;
import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class SoundManager {
    
    private MediaPlayer mediaPlayer;
    
    public SoundManager() {}

    /**
     * Play an audio to the user
     * @param fileName
     * @param volume
     */
    public void playSoundEffect(String fileName, double volume) {
        Media media = new Media(new File(fileName).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(()->{
            mediaPlayer.stop();
            mediaPlayer.dispose();
        });
        //mediaPlayer.dispose();
    }

    /**
     * Play background music for the user
     * @pre no background music is currently playiing
     * @post background music is playing
     * @param fileName
     */
    public void playBackgroundMusic(String fileName) {

        Media media = new Media(new File(fileName).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0);
        mediaPlayer.play();

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(3),
            new KeyValue(mediaPlayer.volumeProperty(), 0.05))
        );
        timeline.play();

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        
    }

    /**
     * Changes the current background music to another 
     * @pre background music is currently playiing
     */
    public void changeBackgroundMusic(String fileName) {
        stopBackgroundMusic();
        if (isMuted()) {
            playBackgroundMusic(fileName);
            setMuted(true);
        } else {
            playBackgroundMusic(fileName);
        }
    }

    /**
     * Stop the background music for the user
     * @pre background music is currently playiing
     * @post no background music is playing
     */
    public void stopBackgroundMusic() {
        mediaPlayer.stop();
    }

    /**
     * Checks if the background music is currently muted
     * @pre background music is currently playiing
     * @post no background music is playing
     */
    public boolean isMuted() {
        return mediaPlayer.isMute();
    }

    /**
     * Mutes or unmutes the background music
     * @param value
     */
    public void setMuted(boolean value) {
        if (value) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
        }

        mediaPlayer.setMute(value);
    }

    /**
     * Pauses the background music
     */
    public void pause() {
        mediaPlayer.pause();
    }

    /**
     * Resumes the background music
     */
    public void resume() {
        mediaPlayer.play();
    }

}
