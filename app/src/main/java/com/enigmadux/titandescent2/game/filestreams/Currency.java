package com.enigmadux.titandescent2.game.filestreams;

import android.content.Context;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Currency {
    public static final int COMPLETION_CURRENCY = 15;
    public static final long MAX_MILLIS_FOR_STABILIZATION = 150;
    public static final float PLATFORM_DIVISOR = 6;

    //the path to the settings file
    private static final String PATH = "currency";



    private static final int STARTING_CURRENCY = 500;
    private static int CURRENCY = STARTING_CURRENCY;

    //context used for resource opening
    private Context context;

    /** Default constructor
     *
     * @param context any non null context that can access files
     */
    public Currency(Context context){
        this.context = context;
    }

    /** Writes the data from SoundLib settings (play music/sound effects) into the settings file
     *
     */
    public void writeCurrency(){
        try {
            PrintWriter stdout = new PrintWriter(new OutputStreamWriter(this.context.openFileOutput (Currency.PATH, Context.MODE_PRIVATE)));

            stdout.println(CURRENCY);

            stdout.println();
            stdout.close();

        } catch (IOException e){
//            Log.d("BACKEND","File write failed",e);
        }
    }


    /** Loads the data from the settings file into the SoundLib settings (play music/sound effects)
     *
     */
    public void readCurrency(){
        try (Scanner stdin = new Scanner(this.context.openFileInput(Currency.PATH))) {
            CURRENCY = stdin.nextInt();
        } catch (Exception e) {
//            Log.d("FRONTEND", "Error loading settings file ", e);
            this.writeCurrency();
        }
    }


    public static int getCurrency() {
        return CURRENCY;
    }

    public static void addCurrency(int amnt) {
        CURRENCY += amnt;
    }



}