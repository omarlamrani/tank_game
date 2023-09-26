package objects.projectiles;

import engine.Maze;
import engine.Vec;
import engine.handler.Handler;
import engine.handler.HeadlessHandler;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ProjectileTest {

    private JFXPanel jfxPanel = new JFXPanel();
    private Projectile p;
    Vec vector;
    Vec vector2;

    @BeforeEach
    public void setUp(){
        Handler handler = new HeadlessHandler(10, 10);
        vector = new Vec(4,10);
        vector2 = new Vec(4,10);

        p = new Projectile(handler, vector, vector2);
    }

    @Test
    @DisplayName("Tests Move Function of Projectile")
    void move() {
        int depth = 0;
        assertEquals(false , p.move(vector,depth));
        assertNotEquals(true , p.move(vector,depth+2));

    }

}