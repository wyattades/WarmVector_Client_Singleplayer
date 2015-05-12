package Visual.Occlusion;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 5/11/2015.
 */
public class EndPoint extends Point {
    public boolean begin = false;
    public Segment segment = null;
    public float angle = 0;
    public boolean visualize = false;
    public EndPoint(float x, float y) {
        super(x,y);
    }
}
