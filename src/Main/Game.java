package Main;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 12/29/2014.
 */

import GameState.GameStateManager;
import StaticManagers.InputManager;
import StaticManagers.OutputManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.MemoryImageSource;
import java.util.TimerTask;
import java.util.Timer;

public class Game implements Runnable {

    public static int WIDTH;
    public static int HEIGHT;

    public static boolean running;

    private BufferStrategy bufferStrategy;
    private GameStateManager gsm;
    private InputManager inputManager;

    public Game() {

        WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        running = true;
        JFrame frame = new JFrame("WarmVector Singleplayer V2");

        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        Canvas canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.add(new JLabel("WarmVector V2", SwingConstants.CENTER), BorderLayout.CENTER);
        frame.validate();
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        canvas.requestFocus();

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        canvas.requestFocus();

        //Set default mouse cursor to transparent
        Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                Toolkit.getDefaultToolkit().createImage(
                        new MemoryImageSource(16, 16, new int[16 * 16], 0, 16)), new Point(0, 0), "invisibleCursor");
        panel.setCursor(transparentCursor);

        //Create a manager for handling key and mouse inputs
        inputManager = new InputManager(canvas);

        //Add key and mouse mappings to inputManager
        inputManager.addKeyMapping("UP", KeyEvent.VK_W);
        inputManager.addKeyMapping("DOWN", KeyEvent.VK_S);
        inputManager.addKeyMapping("LEFT", KeyEvent.VK_A);
        inputManager.addKeyMapping("RIGHT", KeyEvent.VK_D);
        inputManager.addKeyMapping("ESC", KeyEvent.VK_ESCAPE);
        inputManager.addKeyMapping("ALT", KeyEvent.VK_ALT);
        inputManager.addKeyMapping("SPACE", KeyEvent.VK_SPACE);
        inputManager.addMouseMapping("LEFTMOUSE", MouseEvent.BUTTON1);
        inputManager.addMouseMapping("RIGHTMOUSE", MouseEvent.BUTTON3);

        //Create a manager for handling the different game states e.g. intro, play, gameOver
        gsm = new GameStateManager();

        //Run the game thread
        run();
    }

    public static int currentTimeMillis() {
        long millisLong = System.currentTimeMillis();
        while (millisLong > Integer.MAX_VALUE) {
            millisLong -= Integer.MAX_VALUE;
        }
        return (int) millisLong;
    }

    double previous = currentTimeMillis();
    double lag = 0.0;

    @Override
    public void run() {

        while (running) {

            double current = currentTimeMillis();
            double elapsed = current - previous;
            previous = current;
            lag += elapsed;

            //INPUT HANDLE
            gsm.inputHandle(inputManager);

            //UPDATE
            int MS_PER_UPDATE = 16;
            if (lag >= MS_PER_UPDATE) {
                gsm.update();
                lag -= MS_PER_UPDATE;
            }

            //RENDER
            render();

            System.out.println(1000d/elapsed);

        }

        exit();

    }

    private void render() {

        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();

        if (OutputManager.getSetting("quality") == 1) {
            g.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
        }
        if (OutputManager.getSetting("anti_aliasing") == 1) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        gsm.draw(g); //game is drawn

        g.dispose();

        bufferStrategy.show();

    }

    private void exit() {

        System.out.println("Closing program...");
        System.exit(0);

    }

}