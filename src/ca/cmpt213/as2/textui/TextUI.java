package ca.cmpt213.as2.textui;

import java.security.InvalidParameterException;
import java.util.Scanner;

import ca.cmpt213.as2.model.CellLocation;
import ca.cmpt213.as2.model.CellState;
import ca.cmpt213.as2.model.Game;
import ca.cmpt213.as2.model.GameBoard;

/**
 * Handle the text-based display and keyboard based interaction for
 * running the Fortress Defense game.
 */
public class TextUI {
	private static final char SYMBOL_FOG = '~';
	private static final char SYMBOL_TANK = 'X';
	private static final char SYMBOL_MISS = '.';
	private static final char SYMBOL_NOTHING = ' ';
	
	
	private Game game;
	private boolean revealBoard = false;

	public TextUI(Game game) {
		this.game = game;
	}

	public void playGame() {
		displayWelcome();
		displayBoard();
		while (gameRunning()) {
			doPlayerShot();
			doEnemyTanksShot();
			displayBoard();
		}
		doWonOrLost();
	}

	private void displayWelcome() {
		System.out.println("----------------------------");
		System.out.println("Welcome to Fortress Defense!");
		System.out.println("by Brian Fraser");
		System.out.println("----------------------------");
		System.out.println("");
	}

	private boolean gameRunning() {
		return !game.hasUserWon() && !game.hasUserLost();
	}

	private void displayBoard() {
		System.out.println();
		System.out.println("Game Board:");
		
		// Print column headings:
		System.out.printf("%5s", "");
		for (int col = 0; col < GameBoard.NUMBER_COLS; col++) { 
			System.out.printf("%3d", col + 1);
		}
		System.out.println();
		
		// Print rows: 
		for (int row = 0; row < GameBoard.NUMBER_ROWS; row++) {
			System.out.printf("%5c", 'A' + row);
			for (int col = 0; col < GameBoard.NUMBER_COLS; col++) {
				CellLocation cell = new CellLocation(row, col);
				CellState state = game.getCellState(cell);
				char symbol = convertCellStateToSymbol(state);
				System.out.printf("%3c", symbol);
			}
			System.out.println();
		}
		
		// Print structural strength left:
		System.out.printf("Fortress Structure Left: %d.%n",
				game.getFortressHealth());
	}

	private char convertCellStateToSymbol(CellState state) {
		if (state.isHidden() && !revealBoard) {
			return SYMBOL_FOG;
		} else if (state.hasTank()) {
			return SYMBOL_TANK;
		} else if (state.hasBeenShot()){
			return SYMBOL_MISS;
		} else {
			return SYMBOL_NOTHING;
		}
	}

	private void doPlayerShot() {
		CellLocation cell = getPlayerMove();
		game.recordPlayerShot(cell);
		if (game.didLastPlayerShotHit()) {
			System.out.println("HIT!");
		} else {
			System.out.println("Miss.");
		}
	}

	private CellLocation getPlayerMove() {
		while (true) {
			System.out.print("Enter your move: ");

			// Don't warn about un-closed System.in Scanner.
			// Don't close System.in or it won't read user input.
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			
			try {
				CellLocation location = new CellLocation(input);
				return location;
			} catch (InvalidParameterException exception) {
				System.out.println("Invalid target. Please enter a corrdinate such as D10.");
			}
		}
	}

	private void doEnemyTanksShot() {
		game.fireTanks();
		int[] damages = game.getLatestTankDamages();
		
		for (int damage : damages) {
			System.out.printf("You were shot for %d!%n", 
					damage);
		}		
	}

	private void doWonOrLost() {
		if (game.hasUserWon()) {
			System.out.println("Congratulations! You won!");
		} else if (game.hasUserLost()) {
			System.out.println("I'm sorry, your fortress has been smashed!");
			revealBoard();
		} else {
			assert false;
		}
	}
	
	public void revealBoard() {
		revealBoard = true;
		displayBoard();
	}

}
