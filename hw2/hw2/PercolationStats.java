package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private double[] p;
    private int times;
    private double mean, stddev;

    //Perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T must be greater than 0!");
        }
        times = T;
        p = new double[times];
        for (int i = 0; i < times; i++) {
            Percolation experiment = pf.make(N);
            while (!experiment.percolates()) {
                int randomRow = (int) StdRandom.uniform() * N;
                int randomCol = (int) StdRandom.uniform() * N;
                experiment.open(randomRow, randomCol);
            }
            p[i] = experiment.numberOfOpenSites() / (N*N);
        }
        mean = mean();
        stddev = stddev();
    }

    //Sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(p);
    }

    //Sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(p);
    }

    //Low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean - 1.96 * stddev / Math.pow(times, 0.5);
    }

    //High endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean + 1.96 * stddev / Math.pow(times, 0.5);
    }
}
