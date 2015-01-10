package Default;

/**
 * Created by Wyatt on 12/29/2014.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

public class Game implements Runnable{

    final int WIDTH = 1200;
    final int HEIGHT = 600;

    long desiredFPS = 60;
    long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;

    boolean running = true;

    JFrame frame;
    Canvas canvas;
    BufferStrategy bufferStrategy;
    InputManager inputManager;

    public Game(){
        frame = new JFrame("WarmVector Singleplayer V2");

        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);

        inputManager = new InputManager(canvas);
        inputManager.addKeyMapping("UP", KeyEvent.VK_UP);
        inputManager.addKeyMapping("DOWN", KeyEvent.VK_DOWN);
        inputManager.addKeyMapping("LEFT", KeyEvent.VK_LEFT);
        inputManager.addKeyMapping("RIGHT", KeyEvent.VK_RIGHT);
        inputManager.addMouseMapping("LEFTMOUSE", MouseEvent.BUTTON1);
        inputManager.addMouseMapping("RIGHTMOUSE", MouseEvent.BUTTON3);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        canvas.requestFocus();
    }

    public void run(){

        long beginLoopTime;
        long endLoopTime;
        long currentUpdateTime = System.nanoTime();
        long lastUpdateTime;
        long deltaLoop;

        while(running){
            beginLoopTime = System.nanoTime();

            render();

            lastUpdateTime = currentUpdateTime;
            currentUpdateTime = System.nanoTime();
            update((int) ((currentUpdateTime - lastUpdateTime)/(1000000)));

            endLoopTime = System.nanoTime();
            deltaLoop = endLoopTime - beginLoopTime;

            if(deltaLoop > desiredDeltaLoop){
                //Do nothing. We are already late.
            }else{
                try{
                    Thread.sleep((desiredDeltaLoop - deltaLoop)/(1000000));
                }catch(InterruptedException e){
                    //Do nothing
                }
            }
        }
    }

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        render(g);
        g.dispose();
        bufferStrategy.show();
    }

    //TESTING
    private double x = 0;
    private double y = 0;
    private double x2 = 0;
    private double y2 = 0;


    /**
     * Rewrite this method for your game
     */
    protected void update(int deltaTime) {

        if (inputManager.isKeyPressed("LEFT")) x -= deltaTime*0.2;
        if (inputManager.isKeyPressed("RIGHT")) x += deltaTime*0.2;
        if (inputManager.isKeyPressed("UP")) y -= deltaTime*0.2;
        if (inputManager.isKeyPressed("DOWN")) y += deltaTime*0.2;

        if (inputManager.mouse.inScreen && inputManager.isMousePressed("LEFTMOUSE")) {
            x = inputManager.mouse.x;
            y = inputManager.mouse.y;
        }
        if (inputManager.isMouseClicked("LEFTMOUSE")) {
            x = 60;
            y = 60;
        }
    }

    /**
     * Rewrite this method for your game
     */
    protected void render(Graphics2D g){

        g.fillRect((int)x, (int)y, 200, 200);
        //g.fillRect((int)x2, (int)y2, 150, 150);
    }

}