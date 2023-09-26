package com.enigmadux.titandescent2.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.enigmadux.titandescent2.R;

/** All songs and sound effect sare played here
 *
 *
 * @author Manu Bhat
 * @version BETA
 */
public class SoundLib {

    /** music played when in game */
    private static MediaPlayer gameMusic;


    /** whether or not the player has the music on*/
    private static boolean playMusic = true;
    /** whether or not the player has the sound effects on*/
    private static boolean playSoundEffects = true;

    private static int goodLanding;
    private static int crashLanding;
    private static int cloudDeath;
    private static int thrusters;
    private static int leftThrusterPointer;
    private static boolean lastLeftThrusterState = false;
    private static int rightThrusterPointer;
    private static boolean lastRightThrusterState = false;

    private static int starCollected;
    private static int fuelCollected;

    private static int buttonSelected;





    private static SoundPool soundEffects;

    /** This binds the raw music to the music and the sound effects
     *
     * @param context any non null Context, that's used to access resources
     */
    public static void loadMedia(Context context){
        if (! SoundLib.playMusic) return;

        soundEffects = new SoundPool.Builder().setMaxStreams(10).build();

        gameMusic = MediaPlayer.create(context,R.raw.gamemusic);
        gameMusic.setLooping(true);


        cloudDeath = soundEffects.load(context,R.raw.cloud_death,1);
        goodLanding = soundEffects.load(context,R.raw.good_landing,1);

        crashLanding = soundEffects.load(context,R.raw.crash,1);
        buttonSelected = soundEffects.load(context,R.raw.button_click,1);
        fuelCollected = soundEffects.load(context,R.raw.fuel_collected,1);
        thrusters = soundEffects.load(context,R.raw.exhaust,1);
        starCollected = soundEffects.load(context,R.raw.star_collected,1);

    }

    /** Pauses all music,
     * unlike muteAllMedia, the songs do not continue to play FIX THIS IT STILL DOES CONTINUE TO PLAY
     *
     */
    public static void pauseAllMedia(){
        try {
            SoundLib.gameMusic.pause();
            soundEffects.stop(leftThrusterPointer);
            soundEffects.stop(rightThrusterPointer);

        } catch (Exception e){
//            Log.d("SOUND_LIB","null pointer",e);
        }
    }

    public static void stopAllMedia(){
        try {
            SoundLib.gameMusic.release();
            gameMusic  = null;

            soundEffects.release();
            soundEffects = null;

        } catch (Exception e){
//            Log.d("SOUND_LIB","null pointer",e);
        }
    }

    /** resumes all music
     *
     *
     *
     */
    public static void resumeAllMedia(){
        try {
            if (playMusic) {
                SoundLib.gameMusic.start();
            }
        } catch (Exception e){
//            Log.d("SOUND_LIB","null pointer",e);
        }
    }

    /** All media (only music for now) is muted, however it keeps playing just at 0 volume
     */
    public static void muteAllMedia(){
        SoundLib.playMusic = false;

        try {
            SoundLib.gameMusic.setVolume(0,0);
        } catch (Exception e){
//            Log.d("SOUND_LIB","null pointer",e);
        }

    }

    /** All media (only music for now) is un muted
     *
     */
    public static void unMuteAllMedia(){
        SoundLib.playMusic = true;

        try {
            SoundLib.gameMusic.setVolume(1,1);
        } catch (Exception e){
//            Log.d("SOUND_LIB","null pointer",e);
        }
    }



    /** Sets the state of the game music
     *
     * @param state true means to start playing, false means to end playing
     */
    public static void setStateGameMusic(boolean state){
        try {
            if (SoundLib.gameMusic == null) {
                return;
            }

            if (state) {
                if (!SoundLib.gameMusic.isPlaying()) {
                    SoundLib.gameMusic.start();
                }
                if (!SoundLib.playMusic) {
                    SoundLib.muteAllMedia();
                    SoundLib.gameMusic.setVolume(0, 0);
                }
            } else if (SoundLib.gameMusic.isPlaying()) {
                SoundLib.gameMusic.pause();
                SoundLib.gameMusic.seekTo(0);
            }
        } catch (Exception e){
            //
        }
    }

    public static void playCloudDeath(){
        try {
            soundEffects.play(cloudDeath, 1, 1, 0, 0, 1);
        } catch (Exception e){
            //
        }
    }


    public static void playButtonClicked(){
        try {
            soundEffects.play(buttonSelected, 1, 1, 0, 0, 1);
        } catch (Exception e){
            //
        }

    }
    public static void playStarCollected(){
        try {
            soundEffects.play(starCollected,1,1,0,0,1);
        } catch (Exception e){
            //
        }
    }

    public static void playFuelCollected(){
        try {
            soundEffects.play(fuelCollected,1,1,0,0,1);
        } catch (Exception e){
            //
        }
    }

    public static void playCrash(){
        try{
            soundEffects.play(crashLanding,1,1,0,0,1);
        } catch (Exception e){
            //
        }
    }
    public static void playGoodLanding(){
        try {
            soundEffects.play(goodLanding, 1, 1, 0, 0, 1);
        } catch (Exception e){
            //
        }
    }

    public static void updateLeftThrusterState(boolean state){
        try {
            if (state && !lastLeftThrusterState) {
                leftThrusterPointer = soundEffects.play(thrusters, 1, 1, 0, 0, 1);
            } else if (!state && lastLeftThrusterState) {
                soundEffects.stop(leftThrusterPointer);
            }
            lastLeftThrusterState = state;
        } catch (Exception e){
            //
        }
    }

    public static void updateRightThrusterState(boolean state){
        try {
            if (state && !lastRightThrusterState) {
                rightThrusterPointer = soundEffects.play(thrusters, 1, 1, 0, 0, 1);
            } else if (!state && lastRightThrusterState) {
                soundEffects.stop(rightThrusterPointer);
            }
            lastRightThrusterState = state;
        } catch (Exception e){
            //
        }
    }


    /** Sets whether or not playing music, true means music will be played, false means music wont be played
     *
     * @param playSoundEffects whether or not to play soundEffects
     */
    public static void setPlaySoundEffects(boolean playSoundEffects){
        SoundLib.playSoundEffects = playSoundEffects;
    }

    /** Whether or not music is being played
     *
     * @return Whether or not music is being played
     */
    public static boolean isPlayMusic() {
        return SoundLib.playMusic;
    }

    /** Whether or not sound effects are being played
     *
     * @return Whether or not sound effects are being played
     */
    public static boolean isPlaySoundEffects() {
        return SoundLib.playSoundEffects;
    }

}
