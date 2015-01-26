package Entity;

/**
 * Created by Wyatt on 1/22/2015.
 */
class ThisPlayer extends Player {

    long initTime;

    ThisPlayer(double x, double y, double w, double h, double orient, Weapon weapon) {
        super(x, y, w, h, orient, weapon);
    }

    void update(Input input) {
        updateLife();
        if (input.mouseRight && millis()-input.mouseRightTime>500) {
            input.mouseRightTime = millis();
            boolean collide = true;
            if (weaponType != 0) {
                world.addDroppedWeapon(this);
                weaponType = 0;
                for (int i = 0; i < world.droppedWeps.size ()-1; i++) {
                    DroppedWeapon dw = world.droppedWeps.get(i);
                    if (dw.collidePlayer(this)) {
                        weaponType = dw.type;
                        round = dw.rounds;
                        dw.state = false;
                        break;
                    }
                }
            } else {
                for (int i = 0; i < world.droppedWeps.size (); i++) {
                    DroppedWeapon dw = world.droppedWeps.get(i);
                    if (dw.collidePlayer(this)) {
                        weaponType = dw.type;
                        round = dw.rounds;
                        dw.state = false;
                        break;
                    }
                }
            }
        }

        velocity.set(0, 0, 0);
        if (input.keyLeft && input.keyRight);
        else if (input.keyLeft) updateVelX(-topSpeed);
        else if (input.keyRight) updateVelX(topSpeed);
        if (input.keyUp && input.keyDown);
        else if (input.keyUp) updateVelY(-topSpeed);
        else if (input.keyDown) updateVelY(topSpeed);
        if (velocity.mag()>topSpeed) velocity.setMag(topSpeed);
        updatePosition();
    }

    private void updateAngle(double cursorx, double cursory) {
        orient = Math.atan2(cursory-dy, cursorx-dx);
    }
}
