package edu.neu.coe.info6205.sort.par;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
class ParSort {

    public static int cutoff = 1000;

    public static void sort(int[] array, int from, int to) {
        if (to - from < cutoff) Arrays.sort(array, from, to);
        else {
            CompletableFuture<int[]> parsort1 = parsort(array, from, from + (to - from) / 2); // TO IMPLEMENT
            CompletableFuture<int[]> parsort2 = parsort(array, from + (to - from) / 2, to); // TO IMPLEMENT
            CompletableFuture<int[]> parsort = parsort1.thenCombine(parsort2, (xs1, xs2) -> {
                        int[] result = new int[xs1.length + xs2.length];
                        // TO BE IMPLEMENTED ...
                        int i = 0, j = 0, k = 0;
                        while(i < xs1.length && j < xs2.length) result[k++] = xs1[i] <= xs2[j] ? xs1[i++] : xs2[j++];
                        while(i < xs1.length) result[k++] = xs1[i++];
                        while(j < xs2.length) result[k++] = xs2[j++];
                        // ... END IMPLEMENTATION
                        return result;
                    });

            parsort.whenComplete((result, throwable) -> System.arraycopy(result, 0, array, from, result.length));
//            System.out.println("# threads: "+ ForkJoinPool.commonPool().getRunningThreadCount());
            parsort.join();
        }
    }

    private static CompletableFuture<int[]> parsort(int[] array, int from, int to) {
        return CompletableFuture.supplyAsync(
                () -> {
                    int[] result = new int[to - from];
                    // TO BE IMPLEMENTED ...
                    int k = 0;
                    for(int i = from; i < to; i++) result[k++] = array[i];
                    sort(result, 0, to-from);
                    // ... END IMPLEMENTATION
                    return result;
                }
        );
    }
}