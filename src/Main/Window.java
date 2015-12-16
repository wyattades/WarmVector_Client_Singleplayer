//package Main;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.image.BufferStrategy;
//
///**
// * Directory: WarmVector_Client_Singleplayer/Main/
// * Created by Wyatt on 12/15/2015.
// */
//
//public class Window {
//
//    public static int WIDTH;
//    public static int HEIGHT;
//
//    GraphicsDevice[] devices;
//    DisplayMode oldDisplayMode;
//    GraphicsEnvironment ge;
//    JFrame frame;
//
//    public Window() {
//
//        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        devices = ge.getScreenDevices();
//
//        frame = new JFrame(devices[0].getDefaultConfiguration());
//        frame.setName("WarmVector");
//        frame.getContentPane().setLayout(new BorderLayout());
//
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//        frame.pack();
//
//        mainScreenTurnOn();
//
//        Canvas canvas = new Canvas();
//        canvas.setBounds(0, 0, WIDTH, HEIGHT);
//        canvas.setIgnoreRepaint(true);
//
//        panel.add(canvas);
//
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setResizable(false);
//        frame.setUndecorated(true);
//        frame.add(new JLabel("WarmVector V2", SwingConstants.CENTER), BorderLayout.CENTER);
//        frame.validate();
//        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
//
//        canvas.createBufferStrategy(2);
//        bufferStrategy = canvas.getBufferStrategy();
//
//        canvas.requestFocus();
//
//        canvas.createBufferStrategy(2);
//        bufferStrategy = canvas.getBufferStrategy();
//
//        canvas.requestFocus();
//
//
//    }
//
//    public void toggleFullscreen(int toggle) {
//
//        if (toggle == 0) {
//            mainScreenTurnOff();
//        } else if (toggle == 1) {
//            mainScreenTurnOn();
//        }
//
////        WIDTH = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
////        HEIGHT = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
//        WIDTH = ge.getMaximumWindowBounds().width;
//        HEIGHT = ge.getMaximumWindowBounds().height;
//
//    }
//
//    private void mainScreenTurnOn() {
//
//        //Save old display mode
//        oldDisplayMode = devices[0].getDisplayMode();
//
//        //Go fullscreen
//        if (devices[0].isFullScreenSupported()) {
//            devices[0].setFullScreenWindow(frame);
//
//            //Change resolution
//            DisplayMode dm = new DisplayMode(800, 600, 32, 60);
//            if (devices[0].isDisplayChangeSupported())
//                devices[0].setDisplayMode(dm);
//        }
//    }
//
//    private void mainScreenTurnOff() {
//
//        devices[0].setDisplayMode(oldDisplayMode);
//        //Fix window size here or whatever etc etc.
//
//    }
//
//
//}
