package yanglegeyang;

import javax.swing.*;
import java.awt.*;

public class RunYLGY implements Runnable {

    public void run() {

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Yang Le Ge Yang");
        frame.setLocation(0, 0);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Try to eliminate by collecting 3 of the same kind.");
        status_panel.add(status);

        // Game board
        final GameLayout layout = new GameLayout(status);
        frame.add(layout, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> layout.reset());
        control_panel.add(reset);

        final JButton save = new JButton("Save");
        save.addActionListener(e -> layout.saveProgress());
        control_panel.add(save);

        final JButton instruction = new JButton("Instruction");
        instruction.addActionListener(e -> popUpInstruction());
        control_panel.add(instruction);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    private void popUpInstruction() {
        final JFrame instruction_frame = new JFrame("Instruction");
        instruction_frame.setLocation(0, 0);

        final JPanel instruction_panel = new JPanel();
        instruction_frame.add(instruction_panel, BorderLayout.CENTER);


        final JTextArea instruction = new JTextArea(" INSTRUCTION \n\n" +
                " Yang Le Ge Yang is a tile elimination game. \n" +
                " Click the tile to move it from the pile to \n" +
                " the card slots at the bottom of the screen. \n" +
                " There are three layers in the pile. Once \n" +
                " there are three of the same kind at the \n" +
                " bottom card slots, all three of them get \n" +
                " eliminated. Your end goal is to eliminate \n" +
                " all the tiles on the screen. If all the card \n" +
                " slots get filled before all tiles are \n" +
                " eliminated, you lose the game. The game won't \n" +
                " automatically save, so make sure to click the \n" +
                " save button before quitting if you hope to save \n" +
                " your progress. If you don't save, the game will \n" +
                " restart from the last saved state. Click the \n" +
                " reset button if you hope to start over. \n");
        instruction_frame.add(instruction);



        instruction_frame.pack();
        instruction_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        instruction_frame.setVisible(true);

    }

}
