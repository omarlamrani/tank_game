package engine;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSheet {
    private final BufferedImage image;

    public ImageSheet(File filepath) throws IOException {
        image = ImageIO.read(filepath);
    }

    public static Image getSubImage(File filepath, int x, int y, int w, int h) {
        try {
            return SwingFXUtils.toFXImage(ImageIO.read(filepath).getSubimage(x, y, w, h), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Image getSubImage(int x, int y, int w, int h) {
        return SwingFXUtils.toFXImage(image.getSubimage(x, y, w, h), null);
    }

}
