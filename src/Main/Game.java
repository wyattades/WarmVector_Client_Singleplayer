package Main;

/**
 * Created by Wyatt on 12/29/2014.
 */
import Manager.FileManager;
import Manager.InputManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

public class Game implements Runnable{

    final int WIDTH = 1200;
    final int HEIGHT = 600;

    boolean running = true;

    JFrame frame;
    Canvas canvas;
    BufferStrategy bufferStrategy;
    InputManager inputManager;
    FileManager fileManager;

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
        inputManager.addKeyMapping("UP", KeyEvent.VK_UP);
        inputManager.addKeyMapping("DOWN", KeyEvent.VK_DOWN);
        inputManager.addKeyMapping("LEFT", KeyEvent.VK_LEFT);
        inputManager.addKeyMapping("RIGHT", KeyEvent.VK_RIGHT);
        inputManager.addMouseMapping("LEFTMOUSE", MouseEvent.BUTTON1);
        inputManager.addMouseMapping("RIGHTMOUSE", MouseEvent.BUTTON3);

    }

    public void run(){
        while(running){
            update();
            render();
        }
    }

    //TESTING
    private double x = 0;
    private double y = 0;
    private double a = 0;
    private double b = 0;

    protected void render(Graphics2D g, int x, int y, int w, int h ){
        g.fillRect(x, y, w, h);
    }

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        render(g,(int)x,(int)y,100,100);
        render(g,(int)a,(int)b,100,100);
        g.dispose();
        bufferStrategy.show();
    }


    /**
     * Rewrite this method for your game
     */
    protected void update() {

        if (inputManager.isKeyPressed("LEFT")) x -= 0.2;
        if (inputManager.isKeyPressed("RIGHT")) x += 0.2;
        if (inputManager.isKeyPressed("UP")) y -= 0.2;
        if (inputManager.isKeyPressed("DOWN")) y += 0.2;

        if (inputManager.mouse.inScreen) {
            a = inputManager.mouse.x;
            b = inputManager.mouse.y;
        }
        if (inputManager.isMouseClicked("LEFTMOUSE")) {
            a = 60;
            b = 60;
        }
    }

}