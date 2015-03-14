package Main;

/**
 * Created by Wyatt on 12/29/2014.
 */

import Manager.GameStateManager;
import Manager.InputManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.MemoryImageSource;

public class Game implements Runnable {

    public final static int WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public final static int HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public final static double SCALEFACTOR = 1.333;
    private static final long MS_PER_FRAME = 16;

    private boolean running = true;

    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;
    private InputManager inputManager;
    private GameStateManager gsm;
    private Robot robot;

    public Game() {
        frame = new JFrame("WarmVector Singleplayer V2");

        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
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

        Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                Toolkit.getDefaultToolkit().createImage(
                        new MemoryImageSource(16, 16, new int[16 * 16], 0, 16)), new Point(0, 0), "invisibleCursor");
        panel.setCursor(transparentCursor);

        inputManager = new InputManager(canvas);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        inputManager.addKeyMapping("UP", KeyEvent.VK_W);
        inputManager.addKeyMapping("DOWN", KeyEvent.VK_S);
        inputManager.addKeyMapping("LEFT", KeyEvent.VK_A);
        inputManager.addKeyMapping("RIGHT", KeyEvent.VK_D);
        inputManager.addKeyMapping("ESCAPE", KeyEvent.VK_ESCAPE);
        inputManager.addMouseMapping("LEFTMOUSE", MouseEvent.BUTTON1);
        inputManager.addMouseMapping("RIGHTMOUSE", MouseEvent.BUTTON3);

        gsm = new GameStateManager();

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

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public void run() {

        while (running) {
            long start = System.currentTimeMillis();

            //ALL DA CODE HERE
            update();
            render();

            //sets fps to 30
            long sleepTime = (start + MS_PER_FRAME) - System.currentTimeMillis();
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            frames++;
            if (System.currentTimeMillis() - startTime >= 1000) {
                startTime = System.currentTimeMillis();
                System.out.println(frames);
                frames = 0;
            }
        }
    }

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_DITHERING,
                RenderingHints.VALUE_DITHER_DISABLE);

        g.setColor(new Color(120, 120, 120));
        g.fillRect(0, 0, WIDTH, HEIGHT); //background
        gsm.draw(g); //here is where the game is actually drawn
        g.dispose();
        bufferStrategy.show();
    }

    void update() {
        robot.mouseMove(Game.WIDTH / 2, Game.HEIGHT / 2);
        gsm.update();
    }

}