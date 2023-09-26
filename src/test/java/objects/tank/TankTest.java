package objects.tank;

import engine.Maze;
import engine.handler.Handler;
import engine.handler.HeadlessHandler;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TankTest {
      private JFXPanel jfxPanel = new JFXPanel();
      private Tank t;

      @BeforeEach
      public void setUp(){
          Handler handler = new HeadlessHandler(10, 10);
          t = new Tank(handler, handler.getGridWorld().getSpawn(), 0.2F);
     }

    @Test
    @DisplayName("Correctly Returns Health Points")
    void getHealthPoints() {
        assertEquals(100, t.getHealthPoints());
        assertNotEquals(15, t.getHealthPoints());

    }

    @Test
    @DisplayName("Correctly Reduces Health Points When Hit")
    void reduceHealthPoints() {
        t.reduceHealthPoints(10.0f);
        assertEquals(90, t.getHealthPoints());
        t.reduceHealthPoints(30.0f);
        assertEquals(60, t.getHealthPoints());
    }
}