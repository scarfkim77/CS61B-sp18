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
                int randomRow = StdRandom.uniform(N);
                int randomCol = StdRandom.uniform(N);
                experiment.open(randomRow, randomCol);
            }
            p[i] = (double) experiment.numberOfOpenSites() / (N*N);
        }
        mean = this.mean();
        stddev = this.stddev();
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

    public static void main(String[] args) {
        int N = 20, T = 10;
        PercolationFactory pf = new PercolationFactory();
        pf.make(N);
        PercolationStats test = new PercolationStats(N, T, pf);
        System.out.println(test.p[3]);
    }
}
