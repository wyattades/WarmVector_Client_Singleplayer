package Main;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 12/29/2014.
 */

import GameState.GameStateManager;
import Manager.InputManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.MemoryImageSource;

public class Game implements Runnable {

    public final static int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public final static int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public final static float SCALEFACTOR = 2;
    private static final int MS_PER_FRAME = 16;

    private boolean running;

    private BufferStrategy bufferStrategy;
    private GameStateManager gsm;

    public Game() {
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

        //Set default mouse cursor to transparent
        Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                Toolkit.getDefaultToolkit().createImage(
                        new MemoryImageSource(16, 16, new int[16 * 16], 0, 16)), new Point(0, 0), "invisibleCursor");
        panel.setCursor(transparentCursor);

        //Create a manager for handling key and mouse inputs
        InputManager inputManager = new InputManager(canvas);

        //Add key and mouse mappings to inputManager
        inputManager.addKeyMapping("UP", KeyEvent.VK_W);
        inputManager.addKeyMapping("DOWN", KeyEvent.VK_S);
        inputManager.addKeyMapping("LEFT", KeyEvent.VK_A);
        inputManager.addKeyMapping("RIGHT", KeyEvent.VK_D);
        inputManager.addKeyMapping("ESCAPE", KeyEvent.VK_ESCAPE);
        inputManager.addKeyMapping("ALT", KeyEvent.VK_ALT);
        inputManager.addMouseMapping("LEFTMOUSE", MouseEvent.BUTTON1);
        inputManager.addMouseMapping("RIGHTMOUSE", MouseEvent.BUTTON3);

        //Create a manager for handling the different game states e.g. intro, play, gameOver
        gsm = new GameStateManager();

        //Run the game thread
        run();
    }

    private double startTime = 0;
    private int frames = 0;

    public static int currentTimeMillis() {
        long millisLong = System.currentTimeMillis();
        while (millisLong > Integer.MAX_VALUE) {
            millisLong -= Integer.MAX_VALUE;
        }
        return (int) millisLong;
    }

    public static float random(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    public void run() {

        while (running) {
            int start = currentTimeMillis();

            update();
            render();

            //sets fps to 60
            int sleepTime = (start + MS_PER_FRAME) - currentTimeMillis();
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            frames++;
            if (currentTimeMillis() - startTime >= 1000) {
                startTime = currentTimeMillis();
                System.out.println(frames);
                frames = 0;
            }
        }
    }

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
//        g.setRenderingHint(RenderingHints.KEY_RENDERING,
//                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
//                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
//                RenderingHints.VALUE_COLOR_RENDER_SPEED);
//        g.setRenderingHint(RenderingHints.KEY_DITHERING,
//                RenderingHints.VALUE_DITHER_ENABLE);

        g.setFont(new Font("Dotum Bold", Font.BOLD, 45));

        gsm.draw(g); //here is where the game is actually drawn

        g.dispose();

        bufferStrategy.show();

    }

    void update() {
        gsm.update();
    }

}