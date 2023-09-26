package com.enigmadux.titandescent2.game.filestreams;

import android.content.Context;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class CheckPointPlatforms {
    //the path to the settings file
    private static final String PATH = "checkpoint_platforms";


    private static HashSet<Integer> CHECKPOINTS = new HashSet<>();

    //context used for resource opening
    private Context context;

    /** Default constructor
     *
     * @param context any non null context that can access files
     */
    public CheckPointPlatforms(Context context){
        this.context = context;
    }

    /** Writes the data from SoundLib settings (play music/sound effects) into the settings file
     *
     */
    public void writeCheckpoints(){
        try {
            PrintWriter stdout = new PrintWriter(new OutputStreamWriter(this.context.openFileOutput (CheckPointPlatforms.PATH, Context.MODE_PRIVATE)));

            for (int i:CHECKPOINTS) {
                stdout.println(i);
            }

            stdout.println();
            stdout.close();

        } catch (IOException e){
//            Log.d("BACKEND","File write failed",e);
        }
    }


    /** Loads the data from the settings file into the SoundLib settings (play music/sound effects)
     *
     */
    public void readCheckpoints(){
        try (Scanner stdin = new Scanner(this.context.openFileInput(CheckPointPlatforms.PATH))) {
            while (stdin.hasNextInt()){
                CHECKPOINTS.add(stdin.nextInt());
            }

        } catch (Exception e) {
//            Log.d("FRONTEND", "Error loading settings file ", e);
            this.writeCheckpoints();
        }
    }

    public void addCheckpoint(int platID){
        CHECKPOINTS.add(platID);
        this.writeCheckpoints();
    }

    public boolean isCheckpoint(int platID){
        return CHECKPOINTS.contains(platID);
    }




}