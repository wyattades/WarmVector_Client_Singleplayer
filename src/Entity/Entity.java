package Entity;

/**
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Entity {

    double x,y,dx,dy;

    public Entity(double x, double y) {
        this.x = x;
        this.y = y;
        dx = dy = 0;
    }

}
