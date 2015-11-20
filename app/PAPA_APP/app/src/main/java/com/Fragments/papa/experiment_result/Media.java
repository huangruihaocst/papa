package com.Fragments.papa.experiment_result;

import android.graphics.Bitmap;

/**
 * Created by huang on 15-11-20.
 */
public class Media {
    public enum Type {
        image,
        video,
    }

    public String id;

    public Bitmap bitmap;
    public String path;
    public Type type;//1 for image, 0 for video

    public Media(Bitmap bitmap, String path, Type type){
        this.bitmap = bitmap;
        this.path = path;
        this.type = type;
        id = "-1";
    }
    public Media(){
        id = "-1";
    }
}
