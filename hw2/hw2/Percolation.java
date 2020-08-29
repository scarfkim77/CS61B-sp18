package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    int rowOrCol;
    int[] grid;
    WeightedQuickUnionUF gridUF;
    int virtualTopSite, virtualBottomSite;

    //Create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N < 0) {
            throw new IllegalArgumentException("N must be greater than 0!");
        }
        rowOrCol = N;
        grid = new int[rowOrCol*rowOrCol];
        for (int i = 0; i < rowOrCol * rowOrCol; i++) {
            grid[i] = -1;
        }
        //多初始化2格，分别留给virtualTopSite和virtualBottomSite
        gridUF = new WeightedQuickUnionUF(N*N+2);
        virtualTopSite = N * N;
        virtualBottomSite = N * N + 1;
    }

    //Open the site(row, col) if it is not open already
    public void open(int row, int col) {
        int index = row * rowOrCol + col;
        if (!isOpen(row, col)) {
            grid[index] = index;
        }
        try {
            if (isOpen(row, col - 1)) {
                gridUF.union(index, index - 1);
            }
            if (isOpen(row, col + 1)) {
                gridUF.union(index, index + 1);
            }
            if (isOpen(row - 1, col)) {
                gridUF.union(index, index - rowOrCol);
            }
            if (isOpen(row + 1, col)) {
                gridUF.union(index, index + rowOrCol);
            }
            if (row == 0) {
                gridUF.union(index, virtualTopSite);
            } else if (row == rowOrCol) {
                gridUF.union(index, virtualBottomSite);
            }
        } catch (IndexOutOfBoundsException e) {
            return;
        }
    }

    //Is the site(row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= rowOrCol || col < 0 || col >= rowOrCol) {
            throw new IndexOutOfBoundsException("The given site is not in the grid!");
        }
        int index = row * rowOrCol + col;
        return grid[index] != -1;
    }

    //Is the site(row, col) full?
    public boolean isFull(int row, int col) {
        if (isOpen(row, col)) {
            int index = row * rowOrCol + col;
            return gridUF.connected(virtualTopSite, index);
        }
        return false;
    }

    //Number of open sites
    public int numberOfOpenSites() {
        int num = 0;
        for (int i = 0; i < rowOrCol * rowOrCol; i++) {
                if (grid[i] != -1) {
                    num++;
            }
        }
        return num;
    }

    //Does the system percolates?
    public boolean percolates() {
        return gridUF.connected(virtualTopSite, virtualBottomSite);
    }

    // use for unit testing (not required)
    public static void main(String[] args) {
        Percolation test = new Percolation(3);
        System.out.println(test.rowOrCol);
        test.open(0, 2);
        /*test.open(0, 2);
        test.open(1, 2);
        test.open(2, 2);
        test.open(2, 0);
        System.out.println(test.percolates());
        System.out.println(test.isFull(2, 0));
        */
    }
}
