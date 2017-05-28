package ca.cmpt213.as2.model;

import javax.swing.*;
import java.security.InvalidParameterException;

public class TankGameLogic {
    private Game game;

    public TankGameLogic(Game game) {
        this.game = game;
    }

    public boolean doPlayerShot(int row, int col) {
        CellLocation cell = getPlayerMove(row, col);
        game.recordPlayerShot(cell);
        if (game.didLastPlayerShotHit()) {
            System.out.println("HIT!");
        } else {
            System.out.println("Miss.");
        }
        return game.didLastPlayerShotHit();
    }

    private CellLocation getPlayerMove(int row, int col) {
        row++;
        col++;
        String row_char = String.valueOf((char) (row + 'A' - 1));
        String input = row_char + Integer.toString(col);
        System.out.print("Entered move: " + input + " . . . ");
        try {
            return new CellLocation(input);
        } catch (InvalidParameterException exception) {
            System.out.println("Invalid target. Please enter a coordinate such as D10.");
            return new CellLocation(1, 1);
        }
    }

    public void doEnemyTanksShot() {
        game.fireTanks();
    }

    public JTextArea getDamageTaken(JTextArea textArea) {
        textArea.setText("");
        int[] damages = game.getLatestTankDamages();
        for (int damage : damages) {
            textArea.append("You were shot for " + Integer.toString(damage) + "!\n");
        }
        return textArea;
    }
}