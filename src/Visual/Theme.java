package Visual;

import Main.Game;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by wyatt on 8/11/2015.
 */
public abstract class Theme {

    public static Color
//            textOver = new Color(158, 158, 158),
//            textTitle = new Color(0, 0, 0, 200),
            buttonSelected = new Color(220, 220, 220),
            buttonDefault = new Color(180, 180, 180, 220),
            buttonOver = new Color(249, 249, 249),
            buttonOverOld = buttonOver,
            cursor = new Color(198, 198, 198),
            menuBackground = new Color(22, 20, 22);

    private static float scale = Game.HEIGHT / 1080.0f;

    public static Font
            fontLogo = new Font("Dotum Bold", Font.BOLD, (int) (200.0f * scale)),
            fontButton = new Font("Dotum Bold", Font.BOLD, (int) (60.0f * scale)),
            fontHUD = new Font("Dotum Bold", Font.BOLD, (int) (40.0f * scale));

    public static int
            buttonHeight = (int) (50.0f * scale);
}
