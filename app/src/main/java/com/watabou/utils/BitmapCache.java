/*
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.watabou.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapCache {

    private static final String DEFAULT = "__default";

    private static final HashMap<String, Layer> layers = new HashMap<>();

    private static final BitmapFactory.Options opts = new BitmapFactory.Options();

    static {
        opts.inDither = false;
    }

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public static Bitmap get(String assetName) {
        return get(DEFAULT, assetName);
    }

    @SuppressWarnings("SameParameterValue")
    private static Bitmap get(String layerName, String assetName) {

        Layer layer;
        if (!layers.containsKey(layerName)) {
            layer = new Layer();
            layers.put(layerName, layer);
        } else {
            layer = layers.get(layerName);
        }

        if (layer.containsKey(assetName)) {
            return layer.get(assetName);
        } else {

            try {
                InputStream stream = context.getResources().getAssets().open(assetName);
                Bitmap bmp = BitmapFactory.decodeStream(stream, null, opts);
                layer.put(assetName, bmp);
                return bmp;
            } catch (IOException e) {
                return null;
            }

        }
    }

// --Commented out by Inspection START (8/28/18, 6:54 PM):
//    public static Bitmap get(int resID) {
//        return get(DEFAULT, resID);
//    }
// --Commented out by Inspection STOP (8/28/18, 6:54 PM)

    @SuppressWarnings("SameParameterValue")
    private static Bitmap get(String layerName, int resID) {

        Layer layer;
        if (!layers.containsKey(layerName)) {
            layer = new Layer();
            layers.put(layerName, layer);
        } else {
            layer = layers.get(layerName);
        }

        if (layer.containsKey(resID)) {
            return layer.get(resID);
        } else {

            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resID);
            layer.put(resID, bmp);
            return bmp;

        }
    }

// --Commented out by Inspection START (8/28/18, 6:54 PM):
//    public static void clear(String layerName) {
//        if (layers.containsKey(layerName)) {
//            layers.get(layerName).clear();
//            layers.remove(layerName);
//        }
//    }
// --Commented out by Inspection STOP (8/28/18, 6:54 PM)

// --Commented out by Inspection START (8/28/18, 6:54 PM):
//    public static void clear() {
//        for (Layer layer : layers.values()) {
//            layer.clear();
//        }
//        layers.clear();
//    }
// --Commented out by Inspection STOP (8/28/18, 6:54 PM)

    @SuppressWarnings("serial")
    private static class Layer extends HashMap<Object, Bitmap> {

        @Override
        public void clear() {
            for (Bitmap bmp : values()) {
                bmp.recycle();
            }
            super.clear();
        }
    }
}
