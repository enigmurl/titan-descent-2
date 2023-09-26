package com.enigmadux.titandescent2.layouts.deathlayout;

import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.game.objects.Platform;
import com.enigmadux.titandescent2.guilib.Button;
import com.enigmadux.titandescent2.layouts.inGameOverlay.InGameOverlay;
import com.enigmadux.titandescent2.layouts.revertconfirmation.ConfirmationLayout;
import com.enigmadux.titandescent2.main.TitanActivity;
import com.enigmadux.titandescent2.main.TitanRenderer;
import com.enigmadux.titandescent2.values.LayoutConsts;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;

public class WatchButton extends Button {
    private static final float WIDTH = 0.6f;

    private TitanActivity titanActivity;

    private RewardedAdCallback rewardedAdCallback;
    private DeathLayout deathLayout;
    public WatchButton(final TitanActivity titanActivity, final DeathLayout deathLayout, float x, float y) {
        super(null, R.drawable.watch_button, x, y, WIDTH * LayoutConsts.SCALE_X, WIDTH, 0);
        this.titanActivity = titanActivity;
        this.deathLayout = deathLayout;

        this.rewardedAdCallback = new RewardedAdCallback() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                if (! rewardItem.getType().equals(TitanActivity.CHECKPOINT)){
//                    Log.d("Mobile ad Status","Unknown Reward Type");
                }
                synchronized (TitanRenderer.LOCK) {
                    revertToLastPlatform();
                }
            }


            @Override
            public void onRewardedAdClosed() {
                super.onRewardedAdClosed();
                titanActivity.loadRewardedVideoAd();
                synchronized (TitanRenderer.LOCK) {
                    World world = guiData.getLayout(World.class);

                    if (! world.getShip().hasLeftLastPlatform()){
                        world.onNewPlatform();
                    }
                    world.getCamera().reSync();
                    world.getBackendThread().setPaused(false);

                    guiData.stackScene(World.class, InGameOverlay.class);
                }
            }

            @Override
            public void onRewardedAdFailedToShow(int i) {
                super.onRewardedAdFailedToShow(i);
                synchronized (TitanRenderer.LOCK) {
                    World world = guiData.getLayout(World.class);
                    world.showWifiError();
                    if (guiData.contains(ConfirmationLayout.class)) {
                        guiData.stackScene(World.class, InGameOverlay.class, ConfirmationLayout.class);
                    } else {
                        revertToCheckpoint();
                    }
                    deathLayout.resetTouchEvent();
                }
            }

            @Override
            public void onRewardedAdOpened() {
                super.onRewardedAdOpened();
                synchronized (TitanRenderer.LOCK) {
                    deathLayout.resetTouchEvent();
                    World world = guiData.getLayout(World.class);

                    world.getThrusterControls().disengage();
                }
            }
        };
    }

    @Override
    protected void onHardRelease(MotionEvent e) {
        super.onHardRelease(e);


        RewardedAd rewardedAd = this.titanActivity.getRewardedVideoAd();
        if (rewardedAd.isLoaded()){
            rewardedAd.show(titanActivity,rewardedAdCallback);
        } else {
            synchronized (TitanRenderer.LOCK) {
                World world = guiData.getLayout(World.class);
                world.showWifiError();
                if (guiData.contains(ConfirmationLayout.class)) {
                    guiData.stackScene(World.class, InGameOverlay.class, ConfirmationLayout.class);
                } else {
                    revertToCheckpoint();
                }
            }
        }
        deathLayout.finishTouchEvent();
    }

    private void revertToLastPlatform(){
        World world = guiData.getLayout(World.class);

        world.onAdRewarded();
        world.revert();
        world.getCamera().reSync();



    }

    private void revertToCheckpoint(){
        World world = guiData.getLayout(World.class);

        Platform target = world.getShip().getLastCheckpoint();
        world.getShip().death(world);
        world.getShip().setCurrentPlatform(target);
        world.onNewPlatform();
        world.getCamera().reSync();
        world.getBackendThread().setPaused(false);

        guiData.stackScene(World.class, InGameOverlay.class);
    }
}
