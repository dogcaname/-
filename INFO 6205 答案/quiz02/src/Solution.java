import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;


class Result {

    /*
     * Complete the 'fizzBuzz' function below.
     *
     * The function accepts INTEGER n as parameter.
     */

    public static void fizzBuzz(int n) {
        // Write your code here
        for(int x=1;x<=n;x++){
            if((x%3)==0&&(x%5)==0) System.out.println("FizzBuzz");
            else if((x%3)==0) System.out.println("Fizz");
            else if((x%5)==0) System.out.println("Buzz");
            else System.out.println(x);
        }
        return;

    }

}

public class Solution {
    public static void main(String[] args) {
        Result.fizzBuzz(15);

    }
}
