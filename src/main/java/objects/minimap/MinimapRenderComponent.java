package objects.minimap;

import engine.components.PositionComponent;
import engine.components.RenderComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MinimapRenderComponent extends RenderComponent {
    private static final int MAP_SIZE = 7;
    private static final Color deafult = new Color(0.65, 0, 0, 1);

    int[][] simpleMap = {};

    public MinimapRenderComponent(PositionComponent pos, int z) {
        super(pos, z);
        transformable = false;
    }

    public void setMap(int[][] map) {
        this.simpleMap = map;
    }

    @Override
    public void toRender(GraphicsContext gc) {
        //if you don't override toRender you get an easily recognisable missing texture
        for (int i = 0; i < simpleMap.length; i++) {
            for (int j = 0; j < simpleMap[0].length; j++) {

                gc.setFill(new Color(0, 0.2, 0, 0.5));
                if (simpleMap[i][j] == 1) gc.setFill(new Color(0.5, 0, 0, 0.5));
                else if (simpleMap[i][j] > 1 || simpleMap[i][j] < 0) {
                    gc.setFill(deafult.deriveColor((float) (simpleMap[i][j] - 2) / 1000F, 1, 20, 1));
                }


                gc.fillRect(x.get() + j * MAP_SIZE, y.get() + i * MAP_SIZE, MAP_SIZE, MAP_SIZE);
                //System.out.println(Arrays.deepToString(simpleMap));
            }
        }
    }

}
