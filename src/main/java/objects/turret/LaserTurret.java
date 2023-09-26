package objects.turret;

import engine.*;
import engine.handler.Handler;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import objects.RenderComponents.LaserTurretRenderComponent;
import objects.health.HealthComponent;
import objects.projectiles.Laser;
import objects.tank.Tank;

public class LaserTurret extends Turret {


    private final float DAMAGE = 10;
    /**
     * Constructor for the Turret class, which is called by the Tank constructor. it creates a Turret
     * object, and sets its position to the position of the parent Tank, causing it to stay in the same
     * location relative to the parent Tank object, and giving the illusion that the Tank and Turret are
     * one object.
     *
     * @param handler handler is passed to handle components etc
     * @param tank    the parent Tank object that the Turret is connected to
     */

    private Color beamColour;

    public LaserTurret(Handler handler, Tank tank) {
        super(handler, tank);
        this.removeComponent(renderer);
        renderer = new LaserTurretRenderComponent(pos, tank.hue);
        this.addComponent(renderer);
        NUM_FRAMES = 10;
    }

    @Override
    public void fire() {

        if (!animating) {
            if (beamColour == null) {
                Image turret = super.renderer.fetchImage().image;
                this.beamColour = turret.getPixelReader().getColor(321, 17);
            }
            //Bullet fire Sound Effects
            String path = "src/main/resources/sounds/zap.wav"; //sound location
            SoundFX laserFire = new SoundFX(path);
            laserFire.play(); //plays sound


            //Image turret = super.renderer.fetchImage().image;
            //this.beamColour = turret.getPixelReader().getColor(190, 80);
            //System.out.println(beamColour);
            //System.out.println(beamColour.getRed() + ", " + beamColour.getGreen() + ", " + beamColour.getBlue() + ", " + beamColour.getOpacity() + ", " + beamColour.getBrightness());
            //beamColour = new Color(beamColour.getRed(), beamColour.getGreen(), beamColour.getBlue(), 1);
            //super.fire();
            //new Projectile(parent.handler, Vec.add(Vec.add(Vec.scalarMult(pos.getRotation(), 45), parent.pos.get()), Vec.scalarMult(parent.vel, 2)), Vec.scalarMult(pos.getRotation(), 5));
            Vec rayStart = Vec.add(Vec.scalarMult(pos.getRotation(), 50), parent.pos.get());
            Ray testRay = new Ray(rayStart, Vec.scalarMult(pos.getRotation(), 5), parent.handler.getGridWorld());

            Collision c = testRay.getCollision();
            if (c != null) {
                //System.out.println(c.getPos());
                //new Projectile(parent.handler, c.getPos(), new Vec(0 ,0));
                new Laser(parent.handler, rayStart, c.getPos(), beamColour);

                if (c.getEntity() != null) {
                    HealthComponent health = c.getEntity().getComponent(HealthComponent.class);
                    if (health != null) {
                        SoundFX collision = new SoundFX("src/main/resources/sounds/collision.mp3");
                        collision.play();
                        health.reduceHealthPoints(DAMAGE);
                    }
                }
            }

            animating = true;
        }

    }
}
