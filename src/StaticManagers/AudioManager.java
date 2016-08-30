package StaticManagers;

import Util.MyMath;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Directory: WarmVector_Client_Singleplayer/StaticManagers/
 * Created by Wyatt on 12/13/2015.
 */
public class AudioManager {

    private static float musicVolume, SFXVolume;
    static {
        updateSettings();
    }
    private static Clip background;
    private static String backgroundName = "";

    public static void updateSettings() {

        musicVolume = MyMath.map(OutputManager.getSetting("music_volume"), 0.0f, 4.0f, 0.0f, 1.0f);
        musicVolume = 30.0f * (float) Math.log(musicVolume);
        setVolume(background, musicVolume);


        SFXVolume = MyMath.map(OutputManager.getSetting("sfx_volume"), 0.0f, 4.0f, 0.0f, 1.0f);
        SFXVolume = 40.0f * (float) Math.log(SFXVolume);

    }

    public static void playSFX(String name) {

        Clip clip = FileManager.getSound(name);

        //reset clip
        clip.stop();
        clip.flush();
        clip.setFramePosition(0);

        //set volume of clip
        setVolume(clip, SFXVolume);

        clip.start();
    }

    public static void playMusic(String name) {

        if (!backgroundName.equals(name)) {
            backgroundName = name;

            //reset clip
            if (background != null) {
                background.stop();
                background.flush();
                background.setFramePosition(0);
            }

            background = FileManager.getSound(name);

            //set volume of clip
            setVolume(background, musicVolume);

            background.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    private static void setVolume(Clip clip, float level) {
        if (clip != null) {
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(level);
        }
    }

//    public void pauseAll() {
//
//    }
//
//    public void resumeAll() {
//
//    }
//
//    public void stopAll() {
//
//    }

}
