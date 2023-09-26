package com.enigmadux.titandescent2.main;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.enigmadux.titandescent2.util.SoundLib;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import enigmadux2d.core.EnigmaduxActivity;
import enigmadux2d.core.EnigmaduxGLSurfaceView;

public class TitanActivity extends EnigmaduxActivity{
    public static final String CHECKPOINT = "temporary_checkpoint";

    private RewardedAd rewardedVideoAd;

    private boolean isLoaded;

    private boolean isLoading;

    private RelativeLayout.LayoutParams adParams;
    private AdView adView;

    private RelativeLayout titleContent;


    /** Entry point to the app. The Surface view is set up here.
     *
     * @param savedInstanceState  Data that can be used to restore the activity to it's previous state. Should only really be used for super
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            // Create a GLSurfaceView instance and set it
            // as the ContentView for this Activity
            enigmaduxGLSurfaceView = new TitanGLSurfaceView(this);

            this.initAdMob();
            // Add the AdView to the view hierarchy. The view will have no size
            // until the ad is loaded.
            RelativeLayout layout = new RelativeLayout(this);
            layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            // Create an ad request.
            // get test ads on a physical device.
            AdRequest adRequest = new AdRequest.Builder()
                    .build();

            // Start loading the ad in the background.
            adView.loadAd(adRequest);



            adParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);



            layout.addView(enigmaduxGLSurfaceView);

            this.titleContent = layout;



            setContentView(layout);


        } catch (Exception e){
//            Log.d("Exception","On Create Failed:" + e);
        }


    }
    private void initAdMob(){
        List<String> testDeviceIds = Collections.singletonList("46F522AB549EF96A0DAFC5DF05917350");
        RequestConfiguration configuration =
                MobileAds.getRequestConfiguration().toBuilder().
                        setTestDeviceIds(testDeviceIds).
                        setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE).
                        setTagForUnderAgeOfConsent(RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE).
                        setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_PG).
                        build();
        MobileAds.setRequestConfiguration(configuration);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                    Log.d("Mobile Ads Status","init status: " + initializationStatus.toString());
            }
        });

        this.loadRewardedVideoAd();


        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        // Create an ad.
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-9330693234949164/2218439464");
        adView.setBackgroundColor(Color.TRANSPARENT);
    }

    /** on pause
     *
     */
    @Override
    public void onPause(){
        try {
            super.onPause();
            this.enigmaduxGLSurfaceView.onPause();
            SoundLib.pauseAllMedia();
        } catch (Exception e){
//            Log.d("Exception","Pause Failed",e);
        }
    }


    /** on resume
     *
     */
    @Override
    public void onResume(){
        try {
            super.onResume();
            this.enigmaduxGLSurfaceView.onResume();
            SoundLib.resumeAllMedia();
        } catch (Exception e){
//            Log.d("Exception","Resume Failed",e);
        }
    }
    /** Loads all media
     *
     */
    @Override
    public void onStart(){
        try {
            super.onStart();
            this.enigmaduxGLSurfaceView.onStart();
        } catch (Exception e){
//            Log.d("Exception","Start Failed",e);
        }
    }

    /** called whenever app is exited, but still in memory
     *
     */
    @Override
    public void onStop(){
        try {
            super.onStop();
            this.enigmaduxGLSurfaceView.onStop();
        } catch (Exception e){
//            Log.d("Exception","Stop Failed",e);
        }
//        Log.d("CRATER","Stopping Activity");
    }


    @Override
    protected void onDestroy() {
        try {
            SoundLib.stopAllMedia();
            this.enigmaduxGLSurfaceView.onDestroy();
            super.onDestroy();
        } catch (Exception e){
//            Log.d("Exception","Destroy Failed",e);
        }
    }

    public void loadRewardedVideoAd() {
        try {
            isLoading = true;
            // Use an activity context to get the rewarded video instance.
            rewardedVideoAd = new RewardedAd(this, "ca-app-pub-9330693234949164/2357138900");

            AdRequest adRequest = new AdRequest.Builder().build();

            rewardedVideoAd.loadAd(adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onRewardedAdLoaded() {
                    // Ad successfully loaded.
//                Log.d("Mobile Ad Status","Loaded Ad");
                    isLoaded = true;
                    isLoading = false;
                }

                @Override
                public void onRewardedAdFailedToLoad(int errorCode) {
                    switch (errorCode) {
                        case AdRequest.ERROR_CODE_APP_ID_MISSING:
//                        Log.d("Ads","App id missing");
                            break;
                        case AdRequest.ERROR_CODE_INVALID_REQUEST:
//                        Log.d("Ads","Invalid Request");
                            break;
                        case AdRequest.ERROR_CODE_INTERNAL_ERROR:
//                        Log.d("Ads","Internal Error");
                            break;
                        case AdRequest.ERROR_CODE_NETWORK_ERROR:
//                        Log.d("Ads","Network Error");
                            break;
                        default:
//                        Log.d("Ads","Error Code " + errorCode);
                    }

                    // Ad failed to load.
                    isLoaded = false;
                    isLoading = false;
                }
            });
        } catch (Exception e){
            //
        }
    }


    public RewardedAd getRewardedVideoAd(){
        return rewardedVideoAd;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void onTitleShown(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adView.getParent() != titleContent) {
                    titleContent.addView(adView,adParams);
                }
            }
        });



    }
    public void onTitleHide(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adView.getParent() == titleContent) {
                    titleContent.removeView(adView);
                }
            }
        });


    }
}
