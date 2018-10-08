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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class BitmapCache {

    private static final String DEFAULT = "__default";
    @SuppressLint ( "StaticFieldLeak" )
    private static Context context;
    private static final HashMap<String, Layer> layers = new HashMap<>();
    private static final BitmapFactory.Options opts = new BitmapFactory.Options();

    static {
        opts.inDither = false;
    }

    public static Bitmap get ( String assetName ) {

        Layer layer;
        if ( !layers.containsKey( DEFAULT ) ) {
            layer = new Layer();
            layers.put( DEFAULT, layer );
        } else {
            layer = layers.get( DEFAULT );
        }

        if ( layer.containsKey( assetName ) ) {
            return layer.get( assetName );
        } else {

            try {
                InputStream stream = getContext().getResources().getAssets().open( assetName );
                Bitmap bmp = BitmapFactory.decodeStream( stream, null, opts );
                layer.put( assetName, bmp );
                return bmp;
            } catch ( IOException e ) {
                return null;
            }

        }
    }

    private static Context getContext () {
        return context;
    }

    public static void setContext ( Context context ) {
        BitmapCache.context = context;
    }

    @SuppressWarnings ( "serial" )
    private static class Layer extends HashMap<Object, Bitmap> {

        @Override
        public void clear () {
            for ( Bitmap bmp : values() ) {
                bmp.recycle();
            }
            super.clear();
        }
    }
}
