package engine;

public class Maze {

    private int[][] finalMaze;

    public Maze(String mazeType, int rows, int columns){

        if (mazeType == "AI Maze"){
            int[][] formattedMaze = altMaze("Blank Maze",rows,columns);
            finalMaze = randomMaze(formattedMaze);
        } else {
            finalMaze = altMaze(mazeType, rows, columns);
        }

    }

    //generates alternative Maze
    public int[][] altMaze(String mazeType, int rows, int columns) {
        int[][] altMaze = new int[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (i == 0 || i == columns - 1 || j == 0 || j == rows - 1) {
                    altMaze[i][j] = 3;
                } else if (i == 1 || i == columns - 2 || j == 1 || j == rows - 2) {
                    altMaze[i][j] = 2;
                }
//                Open Maze
                else if (mazeType=="Open"){
                    if (Math.random() > 0.7) altMaze[i][j]=1;
                }
//                Simple Maze
                else if (mazeType=="Simple Maze") {
                    if (j % 4 == 0) { //every 5th rows
                        if (j % 8 == 0) {
                            if (i > (columns / 3)) {
                                altMaze[i][j] = 1;
                                altMaze[i][j-1] = 1;
                            }

                        } else {
                            if (i < ((columns / 3) * 2)) {
                                altMaze[i][j] = 1;
                                altMaze[i][j-1] = 1;
                            }
                        }
                    }
                }
//                Blank Maze
                else if (mazeType=="Blank Maze") {
                    if (j % 2 != 0) { //even rows
                        if (i % 2 != 0) {
                            altMaze[i][j] = 0;
                        } else {
                            altMaze[i][j] = 1;
                        }
                    } else {
                        altMaze[i][j] = 1;
                    }
                }
                else {
                    altMaze[i][j] = 0;
                }
            }
        }

//        adds Outer Walls
        for (int i = 0; i < altMaze.length; i++) {
            for (int j = 0; j < altMaze[0].length; j++) {
                if (altMaze[i][j] == 3) altMaze[i][j] = 1;
                else if (altMaze[i][j] == 2 && mazeType=="Simple Maze") altMaze[i][j] = 1; //adds double outer walls
            }
        }
        return altMaze;
    }

    //auto generates a maze using a binary search tree algorithm, carving walls north and west to form a maze structure from a plain formatted grid
    public int[][] randomMaze(int[][] plainGrid) {

        for (int i = 1; i < plainGrid.length; i++) {
            for (int j = 1; j < plainGrid[0].length - 1; j++) {
                if (plainGrid[i][j] == 0) {
                    int north = plainGrid[i - 1][j];
                    int west = plainGrid[i][j - 1];
                    //randomly carves north or west
                    if (north == 1 && west == 1) {
                        if (Math.random() > 0.5) {
                            plainGrid[i - 1][j] = 0;
                        } else {
                            plainGrid[i][j - 1] = 0;
                        }
                    }
                    //carves path either north or west if only one available
                    else if (north != 1 && west == 1) plainGrid[i][j - 1] = 0;
                    else if (north == 1 && west != 1) plainGrid[i - 1][j] = 0;
                }
            }
        }

        for (int i = 0; i < plainGrid.length; i++) {
            for (int j = 0; j < plainGrid[0].length; j++) {
                if (plainGrid[i][j] == 3) {
                    plainGrid[i][j] = 1;
                }
            }
        }

        return plainGrid;
    }

    public int[][] getFinalMaze() {
        return finalMaze;
    }
}

