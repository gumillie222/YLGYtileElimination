package yanglegeyang;

import java.awt.*;
import java.util.*;

public class CardSlot {

    public static final int NUM_SLOTS = 8;
    public static final int WIDTH = 8 * Tile.WIDTH;
    public static final int HEIGHT = Tile.HEIGHT;
    public static final int X_CO = GameLayout.BOARD_WIDTH / 2 - 4 * Tile.WIDTH;
    public static final int Y_CO = GameLayout.BOARD_HEIGHT - HEIGHT;
    private final ArrayList<Tile> slots;
    private final boolean full;

    public CardSlot() {
        slots = new ArrayList<>();
        full = false;
    }

    public void add(Tile t) {
        int index = slots.size();
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).getPattern() == t.getPattern()) {
                index = i + 1;
            }
        }
        slots.add(index, t);
    }

    public void eliminate() {
        int[] patternCount = new int[9];
        for (int i = 0; i < patternCount.length; i++) {
            for (int j = 0; j < slots.size(); j++) {
                if (slots.get(j).getPattern() == i) {
                    patternCount[i]++;
                }
            }
        }
        // if 3 of a kind in slots, eliminate all 3
        int patternToBeEliminated = -1;
        for (int i = 0; i < patternCount.length; i++) {
            if (patternCount[i] >= 3) {
                patternToBeEliminated = i;
            }
        }
        // invariant: identical patterns are next to each other
        int counter = 0;
        while (slots.get(counter).getPattern() != patternToBeEliminated
                && counter + 1 < slots.size()) {
            counter++;
        }
        if (counter >= 0 && counter <= slots.size() - 2) {
            slots.remove(counter);
            slots.remove(counter);
            slots.remove(counter);
        }
    }

    public boolean checkLose() {
        return slots.size() >= NUM_SLOTS;
    }

    public void draw(Graphics g) {
        g.drawRect(X_CO, Y_CO, WIDTH, HEIGHT);
        int counter = 0;
        for (Tile t : slots) {
            t.draw(g, X_CO + counter * Tile.WIDTH, Y_CO);
            counter++;
        }
    }

    public int numSlotsFilled() {
        return slots.size();
    }

    public ArrayList<Tile> getSlots() {
        return slots;
    }
}
