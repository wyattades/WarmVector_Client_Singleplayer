package Visual.Occlusion;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 5/11/2015.
 */
public class Segment {
    public EndPoint p1,p2;
    public float d;

    public Segment() {
    }

    public Segment(EndPoint p1, EndPoint p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

}
