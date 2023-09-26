package objects.RenderComponents;

import engine.components.PositionComponent;
import engine.components.RenderComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * Class that handles image rendering for the Landmine class.<br>
 * contains a toRender method that draws the sprite in the correct position, and a constructor method.
 */
public class LandmineRenderComponent extends RenderComponent {

    static Image mine = new Image("file:src/main/resources/landmine.png"); //landmine sprite

    /**
     * creates a new LandmineRenderComponent.
     * @param pos the position the sprite will be
     */
    public LandmineRenderComponent(PositionComponent pos) {super (pos);}

    /**
     * overrides the toRender function in the RenderComponent class. it draws the landmine sprite in the correct
     * location.<br>
     * @param gc the stage the sprite will appear on
     */
    @Override
    public void toRender(GraphicsContext gc) {
        gc.drawImage(mine, x.get() - 18.5, y.get() - 18.5);
        //-18.5 is half the width/height of the mine, this adjustment ensures the centre of the mine is placed at the
        //center of the grid square.
    }
}
