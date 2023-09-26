package com.enigmadux.titandescent2.game.filestreams;

import android.content.Context;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class CompletedPlatforms {
    //the path to the settings file
    private static final String PATH = "completed_platforms";


    private static HashSet<Integer> COMPLETED = new HashSet<>();

    //context used for resource opening
    private Context context;

    /** Default constructor
     *
     * @param context any non null context that can access files
     */
    public CompletedPlatforms(Context context){
        this.context = context;
    }

    /** Writes the data from SoundLib settings (play music/sound effects) into the settings file
     *
     */
    public void writeCompleted(){
        try {
            PrintWriter stdout = new PrintWriter(new OutputStreamWriter(this.context.openFileOutput (CompletedPlatforms.PATH, Context.MODE_PRIVATE)));

            for (int i: COMPLETED) {
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
    public void readCompleted(){
        try (Scanner stdin = new Scanner(this.context.openFileInput(CompletedPlatforms.PATH))) {
            while (stdin.hasNextInt()){
                COMPLETED.add(stdin.nextInt());
            }

        } catch (Exception e) {
//            Log.d("FRONTEND", "Error loading settings file ", e);
            this.writeCompleted();
        }
    }

    public void addCompleted(int platID){
        COMPLETED.add(platID);
        this.writeCompleted();
    }

    public boolean isCompleted(int platID){
        return COMPLETED.contains(platID);
    }

}