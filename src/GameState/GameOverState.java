package GameState;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
//public class GameOverState extends MenuState {
//
//    // NOT USED
//    // WILL DELETE MAYBE
//
//    public GameOverState(GameStateManager gsm) {
//        super(gsm);
//    }
//
//    public void init() {
//        startY = Game.HEIGHT - 100;
//    }
//
//    public void unload() {}
//
//    protected void initButtons() {
//        buttons = new ArrayList<>();
//        sliders = new ArrayList<>();
//        addButton("CONTINUE", ButtonC.ButtonType.CONTINUE);
//        initDefault();
//    }
//
//    public void draw(Graphics2D g) {
//
//        drawBackground(g, StartMenuState.BACKGROUND_COLOR);
//
//        for (Slider s : sliders) {
//            s.draw(g);
//        }
//
//        for (ButtonC b : buttons) {
//            b.update(gsm.cursor.x, gsm.cursor.y);
//            b.draw(g);
//        }
//
//        String text = "YOU DIED";
//        g.setColor(ButtonC.buttonDefault);
//        g.drawString(
//                text,
//                Game.WIDTH * 0.5f - (int) g.getFontMetrics().getStringBounds(text, g).getWidth() * 0.5f,
//                Game.HEIGHT * 0.5f - 150
//        );
//
//    }
//
//    public void update() {
//    }
//
//}
