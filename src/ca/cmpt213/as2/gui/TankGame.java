package ca.cmpt213.as2.gui;

import ca.cmpt213.as2.model.CellLocation;
import ca.cmpt213.as2.model.Game;
import ca.cmpt213.as2.model.TankGameLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class TankGame {
    private static final int NUMBER_ROWS = 10;
    private static final int NUMBER_COLS = 10;
    private static Game game;
    private static TankGameLogic logic;
    private boolean GAME_ENDED = false;
    private ImageIcon field;
    private ImageIcon fog;
    private ImageIcon hit;
    private ImageIcon miss;
    private ImageIcon tank;
    private JLabel healthLabel;
    private JTextArea messageBox;
    private JButton[][] buttons = new JButton[10][10];

    public static void main(String[] args) {
        game = new Game();
        logic = new TankGameLogic(game);
        new TankGame(game, logic);
    }

    public TankGame(Game game, TankGameLogic logic) {
        loadImages();
        JFrame frame = new JFrame("Tank Battlefield");
        frame.setLayout(new BorderLayout());

        frame.add(fortressHealth(game), BorderLayout.NORTH);
        frame.add(fieldGrid(logic), BorderLayout.CENTER);
        frame.add(statusBox(), BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(910,1000));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JLabel fortressHealth(Game game) {
        healthLabel = new JLabel("Fortress Health: " + Integer.toString(game.getFortressHealth()));
        healthLabel.setOpaque(true);
        healthLabel.setBackground(Color.GREEN);
        return healthLabel;
    }

    private void updateHealth() {
        int newHealth = game.getFortressHealth();
        healthLabel.setText("Fortress Health: " + Integer.toString(newHealth));
        if (newHealth > 750) {
            healthLabel.setBackground(Color.GREEN);
        }
        if (newHealth <= 750 && newHealth > 375) {
            healthLabel.setBackground(Color.YELLOW);
        }
        if (newHealth <= 375) {
            healthLabel.setBackground(Color.RED);
        }
    }

    private void doWonOrLost() {
        if (game.hasUserWon()) {
            GAME_ENDED = true;
            makeResultWindow("Congratulations! You won!");
            revealBoard();
        } else if (game.hasUserLost()) {
            GAME_ENDED = true;
            makeResultWindow("I'm sorry, your fortress has been smashed!");
            revealBoard();
        }
    }

    private void makeResultWindow(String result) {
        JFrame endFrame = new JFrame("Message");
        JOptionPane infoPane = new JOptionPane(result, JOptionPane.INFORMATION_MESSAGE);
        infoPane.setOptions(new Object[]{okButton(endFrame)});
        endFrame.add(infoPane);
        endFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        endFrame.pack();
        endFrame.setLocationRelativeTo(null);
        endFrame.setVisible(true);
    }

    private JButton okButton(JFrame endGameWindow) {
        JButton ok_button = new JButton("OK");
        ok_button.addActionListener(click -> {
            endGameWindow.setVisible(false);
            endGameWindow.dispose();
        });
        return ok_button;
    }

    private JTextArea statusBox() {
        messageBox = new JTextArea(6, 25);
        messageBox.setText("Choose a location to fire at.");
        return messageBox;
    }

    private void updateStatusBox() {
        logic.getDamageTaken(messageBox);
    }

    private JPanel fieldGrid(TankGameLogic logic) {
        JPanel grid = new JPanel(new GridLayout(NUMBER_ROWS, NUMBER_COLS));
        for (int row = 0; row < NUMBER_ROWS; row++) {
            for (int col = 0; col < NUMBER_COLS; col++) {
                grid.add(makeButton(logic, row, col));
            }
        }
        return grid;
    }

    private Component makeButton(TankGameLogic logic, int row, int col) {
        buttons[row][col] = new JButton(fog);
        //buttons.add(button);
        buttons[row][col].addActionListener(grid_click -> {
            if (GAME_ENDED) return;
            if (logic.doPlayerShot(row, col)) {
                buttons[row][col].setIcon(hit);
            } else buttons[row][col].setIcon(miss);
            logic.doEnemyTanksShot();
            updateHealth();
            updateStatusBox();
            doWonOrLost();
        });
        return buttons[row][col];
    }

    private void revealBoard() {
        int counter = 0;
        for (int row = 0; row < NUMBER_ROWS; row++) {
            for (int col = 0; col < NUMBER_COLS; col++) {
                if (game.getCellState(new CellLocation(row, col)).hasTank()) {
                    if (game.getCellState(new CellLocation(row, col)).hasBeenShot()) {
                        buttons[row][col].setIcon(hit);
                        counter++;
                    } else {
                        buttons[row][col].setIcon(tank);
                        counter++;
                    }
                } else if (!(game.getCellState(new CellLocation(row, col)).hasTank())) {
                    if (game.getCellState(new CellLocation(row, col)).hasBeenShot()) {
                        buttons[row][col].setIcon(miss);
                        counter++;
                    } else {
                        buttons[row][col].setIcon(field);
                        counter++;
                    }
                } else {
                    buttons[row][col].setIcon(field);
                    counter++;
                }
            }
        }
    }

    private void loadImages() {
        try {
            field = resizeImage(new ImageIcon(this.getClass().getResource("/images/field.jpg")));
            fog = resizeImage(new ImageIcon(this.getClass().getResource("/images/fog.png")));
            hit = resizeImage(new ImageIcon(this.getClass().getResource("/images/hit.png")));
            miss = resizeImage(new ImageIcon(this.getClass().getResource("/images/miss.png")));
            tank = resizeImage(new ImageIcon(this.getClass().getResource("/images/tank.png")));
        } catch (NullPointerException error) {
            System.err.println("Could not load game images");
        }
    }

    private ImageIcon resizeImage(ImageIcon image) {
        return getScaleImageIcon(image, 90, 90);
    }

    private static ImageIcon getScaleImageIcon(ImageIcon icon, int width, int height) {
        return new ImageIcon(getScaledImage(icon.getImage(), width, height));
    }

    static private Image getScaledImage(Image srcImg, int width, int height) {
        BufferedImage resizedImg =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, width, height, null);
        g2.dispose();
        return resizedImg;
    }
}