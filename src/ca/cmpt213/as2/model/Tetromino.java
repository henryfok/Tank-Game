package ca.cmpt213.as2.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A four-cell connected shape (a tetromino). Handles creation and
 * access to the cells the shape covers. Starts at a relative (0,0), and grows 
 * randomly from there (+ or -).
 */
public class Tetromino {
	public static final int NUM_CELLS = 4;

	private static final int NUM_DIRECTIONS = 4;
	
	ArrayList<CellLocation> cells = new ArrayList<CellLocation>();
	
	public Tetromino() {
		cells.add(new CellLocation(0, 0));
		for (int i = 1; i < NUM_CELLS; i ++) {
			growTetromino();
		}
	}
	
	private void growTetromino() {
		// Pick random cell to grow from
		ArrayList<Integer> cellsToGrowFrom = generatePermutationOf0ToNMinus1(cells.size());
		for (int cellGrowFormIdx : cellsToGrowFrom) {
			int growFromRow = cells.get(cellGrowFormIdx).getRowIndex();
			int growFromCol = cells.get(cellGrowFormIdx).getColIndex();
			
			// Pick a random direction:
			ArrayList<Integer> directions = generatePermutationOf0ToNMinus1(NUM_DIRECTIONS);
			for (int direction : directions) {
				int newRow = growFromRow;
				int newCol = growFromCol;

				switch (direction) { 
				case 0: newRow++; break; 
				case 1: newRow--; break; 
				case 2: newCol++; break; 
				case 3: newCol--; break; 
				default:
					assert false;
				}
				
				CellLocation newLoc = new CellLocation(newRow, newCol);
				if (!cells.contains(newLoc)) {
					cells.add(newLoc);
					return;
				}
			}
		}
		assert false;
	}
	
	private ArrayList<Integer> generatePermutationOf0ToNMinus1(int n) {
		ArrayList<Integer> permutation = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			permutation.add(i);
		}
		
		Collections.shuffle(permutation);
		return permutation;
	}


	public CellLocation[] getCellLocations() {
		CellLocation[] array = new CellLocation[cells.size()];
		cells.toArray(array);
		return  array;
	}
}