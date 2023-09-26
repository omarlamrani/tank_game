package objects.grid;

import engine.handler.Handler;

public class CheckWallType {


    public static void SpawnWalls(int[][] BLOCK_MAP, Handler handler) {

        for (int i = 0; i < BLOCK_MAP.length; i++) {
            for (int j = 0; j < BLOCK_MAP[0].length; j++) {
                if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i, j + 1) == 1 && getBlock(BLOCK_MAP, i + 1, j) == 1 && getBlock(BLOCK_MAP, i, j - 1) == 1 && getBlock(BLOCK_MAP, i - 1, j) == 1) {
                    new Wall(handler, j, i, 5, 0, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i + 1, j) == 1 && getBlock(BLOCK_MAP, i, j - 1) == 1 && getBlock(BLOCK_MAP, i - 1, j) == 1) {
                    new Wall(handler, j, i, 4, 0, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i, j + 1) == 1 && getBlock(BLOCK_MAP, i, j - 1) == 1 && getBlock(BLOCK_MAP, i - 1, j) == 1) {
                    new Wall(handler, j, i, 4, 90, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i, j + 1) == 1 && getBlock(BLOCK_MAP, i + 1, j) == 1 && getBlock(BLOCK_MAP, i - 1, j) == 1) {
                    new Wall(handler, j, i, 4, 180, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i, j + 1) == 1 && getBlock(BLOCK_MAP, i + 1, j) == 1 && getBlock(BLOCK_MAP, i, j - 1) == 1) {
                    new Wall(handler, j, i, 4, -90, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i + 1, j) == 1 && getBlock(BLOCK_MAP, i - 1, j) == 1) {
                    new Wall(handler, j, i, 2, 0, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i, j + 1) == 1 && getBlock(BLOCK_MAP, i, j - 1) == 1) {
                    new Wall(handler, j, i, 2, 90, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i + 1, j) == 1 && getBlock(BLOCK_MAP, i, j - 1) == 1) {
                    new Wall(handler, j, i, 3, -90, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i, j - 1) == 1 && getBlock(BLOCK_MAP, i - 1, j) == 1) {
                    new Wall(handler, j, i, 3, 0, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i, j + 1) == 1 && getBlock(BLOCK_MAP, i - 1, j) == 1) {
                    new Wall(handler, j, i, 3, 90, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i, j + 1) == 1 && getBlock(BLOCK_MAP, i + 1, j) == 1) {
                    new Wall(handler, j, i, 3, 180, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i - 1, j) == 1) {
                    new Wall(handler, j, i, 1, 180, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i, j + 1) == 1) {
                    new Wall(handler, j, i, 1, -90, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i + 1, j) == 1) {
                    new Wall(handler, j, i, 1, 0, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i, j - 1) == 1) {
                    new Wall(handler, j, i, 1, 90, handler.getGridWorld());
                } else if (getBlock(BLOCK_MAP, i, j) == 1) {
                    new Wall(handler, j, i, 0, 0, handler.getGridWorld());
                }
                if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i + 1, j) == 1) {
                    new WallConnector(handler, j, i, true, handler.getGridWorld());
                }

                if (getBlock(BLOCK_MAP, i, j) == 1 && getBlock(BLOCK_MAP, i, j + 1) == 1) {
                    new WallConnector(handler, j, i, false, handler.getGridWorld());
                }

                //else if (getBlock(BLOCK_MAP, i,  j) == 2) new WorldBorder(handler, j, i, handler.gridWorld);

            }
        }

    }

    public static int getBlock(int[][] array, int y, int x) {
        if (x >= 0 && y >= 0 && x < array[0].length && y < array.length) return array[y][x];
        else return 0;
    }
}
