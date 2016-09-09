package Main;

import GameState.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;

/**
 * Directory: WarmVector_Client_Singleplayer/Main/
 * Created by Wyatt on 12/15/2015.
 */

public class Window {

    public static int WIDTH;
    public static int HEIGHT;
    public static double SCALE;

    Canvas canvas;

    private BufferStrategy strategy;
    private BufferedImage background;
    private Graphics2D backgroundGraphics, graphics;
    private JFrame frame;
    private GraphicsConfiguration config =
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

    // create a hardware accelerated image
    public final BufferedImage create(final int width, final int height,
                                      final boolean alpha) {
        return config.createCompatibleImage(width, height, alpha
                ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
    }

    public Window(WindowAdapter _exitOperation) {

        // Dimensions
        WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
//        WIDTH = 1280;
//        HEIGHT = 780;
        SCALE = HEIGHT / 1080.0;

        // JFrame
        frame = new JFrame();
        frame.setTitle("WarmVector");
        frame.addWindowListener(_exitOperation);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setVisible(true);

        //Set default mouse cursor to transparent
        Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                Toolkit.getDefaultToolkit().createImage(
                        new MemoryImageSource(16, 16, new int[16 * 16], 0, 16)), new Point(0, 0), "invisibleCursor");
        frame.setCursor(transparentCursor);

        // Canvas
        canvas = new Canvas(config);
        canvas.setSize(WIDTH, HEIGHT);
        frame.add(canvas, 0);

        // Background & Buffer
        background = create(WIDTH, HEIGHT, false);
        canvas.createBufferStrategy(2);
        do {
            strategy = canvas.getBufferStrategy();
        } while (strategy == null);

        backgroundGraphics = (Graphics2D) background.getGraphics();

        setQuality(OutputManager.getSetting("quality") == 1);
    }

    // Screen and buffer stuff
    private Graphics2D getBuffer() {
        if (graphics == null) {
            try {
                graphics = (Graphics2D) strategy.getDrawGraphics();
            } catch (IllegalStateException e) {
                return null;
            }
        }
        return graphics;
    }

    private boolean updateScreen() {
        graphics.dispose();
        graphics = null;
        try {
            strategy.show();
            Toolkit.getDefaultToolkit().sync();
            return (!strategy.contentsLost());

        } catch (NullPointerException e) {
            return true;

        } catch (IllegalStateException e) {
            return true;
        }
    }

    public void render(GameStateManager gsm) {
        // Update Graphics
        do {
            Graphics2D bg = getBuffer();
            assert bg != null;

            backgroundGraphics.setColor(Color.BLACK);
            backgroundGraphics.fillRect(0, 0, WIDTH, HEIGHT);

            // TODO: idk, this is weird way of doin it
            gsm.draw(backgroundGraphics);

            bg.drawImage(background, 0, 0, null);
            bg.dispose();

        } while (!updateScreen());
    }

    public void exit() {
        frame.dispose();
    }


    public void setQuality(boolean better) {
        backgroundGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                better ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }
}
