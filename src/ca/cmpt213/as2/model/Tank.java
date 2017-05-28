package ca.cmpt213.as2.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represent a tank, including its location and shape.
 * Able to describe tank's health and damage done by using
 * the GameBoard to find out where the user has shot.
 */
public class Tank {
	GameBoard board;
	CellLocation startCell;
	Tetromino shape = new Tetromino();
	
	// Game designed to have damage fall off very quickly.
	private final static int[] DAMAGE_DONE_PER_UNDAMAGE_CELLS = {0, 1, 2, 5, 20};

	public Tank(GameBoard board) {
		this.board = board;
		placeOnBoard();
	}

	private void placeOnBoard() {
		ArrayList<CellLocation> positions = getAllPossibleLocations();
		for (CellLocation position : positions) {
			if (fitsOnBoardAtPosition(position)) {
				placeOnBoardAtPosition(position);
				return;
			}
		}
		assert false;
	}

	private ArrayList<CellLocation> getAllPossibleLocations() {
		ArrayList<CellLocation> list = new ArrayList<CellLocation>();
		for (int row = 0; row < GameBoard.NUMBER_ROWS; row++) {
			for (int col = 0; col < GameBoard.NUMBER_COLS; col++) {
				list.add(new CellLocation(row, col));
			}
		}
		Collections.shuffle(list);
		return list;
	}
	
	private boolean fitsOnBoardAtPosition(CellLocation position) {
		for (CellLocation shapeCell : shape.getCellLocations()) {
			
			CellLocation realCell = position.add(shapeCell);
			
			if (!board.cellOpenForTank(realCell)) {
				return false;
			}
		}
		return true;
	}

	private void placeOnBoardAtPosition(CellLocation position) {
		startCell = position;
		for (CellLocation shapeCell : shape.getCellLocations()) {
			
			CellLocation realCell = position.add(shapeCell);
			
			board.recordTankInCell(realCell);
		}
	}
	
	public int getUndamagedCellCount() {
		int count = 0;
		for (CellLocation shapeCell : shape.getCellLocations()) {
			CellLocation realCell = startCell.add(shapeCell);
			if (!board.hasCellBeenShot(realCell)) {
				count ++;
			}
		}
		return count;
	}

	public int getShotDamage() {
		return DAMAGE_DONE_PER_UNDAMAGE_CELLS[getUndamagedCellCount()];
	}
	
	public boolean isDestroyed() {
		return getUndamagedCellCount() == 0;
	}

}
