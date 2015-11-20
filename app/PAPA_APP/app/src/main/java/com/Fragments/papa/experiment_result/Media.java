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

    private void init(Bitmap bitmap, String path, Type type, String id)
    {
        this.bitmap = bitmap;
        this.path = path;
        this.type = type;
        this.id = id;
    }

    public Media(Bitmap bitmap, String path, Type type){
        init(bitmap, path, type, "-1");
    }

    public Media(Bitmap bitmap, String path, Type type, String id)
    {
        init(bitmap, path, type, id);
    }
}
