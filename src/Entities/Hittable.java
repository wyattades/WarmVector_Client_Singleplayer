package Entities;

import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by Wyatt on 9/3/2016.
 */
public interface Hittable {

    int getId();

    /**
     * @return preloaded hit animation (image array)
     */
    BufferedImage[] getHitAnimation();

    /**
     * @param p the projectile being checked for collision
     * @return if p is colliding with self
     */
    boolean handleDirectHit(Projectile p);

    /**
     * @param p the projectile being checked for collision
     * @return if p's explosion collides with self
     */
    boolean handleIndirectHit(Projectile p);

}
