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

package com.watabou.gltextures;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.support.annotation.Nullable;

import com.watabou.glwrap.Texture;

public class SmartTexture extends Texture {

    private int width;
    private int height;

    private int fModeMin;
    private int fModeMax;

    private int wModeH;
    private int wModeV;

    @Nullable
    private Bitmap bitmap;

    public SmartTexture ( Bitmap bitmap ) {

        super();

        bitmap( bitmap );
        filter( Texture.NEAREST, Texture.NEAREST );
        wrap( CLAMP, CLAMP );

    }

    @Override
    public void filter ( int minMode, int maxMode ) {
        fModeMin = minMode;
        fModeMax = maxMode;
        super.filter( fModeMin, fModeMax );
    }

    @Override
    public void wrap ( int s, int t ) {
        wModeH = s;
        wModeV = t;
        super.wrap( wModeH, wModeV );
    }

    @Override
    public void bitmap ( Bitmap bitmap ) {
        handMade( bitmap, true );

        this.setBitmap( bitmap );
        setWidth( bitmap.getWidth() );
        setHeight( bitmap.getHeight() );
    }

    public void reload () {
        setId( new SmartTexture( getBitmap() ).getId() );
        filter( fModeMin, fModeMax );
        wrap( wModeH, wModeV );
    }

    //TODO: This is possibly unused since I'm not finding anything that uses this specific delete call
    @Override
    public void delete () {

        super.delete();

        if ( getBitmap() != null ) {
            getBitmap().recycle();
        }
        setBitmap( null );
    }

    public RectF uvRect ( int left, int top, int right, int bottom ) {
        return new RectF(
                (float) left / getWidth(),
                (float) top / getHeight(),
                (float) right / getWidth(),
                (float) bottom / getHeight() );
    }

    public int getWidth () {
        return width;
    }

    private void setWidth ( int width ) {
        this.width = width;
    }

    public int getHeight () {
        return height;
    }

    private void setHeight ( int height ) {
        this.height = height;
    }

    @Nullable
    public Bitmap getBitmap () {
        return bitmap;
    }

    private void setBitmap ( @Nullable Bitmap bitmap ) {
        this.bitmap = bitmap;
    }
}
