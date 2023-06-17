package yanglegeyang;

import java.awt.*;

public class Layer {

    public static final int X_START = GameLayout.BOARD_WIDTH / 2
            - YangLeGeYang.FST_LAYER_SIZE / 2 * Tile.WIDTH;
    public static final int Y_START = 100;

    private final Tile[][] tileLayer;

    public Layer(int width, int height, boolean empty) {
        tileLayer = new Tile[width][height];

        if (!empty) {
            for (int i = 0; i < tileLayer.length; i++) {
                for (int j = 0; j < tileLayer[0].length; j++) {
                    int pattern = (int) (Math.random() * 9);
                    while (YangLeGeYang.getPatternCounter()[pattern] >=
                            YangLeGeYang.MAX_TILE_EACH_PATTERN) {
                        pattern = (int) (Math.random() * 9);
                    }
                    YangLeGeYang.incrementPatternCounter(pattern);
                    tileLayer[i][j] = new Tile(pattern);
                }
            }
        }

    }

    public void addTile(Tile t, int i, int j) {
        tileLayer[i][j] = t;
    }

    public void draw(Graphics g) {
        for (int i = 0; i < tileLayer.length; i++) {
            for (int j = 0; j < tileLayer[0].length; j++) {
                if (tileLayer[i][j] != null) {
                    tileLayer[i][j].draw(g, X_START + i * Tile.WIDTH,
                            Y_START + j * Tile.HEIGHT);
                }
            }
        }
    }

    public boolean isEmpty() {
        for (Tile[] t : tileLayer) {
            for (Tile t2 : t) {
                if (t2 != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public Tile[][] getTileLayer() {
        return tileLayer;
    }
}
