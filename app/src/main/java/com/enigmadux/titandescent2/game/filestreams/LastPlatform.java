package com.enigmadux.titandescent2.game.filestreams;

import android.content.Context;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class LastPlatform {
    //the path to the settings file
    private static final String PATH = "last_platform";


    private static int LAST_PLATFORM_ID = 0;
    private static int LAST_CHECKPOINT_ID = 0;

    //context used for resource opening
    private Context context;

    /** Default constructor
     *
     * @param context any non null context that can access files
     */
    public LastPlatform(Context context){
        this.context = context;
    }

    /** Writes the data from SoundLib settings (play music/sound effects) into the settings file
     *
     */
    public void writeLastPlatform(){
        try {
            PrintWriter stdout = new PrintWriter(new OutputStreamWriter(this.context.openFileOutput (LastPlatform.PATH, Context.MODE_PRIVATE)));

            stdout.println(LAST_PLATFORM_ID);
            stdout.println(LAST_CHECKPOINT_ID);

            stdout.println();
            stdout.close();

        } catch (IOException e){
//            Log.d("BACKEND","File write failed",e);
        }
    }


    /** Loads the data from the settings file into the SoundLib settings (play music/sound effects)
     *
     */
    public void readLastPlatform(){
        try (Scanner stdin = new Scanner(this.context.openFileInput(LastPlatform.PATH))) {
           LAST_PLATFORM_ID = stdin.nextInt();
           LAST_CHECKPOINT_ID = stdin.nextInt();
        } catch (Exception e) {
//            Log.d("FRONTEND", "Error loading settings file ", e);
            this.writeLastPlatform();
        }
    }


    public static int getLastPlatformId() {
        return LAST_PLATFORM_ID;
    }

    public static void setLastPlatformId(int lastPlatformId) {
        LAST_PLATFORM_ID = lastPlatformId;
    }

    public static int getLastCheckpointId() {
        return LAST_CHECKPOINT_ID;
    }

    public static void setLastCheckpointId(int lastPlatformId) {
        LAST_CHECKPOINT_ID = lastPlatformId;
    }



}