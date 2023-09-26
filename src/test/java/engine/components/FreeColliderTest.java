package engine.components;

import engine.Vec;
import engine.handler.Handler;
import engine.handler.HeadlessHandler;
import javafx.embed.swing.JFXPanel;
import objects.tank.Tank;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FreeColliderTest {
    Handler handler = new HeadlessHandler(10, 10);
    JFXPanel jfxPanel = new JFXPanel();


    FreeCollider collider1 = new FreeCollider(new PositionComponent(10, 10, 0), 10, 10, handler.getGridWorld());
    FreeCollider collider2 = new FreeCollider(new PositionComponent(14, 10, 0), 10, 10, handler.getGridWorld());
    FreeCollider collider3 = new FreeCollider(new PositionComponent(20, 20, 0), 10, 10, handler.getGridWorld());


    @Test
    void testForCollision() {
    }

    @Test
    void testForCollisionWithObject() {
        assertEquals(true, collider1.testForCollisionWithObject(collider2, collider1.pos.get()));
        assertEquals(false, collider3.testForCollisionWithObject(collider2, collider3.pos.get()));
    }

    @Test
    void localCollisionTest() {
        Tank tank1 = new Tank(handler, new Vec(10, 10), 0.2F);
        Tank tank2 = new Tank(handler, new Vec(90, 10), 0.2F);

        FreeCollider t1Collider = tank1.getComponent(FreeCollider.class);

        assertEquals(2, t1Collider.getCollides().length);
        System.out.println(handler.getGridWorld().getNearbyCollidables(handler.getGridWorld().getGridPosX(10), handler.getGridWorld().getGridPosY(10)));
        handler.getGridWorld().printGrid();
        int nearColliders = t1Collider.getNearby(tank1.pos.get(), t1Collider.getCollides()).size();

        assertEquals(2, nearColliders);
    }

    @Test
    void getCollision() {
//        Area a1 = new Area(new Rectangle(0, 0, 10, 10));
//        Area a2 = new Area(new Rectangle(0, 0, 10, 10));
//        assertEquals(true, newcCollider.getCollision(a1,a2));
//
//        Area a3 = new Area(new Rectangle(40, 19, 15, 10));
//        assertEquals(false, newcCollider.getCollision(a1,a3));
    }
}