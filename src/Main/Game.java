package Main;

/**
 * Created by Wyatt on 12/29/2014.
 */
import Manager.FileManager;
import Manager.GameStateManager;
import Manager.InputManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

public class Game implements Runnable{

    public final static int WIDTH = 1200;
    public final static int HEIGHT = 600;

    boolean running = true;

    JFrame frame;
    Canvas canvas;
    BufferStrategy bufferStrategy;
    InputManager inputManager;
    GameStateManager gsm;

    public Game(){
        frame = new JFrame("WarmVector Singleplayer V2");

        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        canvas.requestFocus();

        inputManager = new InputManager(canvas);
        inputManager.addKeyMapping("UP", KeyEvent.VK_W);
        inputManager.addKeyMapping("DOWN", KeyEvent.VK_S);
        inputManager.addKeyMapping("LEFT", KeyEvent.VK_A);
        inputManager.addKeyMapping("RIGHT", KeyEvent.VK_D);
        inputManager.addMouseMapping("LEFTMOUSE", MouseEvent.BUTTON1);
        inputManager.addMouseMapping("RIGHTMOUSE", MouseEvent.BUTTON3);
        //fileManager = new FileManager();
        gsm = new GameStateManager();
        run();
    }

    public void run(){
        while(running){
            update();
            render();
        }
    }

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.setColor(new Color(120,120,120));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        gsm.draw(g);
        g.dispose();
        bufferStrategy.show();
    }

    protected void update() {
       gsm.update();
    }

}