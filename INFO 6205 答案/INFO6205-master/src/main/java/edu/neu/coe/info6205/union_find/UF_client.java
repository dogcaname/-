package edu.neu.coe.info6205.union_find;

import java.util.Random;

public class UF_client {
    public static int count(int n) {
        int pairs_num = 0;
        UF_HWQUPC arr = new UF_HWQUPC(n);
        Random random = new Random();
        int pairs_one = 0, pairs_two = 0;
        while (arr.components() != 1) {
            pairs_one = random.nextInt(n);
            pairs_two = random.nextInt(n);
            arr.connect(pairs_one, pairs_two);
            pairs_num++;
        }
        return pairs_num;
    }

    public static void main(String[] args) {
        int[] n_arr = new int[]{1, 10, 100, 1000, 10000, 100000};
        for (int n : n_arr) {
            int result = 0;
            for (int i = 0; i < 100; i++) {
                result += count(n);
            }
            result /= 100;
            System.out.println("When the number of sites are " + n + ", the number of connections generated is " + result + ".");
        }
    }
}
