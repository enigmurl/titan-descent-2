package com.enigmadux.titandescent2.game;

import android.content.Context;
import android.util.Log;

import com.enigmadux.titandescent2.game.objects.Cloud;
import com.enigmadux.titandescent2.game.objects.FuelTank;
import com.enigmadux.titandescent2.game.objects.Platform;
import com.enigmadux.titandescent2.game.objects.Star;

import java.util.HashSet;
import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.Scanner;

public class LevelLoader {


    public void loadLevel(World world,Context context,int resourcePointer){
        Scanner level_data = new Scanner(context.getResources().openRawResource(resourcePointer));
        //data is formatted with US decimals
        level_data.useLocale(Locale.US);

        HashSet<Integer> starIds = new HashSet<>();
        HashSet<Integer> platIds = new HashSet<>();

        while (level_data.hasNext()){
            String type = level_data.next();
            switch (type){
                case "FUEL":
                    float fX = level_data.nextFloat();
                    float fY = level_data.nextFloat();

                    int fId = world.getFuelTanks().createVertexInstance();
                    FuelTank fuelTank = new FuelTank(fId,fX,fY);
                    world.getFuelTanks().addInstance(fuelTank);
                    break;
                case "CLOUD":
                    float cX = level_data.nextFloat();
                    float cY = level_data.nextFloat();
                    float cR = level_data.nextFloat();

                    int cId = world.getClouds().createVertexInstance();
                    Cloud c = new Cloud(cId,cX,cY,cR);
                    world.getClouds().addInstance(c);
                    break;
                case "STAR":
                    int starNum = level_data.nextInt();
                    if (starIds.contains(starNum)){
                        //throw new IllegalStateException("FAILED LOADING LEVEL, the star id: " + starNum + " was used multiple times");
                    }
                    starIds.add(starNum);


                    float sX = level_data.nextFloat();
                    float sY = level_data.nextFloat();


                    int sId = world.getStars().createVertexInstance();
                    Star s = new Star(sId,starNum,sX,sY);
                    world.getStars().addInstance(s);


                    break;
                case "PLATFORM":
                    int id = level_data.nextInt();
                    if (platIds.contains(id)){
                        //throw new IllegalStateException("FAILED LOADING LEVEL, the platform id: " + id + " was used multiple times");
                    }
                    platIds.add(id);
                    float pX;
                    try {
                        pX = level_data.nextFloat();
                    } catch (Exception e){
                        Log.d("LevelLoader","Execpetion",e);
                        return;
                    }
                    float pY = level_data.nextFloat();
                    float pW = level_data.nextFloat();
                    float pH = level_data.nextFloat();

                    int pId = world.getPlatforms().createVertexInstance();
                    Platform platform = new Platform(pId,pX,pY,pW,pH,id);
                    world.getPlatforms().addInstance(platform);
                    break;
                default:
                    //Log.d("World data","FAILED LOADING LEVEL no such item as " + type);
                    //throw new IllegalStateException("FAILED LOADING LEVEL no such item as " + type);

            }


        }

        level_data.close();

    }
}
