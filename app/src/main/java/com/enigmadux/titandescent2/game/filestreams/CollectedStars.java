package com.enigmadux.titandescent2.game.filestreams;

import android.content.Context;


import com.enigmadux.titandescent2.R;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class CollectedStars {
    //the path to the settings file
    private static final String PATH = "collected_star_ids";
    private static final int[] STAR_FILES = new int[] {
            R.raw.titan_world_data
    };

    private static final HashSet<Integer> COLLECTED_STARS = new HashSet<>();

    private static int TOTAL_STARS;

    //context used for resource opening
    private Context context;

    /** Default constructor
     *
     * @param context any non null context that can access files
     */
    public CollectedStars(Context context){
        this.context = context;
    }

    /** Writes the data from SoundLib settings (play music/sound effects) into the settings file
     *
     */
    public void updateCollectedStars(){
        try {
            PrintWriter stdout = new PrintWriter(new OutputStreamWriter(this.context.openFileOutput (CollectedStars.PATH, Context.MODE_PRIVATE)));

            for (int starNum: COLLECTED_STARS) {
                stdout.println(starNum);
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
    public void loadCollectedStars(){
        try (Scanner stdin = new Scanner(this.context.openFileInput(CollectedStars.PATH))) {
            while (stdin.hasNextInt()){
                COLLECTED_STARS.add(stdin.nextInt());
            }
        } catch (Exception e) {
//            Log.d("FRONTEND", "Error loading settings file ", e);
            this.updateCollectedStars();
        }
        for (int resourcePointer: STAR_FILES) {
            Scanner level_data = new Scanner(context.getResources().openRawResource(resourcePointer));
            TOTAL_STARS = 0;
            while (level_data.hasNext()){
                if (level_data.next().equals("STAR")){
                    TOTAL_STARS++;
                }
            }
        }
    }


    public boolean isCollected(int starNum){
        return COLLECTED_STARS.contains(starNum);
    }

    public void setCollected(int starNum){
        COLLECTED_STARS.add(starNum);
    }

    public static int totalCollected(){
        return COLLECTED_STARS.size();
    }

    public static int getTotalStars(){
        return TOTAL_STARS;
    }


}