package ca.cmpt213.as2.model;

import java.security.InvalidParameterException;

/**
 * Represent a location in the game board (i.e., it's coordinates).
 * Handles conversion from a string (such as "A10") to its integer
 * (zero-offset) row and column index numbers.
 *
 */
public class CellLocation {
	private static final int MIN_TEXT_LENGTH = 2;
	private static final int TO_ZERO_OFFSET = 1;
	private static final int COL_INDEX_IN_STRING = 1;
	
	int rowIndex = 0;
	int colIndex = 0;

	public CellLocation(int row, int col) {
		rowIndex = row;
		colIndex = col;
	}

	public CellLocation(String input) {
		if (sourceStringTooShort(input)) {
			throw new InvalidParameterException("Not enough text.");
		}
		
		// Handle the row
		String firstLetter = input.substring(0, COL_INDEX_IN_STRING);
		setRow(charToIndex(firstLetter));

		// Handle the column (#)
		String laterCharacters = input.substring(COL_INDEX_IN_STRING);
		try {
			setCol(Integer.parseInt(laterCharacters) - TO_ZERO_OFFSET);
		} catch (NumberFormatException exception) {
			throw new InvalidParameterException("Invalid input format.");
		}
	}
	private boolean sourceStringTooShort(String input) {
		return input.length() < MIN_TEXT_LENGTH;
	}
	private int charToIndex(String firstLetter) {
		return firstLetter.toUpperCase().charAt(0) - 'A';
	}

	private void setRow(int row) {
		if (row < 0 || row >= GameBoard.NUMBER_ROWS) {
			throw new InvalidParameterException("Invalid row.");
		}
		rowIndex = row;
	}
	private void setCol(int col) {
		if (col < 0 || col >= GameBoard.NUMBER_COLS) {
			throw new InvalidParameterException("Invalid column number.");
		}
		colIndex = col;
	}
	
	public int getRowIndex() {
		return rowIndex;
	}
	public int getColIndex() {
		return colIndex;
	}

	public String toString() {
		return "Row " + rowIndex + "  Col " + colIndex;
	}

	public CellLocation add(CellLocation other) {
		return new CellLocation(
					this.rowIndex + other.rowIndex,
					this.colIndex + other.colIndex
				);
	}
	
	@Override
	public boolean equals(Object otherObject) {
		// Discussion of equals() method found at:
		// http://www.javapractices.com/topic/TopicAction.do?Id=17
		if (otherObject == this) {
			return true;
		}
		if (otherObject == null) {
			return false;
		}
		if (!(otherObject instanceof CellLocation)) {
			return false;
		}
		
		CellLocation other = (CellLocation) otherObject;
		boolean sameRow = this.rowIndex == other.rowIndex;
		boolean sameCol = this.colIndex == other.colIndex;
		return sameRow && sameCol;
	}

}
