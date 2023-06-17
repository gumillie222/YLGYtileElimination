package yanglegeyang;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tile {

    public static final String STRAWB_FILE = "files/strawberry.png";
    public static final String KIWI_FILE = "files/kiwi.png";
    public static final String PINEA_FILE = "files/pineapple.png";
    public static final String WTML_FILE = "files/watermelon.png";
    public static final String MANGO_FILE = "files/mango.png";
    public static final String BANANA_FILE = "files/banana.png";

    public static final String POM_FILE = "files/pomegranate.png";

    public static final String CHER_FILE = "files/cherry.png";

    public static final String ORAN_FILE = "files/orange.png";

    public static final int HEIGHT = 50;
    public static final int WIDTH = 40;

    private BufferedImage img;
    private final int pattern;

    public Tile(int pattern) {
        try {
            if (img == null) {
                switch (pattern) {
                    case 0 -> img = ImageIO.read(new File(STRAWB_FILE));
                    case 1 -> img = ImageIO.read(new File(KIWI_FILE));
                    case 2 -> img = ImageIO.read(new File(PINEA_FILE));
                    case 3 -> img = ImageIO.read(new File(WTML_FILE));
                    case 4 -> img = ImageIO.read(new File(MANGO_FILE));
                    case 5 -> img = ImageIO.read(new File(BANANA_FILE));
                    case 6 -> img = ImageIO.read(new File(POM_FILE));
                    case 7 -> img = ImageIO.read(new File(CHER_FILE));
                    case 8 -> img = ImageIO.read(new File(ORAN_FILE));
                    default -> System.out.println("Error: Tile does not exist");
                }
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        this.pattern = pattern;
    }

    public int getPattern() {
        return pattern;
    }

    public void draw(Graphics g, int x, int y) {
        g.drawImage(img, x, y, WIDTH, HEIGHT, null);
    }

}
