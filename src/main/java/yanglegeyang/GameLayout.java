package yanglegeyang;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class GameLayout extends JPanel {

    // Game constants
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 600;

    private final YangLeGeYang ylgy;
    private final JLabel status;

    public GameLayout(JLabel statusInit) {
        ylgy = new YangLeGeYang();
        status = statusInit;
        updateStatus();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                if (!ylgy.isGameOver()) {
                    ylgy.moveTile(p.x, p.y);
                    updateStatus();
                }
                repaint(); // repaints the game board
            }
        });
    }

    private void updateStatus() {
        if (ylgy.isGameOver()) {
            status.setText("You lost.");
        } else if (ylgy.hasWon()) {
            status.setText("You won.");
        } else {
            status.setText("Try to eliminate by collecting 3 of the same kind.");
        }
    }

    public void reset() {
        ylgy.reset();
        repaint();
        requestFocusInWindow();
        updateStatus();
    }

    public void saveProgress() {
        ylgy.savePiles();
        ylgy.saveCardSlots();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        for (Layer l : YangLeGeYang.getTilePile()) {
            l.draw(g);
        }
        ylgy.getCardSlot().draw(g);
    }

    private void drawBackground(Graphics g) {
        Image img;
        try {
            img = ImageIO.read(new File("files/background.png"));
        } catch (IOException e) {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        g.drawImage(img, 0, 0, BOARD_WIDTH, BOARD_HEIGHT, null);
    }

    public YangLeGeYang getYlgy() {
        return ylgy;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

}
