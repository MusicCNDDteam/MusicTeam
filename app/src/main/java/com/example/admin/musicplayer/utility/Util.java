package com.example.admin.musicplayer.utility;

/**
 * Created by ADMIN on 06/04/2017.
 */

public class Util {
    /*
     * convert milliseconds time (length of song) to
      Timer Format
      Hours : Minutes: Seconds
     */
    public static String milliSecondstoTimer(long milliseconds) {

        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60) / (1000 * 60));
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            finalTimerString = hours + ":";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        /* return timer string */
        return finalTimerString;
    }

    /*
     Function to get Progress percentage
     @param currentDuration
     @param totalDuration
    */
    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        /* calculate percentage */
        percentage = (((double) currentSeconds) / totalSeconds) * 100;
        /* return percentage type Int */
        return percentage.intValue();
    }

    /*
      Function to conver Progress to Timer
      @param progress
      @param total Duration
      output : current duration in milliseconds
     */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        /* return current duration in milliseconds */
        return currentDuration * 1000;
    }
	/*
	tuan cmt 
	*/
}
