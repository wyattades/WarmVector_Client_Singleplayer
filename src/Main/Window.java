package Main;

import GameState.GameStateManager;
import Util.ImageUtils;
import com.sun.deploy.util.SystemUtils;

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


    public Window(WindowAdapter _exitOperation) {

        String OS = System.getProperty("os.name");

        // Dimensions
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        if (!device.isFullScreenSupported() || OS.equals("Linux")) {
            Rectangle winSize = env.getMaximumWindowBounds();
            WIDTH = winSize.width;
            HEIGHT = winSize.height;
        } else {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            WIDTH = (int) screenSize.getWidth();
            HEIGHT = (int) screenSize.getHeight();
        }

        SCALE = HEIGHT / 1080.0;

        // JFrame
        frame = new JFrame("WarmVector");
        frame.addWindowListener(_exitOperation);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setVisible(true);

        // Fullscreen
        if (device.isFullScreenSupported() && !OS.equals("Linux")) {
            try {
                device.setFullScreenWindow(frame);
            } finally {
                device.setFullScreenWindow(null);
            }
        }

        //Set default mouse cursor to transparent
        Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                Toolkit.getDefaultToolkit().createImage(
                        new MemoryImageSource(16, 16, new int[16 * 16], 0, 16)), new Point(0, 0), "invisibleCursor");
        frame.setCursor(transparentCursor);

        // Canvas
        canvas = new Canvas(GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration());
        canvas.setSize(WIDTH, HEIGHT);
        frame.add(canvas, 0);

        // Background & Buffer
        background = ImageUtils.getCompatableVersion(new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB));
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

        } catch (NullPointerException | IllegalStateException e) {
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

            // TODO: this is weird way of calling draw
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
