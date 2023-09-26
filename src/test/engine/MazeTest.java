package engine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MazeTest {

    Maze newMaze = new Maze("AI Map", 10, 10);;

    int[][] plain10x10 = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 1, 1, 1, 1, 2, 1},
            {1, 2, 1, 0, 1, 0, 1, 0, 2, 1},
            {1, 2, 1, 1, 1, 1, 1, 1, 2, 1},
            {1, 2, 1, 0, 1, 0, 1, 0, 2, 1},
            {1, 2, 1, 1, 1, 1, 1, 1, 2, 1},
            {1, 2, 1, 0, 1, 0, 1, 0, 2, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

    @Test
    @DisplayName("Correctly converts Maze to a new Maze of the Same Length")
    void randomMazeLength() {
        int[][] aiMaze = newMaze.getFinalMaze();
        int originalLength = aiMaze.length;
        assertEquals(10, aiMaze.length);
    }


   @Test
   @DisplayName("Correctly converts Maze to a new Maze")
   void randomMazeNew() {
       int[][] aiMaze = newMaze.getFinalMaze();
        assertNotEquals(plain10x10, aiMaze);
   }

    @Test
    @DisplayName("Correctly initiates a correctly formatted Blank Maze")
    void blankMazeFormat(){
        Maze newerMaze = new Maze("Blank Maze", 10,10);
        assertArrayEquals(plain10x10, newerMaze.getFinalMaze());

   }
}
