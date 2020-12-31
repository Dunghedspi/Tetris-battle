package server;

import java.util.Arrays;
import java.util.Random;
import java.util.TimerTask;

public class Utils {

    /**
     * The action to be performed by this timer task.
     */
    public static String randomArrayNumber() {
        Random rd = new Random(); // creating Random object
        int[] arr = new int[10];
        for (int i = 0; i < arr.length; i++)
        {
            arr[i] = rd.nextInt(7) + 1; // storing random integers in an array
        }
        return Arrays.toString(arr);
    }
}
