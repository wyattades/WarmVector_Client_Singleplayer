package StaticManagers;

import Helper.MyMath;
import Main.Game;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Directory: WarmVector_Client_Singleplayer/StaticManagers/
 * Created by Wyatt on 12/13/2015.
 */
public class AudioManager {

    private static float musicVolume, SFXVolume, minVolume, maxVolume;
    static {
        minVolume = -80;
        maxVolume = 6;
        updateSettings();
    }

    public static void updateSettings() {
        musicVolume = MyMath.map(OutputManager.getSettingValue("music_volume"), 0, 4, minVolume, maxVolume);
        SFXVolume = MyMath.map(OutputManager.getSettingValue("sfx_volume"), 0, 4, minVolume, maxVolume);
    }

    public static void playSFX(Clip clip) {
        clip.stop();
        clip.setFramePosition(0);
        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(SFXVolume);
        clip.start();
    }

    public void pauseAll() {

    }

    public void stopAll() {

    }

}
