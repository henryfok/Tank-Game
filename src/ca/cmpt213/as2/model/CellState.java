package ca.cmpt213.as2.model;

/**
 * Represent the state of a game-board cell.
 * An immutable class.
 */
public class CellState {
	private boolean hasBeenShot = false;
	private boolean hasTank = false;
	
	public CellState(boolean isShot, boolean hasTank) {
		this.hasBeenShot = isShot;
		this.hasTank = hasTank;
	}
	
	public boolean hasTank() {
		return hasTank;
	}
	public boolean hasBeenShot() {
		return hasBeenShot;
	}
	public boolean isHidden() {
		return !hasBeenShot;
	}

	// Create new instance based on current state (Immutable)
	public CellState makeHasBeenShot() {
		return new CellState(true, hasTank);
	}
	public CellState makeContainTank() {
		return new CellState(hasBeenShot, true);
	}

}
