package ca.cmpt213.as2.model;

import java.util.ArrayList;

/**
 * Manage the game Fortress Defense game state.
 */
public class Game {
	public static final int NUMBER_TANKS = 5;

	private Fortress fortress = new Fortress();
	private GameBoard board = new GameBoard();
	private ArrayList<Tank> tanks = new ArrayList<Tank>();
	
	private ArrayList<Integer> latestTankDamages;
	private boolean lastPlayerShotHit;
	
	public Game() {
		for (int i = 0; i < NUMBER_TANKS; i++) {
			tanks.add(new Tank(board));
		}
	}

	public boolean hasUserWon() {
		for (Tank tank : tanks) {
			if (!tank.isDestroyed()) {
				return false;
			}
		}
		return true;
	}

	public boolean hasUserLost() {
		return getFortressHealth() == 0;
	}

	public int getFortressHealth() {
		return fortress.getHealth();
	}

	public void recordPlayerShot(CellLocation cell) {
		board.recordUserShot(cell);
		lastPlayerShotHit = board.cellContainsTank(cell);
	}

	public boolean didLastPlayerShotHit() {
		return lastPlayerShotHit;
	}

	public CellState getCellState(CellLocation cell) {
		return board.getCellState(cell);
	}

	public void fireTanks() {
		latestTankDamages = new ArrayList<Integer>();
		for (Tank tank : tanks) {
			int damage = tank.getShotDamage();
			if (damage > 0) {
				fortress.takeDamage(damage);
				latestTankDamages.add(damage);
			}
		}
	}

	public int[] getLatestTankDamages() {
		if (latestTankDamages == null) {
			return new int[0];
		}
		
		int[] damages = new int[latestTankDamages.size()];
		for (int i = 0; i < latestTankDamages.size(); i++) {
			damages[i] = latestTankDamages.get(i);
		}
		return damages;
	}
}
