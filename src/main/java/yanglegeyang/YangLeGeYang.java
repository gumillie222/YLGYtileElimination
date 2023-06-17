package yanglegeyang;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;

public class YangLeGeYang {

    private static Layer[] tilePile;
    private static int[] patternCounter;
    private CardSlot cardSlots;
    private boolean gameOver;

    public final static int NUM_OF_LAYERS = 3;
    public final static int FST_LAYER_SIZE = 6;

    public final static int MAX_TILE_EACH_PATTERN = NUM_OF_LAYERS * FST_LAYER_SIZE * FST_LAYER_SIZE
            / 9;

    // For the creation of a new instance of YangLeGeYang, first try to read files that contain data
    // about piles and card slots. If files are not found, completely reset the game state. If files
    // are read, recover the state that was last saved.
    public YangLeGeYang() {
        BufferedReader brPiles = null;
        BufferedReader brCardSlots = null;
        try {
            brPiles = new BufferedReader(new FileReader("files/savePiles.txt"));
            brCardSlots = new BufferedReader(new FileReader("files/saveCardSlots.txt"));
        } catch (FileNotFoundException e) {
            reset();
        }
        if (brPiles != null && brCardSlots != null) {
            buildFromSaved(brPiles, brCardSlots);
            if (cardSlots.checkLose()) {
                gameOver = true;
            }
        } else {
            reset();
        }
    }

    public void reset() {
        tilePile = new Layer[NUM_OF_LAYERS];
        patternCounter = new int[9];
        for (int i = 0; i < tilePile.length; i++) {
            tilePile[i] = new Layer(FST_LAYER_SIZE, FST_LAYER_SIZE, false);
        }
        cardSlots = new CardSlot();
        gameOver = false;
    }

    // save progress of piles in a file
    // column 0: layer
    // column 1: i index of pile
    // column 2: j index of pile
    // column 3: pattern
    public void savePiles() {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("files/savePiles.txt"));
        } catch (IOException e) {
            e.getStackTrace();
        }
        for (int l = 0; l < tilePile.length; l++) {
            Tile[][] tiles = tilePile[l].getTileLayer();
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[0].length; j++) {
                    if (bw != null) {
                        try {
                            if (tiles[i][j] != null) {
                                bw.write(
                                        l + "," + i + "," + j
                                                + "," + tiles[i][j].getPattern() + "\n"
                                );
                            }
                        } catch (IOException e) {
                            e.getStackTrace();
                        }
                    }
                }
            }
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // save progress of piles in a file
    // column 0: position in card slot
    // column 1: pattern
    public void saveCardSlots() {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("files/saveCardSlots.txt"));
        } catch (IOException e) {
            e.getStackTrace();
        }
        for (int i = 0; i < cardSlots.getSlots().size(); i++) {
            if (bw != null) {
                try {
                    bw.write(
                            i + "," +
                                    cardSlots.getSlots().get(i).getPattern() + "\n"
                    );
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildFromSaved(BufferedReader brPiles, BufferedReader brCardSlots) {
        // build the tile piles from saved data
        // add all the data read from the file to a collection
        Collection<String[]> data = new LinkedList<>();
        tilePile = new Layer[NUM_OF_LAYERS];
        for (int c = 0; c < tilePile.length; c++) {
            tilePile[c] = new Layer(FST_LAYER_SIZE, FST_LAYER_SIZE, true);
        }
        try {
            String s = brPiles.readLine();
            while (s != null && s.split(",").length >= 4) {
                data.add(s.split(","));
                s = brPiles.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            for (String[] s : data) {
                int layer = Integer.valueOf(s[0]);
                int i = Integer.valueOf(s[1]);
                int j = Integer.valueOf(s[2]);
                int pattern = Integer.valueOf(s[3]);

                if (layer < 0 || layer >= tilePile.length ||
                        i < 0 || i >= FST_LAYER_SIZE || j < 0 || j >= FST_LAYER_SIZE ||
                        pattern < 0 || pattern >= 9) {
                    throw new IllegalArgumentException("invalid input data");
                }
                for (int k = 0; k < tilePile.length; k++) {
                    if (k == layer) {
                        tilePile[k].addTile(new Tile(pattern), i, j);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        // build the card slots from saved data
        cardSlots = new CardSlot();
        Collection<String[]> cardSlotData = new LinkedList<>();
        try {
            String s = brCardSlots.readLine();
            while (s != null && s.split(",").length >= 2) {
                cardSlotData.add(s.split(","));
                s = brCardSlots.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int counter = 0;
        for (String[] s : cardSlotData) {
            int pos = Integer.valueOf(s[0]);
            int pattern = Integer.valueOf(s[1]);
            if (pos == counter) {
                if (pattern < 0 || pattern >= 9) {
                    throw new IllegalArgumentException("invalid pattern input");
                }
                cardSlots.add(new Tile(pattern));
                counter++;
            } else {
                throw new IllegalArgumentException("Errors with card slot order");
            }
        }

    }

    public boolean moveTile(int xcoord, int ycoord) {
        int iLocOnGrid = (xcoord - Layer.X_START) / Tile.WIDTH;
        int jLocOnGrid = (ycoord - Layer.Y_START) / Tile.HEIGHT;
        if (iLocOnGrid < 0 || jLocOnGrid < 0 ||
                iLocOnGrid >= FST_LAYER_SIZE || jLocOnGrid >= FST_LAYER_SIZE) {
            return false;
        } else {
            Tile chosenTile = null;
            for (int i = tilePile.length - 1; i >= 0; i--) {
                chosenTile = tilePile[i].getTileLayer()[iLocOnGrid][jLocOnGrid];
                if (chosenTile != null) {
                    cardSlots.add(chosenTile);
                    tilePile[i].getTileLayer()[iLocOnGrid][jLocOnGrid] = null;
                    cardSlots.eliminate();
                    if (cardSlots.checkLose()) {
                        gameOver = true;
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public static Layer[] getTilePile() {
        return tilePile;
    }

    public CardSlot getCardSlot() {
        return cardSlots;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean hasWon() {
        for (Layer l : tilePile) {
            if (!l.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static int[] getPatternCounter() {
        return patternCounter;
    }

    public static void incrementPatternCounter(int pattern) {
        patternCounter[pattern]++;
    }

    public static void main(String[] args) {
        YangLeGeYang y = new YangLeGeYang();
    }

}
