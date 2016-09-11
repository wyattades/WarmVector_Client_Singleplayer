package Main;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.swing.*;
import java.util.concurrent.*;

/**
 * Directory: WarmVector_Client_Singleplayer/Main/
 * Created by Wyatt on 12/13/2015.
 */
public class AudioManager implements Runnable {

    private static final int MAX_QUEUE_AMOUNT = 64;
    private boolean running;
    private volatile double musicVolume, SFXVolume;

    private BlockingQueue<AudioClip> clips;

    private MediaPlayer mediaPlayer;
    private String background;
    private ExecutorService backgroundPlayer;

    AudioManager() {
        clips = new ArrayBlockingQueue<>(MAX_QUEUE_AMOUNT);

        running = true;

        // Wait for JavaFX to initialize
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            new JFXPanel(); // initializes JavaFX environment
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            OutputManager.printError(e);
        }
    }

    public void stopBackground() {
        if (backgroundPlayer != null) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                mediaPlayer = null;
            }
            backgroundPlayer.shutdownNow();
        }
    }

    public void playSong(Media source, String name) {
        if (!name.equals(background)) {
            background = name;

            stopBackground();
            backgroundPlayer = Executors.newSingleThreadExecutor();
            backgroundPlayer.execute(new Thread() {
                @Override
                public void run() {
                    mediaPlayer = new MediaPlayer(source);
                    // Set song to loop
                    mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
                    mediaPlayer.setVolume(musicVolume);
                    mediaPlayer.play();
                }
            });
        }
    }

    public void playSFX(AudioClip source) {
        try {
            clips.put(source);
        } catch (InterruptedException e) {
            OutputManager.printError(e);
            OutputManager.printError("Error while putting item in queue");
            System.exit(1);
        }
    }

    @Override
    public void run() {

        // Continuously loop
        while (running) {

            // TODO: this is a bad way of updating mediaPlayer volume
            if (mediaPlayer != null && musicVolume != mediaPlayer.getVolume()) {
                mediaPlayer.setVolume(musicVolume);
            }

            while (!clips.isEmpty()) {
//                String name = clips.poll();
//                AudioClip source = AssetManager.loadShortAudio(name);
                AudioClip source = clips.poll();
//                if (source.isPlaying()) source.stop();
                source.play(SFXVolume);

            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        // Stop and dispose mediaPlayer when thread closes, idk if this is necessary
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }

    public void setVolume(double SFXSetting, double musicSetting) {
        SFXVolume = SFXSetting * 0.25;
        musicVolume = musicSetting * 0.25;
    }

    public void stop() {
        running = false;
    }

}
