package objects.grid;

import engine.Maze;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckWallTypeTest {
    private JFXPanel jfxPanel = new JFXPanel();
    int [][] finalMaze;

    @BeforeEach
    public void setUp() throws Exception {
        Maze maze = new Maze("AI Maze", 50, 50);
        finalMaze = maze.getFinalMaze();
    }

    @Test
    @DisplayName("Correctly Retrieves Correct Block from Array when Building Walls")
    void getBlock() {
        assertEquals(1, CheckWallType.getBlock(finalMaze,0,0));
        assertEquals(2, CheckWallType.getBlock(finalMaze,1,1));
        assertEquals(2, CheckWallType.getBlock(finalMaze,48,48));

    }

}