package hw3.hash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO:
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int N = oomages.size();
        double lowerBound = N / 50;
        double upperBound = N / 2.5;
        int[] numInBucket = new int[M];
        for (Oomage o: oomages) {
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            numInBucket[bucketNum] += 1;
        }
        for (int i: numInBucket) {
            if (i < lowerBound || i > upperBound) {
                return false;
            }
        }
        return true;
    }
}
