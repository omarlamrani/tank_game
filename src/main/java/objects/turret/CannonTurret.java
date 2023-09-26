package objects.turret;

import engine.SoundFX;
import engine.Vec;
import engine.components.controllers.TurretControls;
import engine.handler.Handler;
import objects.GameObject;
import objects.RenderComponents.CannonTurretRenderComponent;
import objects.RenderComponents.LaserTurretRenderComponent;
import objects.RenderComponents.TurretRenderComponent;
import objects.projectiles.CannonBall;
import objects.projectiles.Projectile;
import objects.tank.Tank;

import static engine.components.controllers.TurretMouseController.getTurnFromTarget;

public class CannonTurret extends Turret {

        //static Image turret = new Image("file:src/main/resources/turret.png");
        //static Image blast = new Image("file:src/main/resources/blast.png");
        //public transient Image sprite;

        //static ShadowWrapper effect = new ShadowWrapper(10.F, Color.BLACK);

        //private int frame = 0;
        //public TurretMovementComponent controls;

        //PositionComponent pos;

        public CannonTurret(Handler handler, Tank tank) {
            super(handler,tank);
            this.removeComponent(renderer);
            renderer = new CannonTurretRenderComponent(pos, tank.hue);
            this.addComponent(renderer);
        }

        public void fire() {
            if (!animating) {

                //Little bit of magic to make it look like it's appearing in the right place while moving
                new CannonBall(parent.handler, Vec.add(Vec.add(Vec.scalarMult(pos.getRotation(), 45), parent.pos.get()), Vec.scalarMult(parent.vel, 2)), Vec.scalarMult(pos.getRotation(), 3));

                //Bullet fire Sound Effects
                String path = "src/main/resources/sounds/gunshot.mp3"; //sound location
                SoundFX bulletFire = new SoundFX(path);
                bulletFire.play(); //plays sound

                animating = true;
            }
        }
}
