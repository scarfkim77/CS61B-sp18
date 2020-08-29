package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int rowOrCol, size, numberOfOpenSites;
    private int[] grid;
    private WeightedQuickUnionUF gridUF;
    private int virtualTopSite, virtualBottomSite;

    //Create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N < 0) {
            throw new IllegalArgumentException("N must be greater than 0!");
        }
        rowOrCol = N;
        size = rowOrCol * rowOrCol;
        numberOfOpenSites = 0;
        grid = new int[size];
        for (int i = 0; i < size; i++) {
            grid[i] = -1;
        }
        //多初始化2格，分别留给virtualTopSite和virtualBottomSite
        gridUF = new WeightedQuickUnionUF(size + 2);
        virtualTopSite = size;
        virtualBottomSite = size + 1;
    }

    //Change the row and col into 1D-index
    private int getIndex(int row, int col) {
        return row * rowOrCol + col;
    }

    //Check if the site is legal or not
    private boolean isValid(int row, int col) {
        if (row < 0 || row >= rowOrCol || col < 0 || col >= rowOrCol) {
            return false;
        }
        return true;
    }

    //Open the site(row, col) if it is not open already
    public void open(int row, int col) {
        int index = getIndex(row, col);
        if (!isOpen(row, col)) {
            grid[index] = index;
            numberOfOpenSites++;
        }
        try {
            if (isValid(row, col - 1) && isOpen(row, col - 1)) {
                gridUF.union(index, index - 1);
            }
            if (isValid(row, col + 1) && isOpen(row, col + 1)) {
                gridUF.union(index, index + 1);
            }
            if (isValid(row - 1, col) && isOpen(row - 1, col)) {
                gridUF.union(index, index - rowOrCol);
            }
            if (isValid(row + 1, col) && isOpen(row + 1, col)) {
                gridUF.union(index, index + rowOrCol);
            }
            if (row == 0) {
                gridUF.union(index, virtualTopSite);
            }
            if (row == rowOrCol - 1) {
                gridUF.union(index, virtualBottomSite);
            }
        } catch (IndexOutOfBoundsException e) {
            return;
        }
    }

    //Is the site(row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isValid(row, col)) {
            throw new IndexOutOfBoundsException("The given site is not in the grid!");
        }
        int index = getIndex(row, col);
        return grid[index] != -1;
    }

    //Is the site(row, col) full?
    public boolean isFull(int row, int col) {
        if (isOpen(row, col)) {
            int index = getIndex(row, col);
            return gridUF.connected(virtualTopSite, index);
        }
        return false;
    }

    //Number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    //Does the system percolates?
    public boolean percolates() {
        return gridUF.connected(virtualTopSite, virtualBottomSite);
    }

    // use for unit testing (not required)
    public static void main(String[] args) {
        Percolation test = new Percolation(1);
        test.open(0, 0);
        /*test.open(1, 2);
        test.open(2, 2);
        test.open(2, 0);*/
        System.out.println(test.percolates());
        //System.out.println(test.isFull(0, 2));
    }
}
