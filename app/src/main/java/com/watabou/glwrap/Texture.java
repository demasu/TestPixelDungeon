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

package com.watabou.glwrap;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class Texture {

    public static final int NEAREST = GLES20.GL_NEAREST;
    public static final int LINEAR = GLES20.GL_LINEAR;

    public static final int REPEAT = GLES20.GL_REPEAT;
    public static final int CLAMP = GLES20.GL_CLAMP_TO_EDGE;

    private int id;

    private boolean premultiplied = false;

    public Texture () {
        int[] ids = new int[1];
        GLES20.glGenTextures( 1, ids, 0 );
        setId( ids[0] );

        bind();
    }

    public void bind () {
        GLES20.glBindTexture( GLES20.GL_TEXTURE_2D, getId() );
    }

    public void filter ( int minMode, int maxMode ) {
        bind();
        GLES20.glTexParameterf( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minMode );
        GLES20.glTexParameterf( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, maxMode );
    }

    public void wrap ( int s, int t ) {
        bind();
        GLES20.glTexParameterf( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, s );
        GLES20.glTexParameterf( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, t );
    }

    public void delete () {
        int[] ids = { getId() };
        GLES20.glDeleteTextures( 1, ids, 0 );
    }

    public void bitmap ( Bitmap bitmap ) {
        bind();
        GLUtils.texImage2D( GLES20.GL_TEXTURE_2D, 0, bitmap, 0 );

        setPremultiplied( true );
    }

    public void pixels ( int w, int h, int[] pixels ) {

        bind();

        IntBuffer imageBuffer = ByteBuffer.
                allocateDirect( w * h * 4 ).
                order( ByteOrder.nativeOrder() ).
                asIntBuffer();
        imageBuffer.put( pixels );
        imageBuffer.position( 0 );

        GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D,
                0,
                GLES20.GL_RGBA,
                w,
                h,
                0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                imageBuffer );
    }

    public void pixels ( int w, int h, byte[] pixels ) {

        bind();

        ByteBuffer imageBuffer = ByteBuffer.
                allocateDirect( w * h ).
                order( ByteOrder.nativeOrder() );
        imageBuffer.put( pixels );
        imageBuffer.position( 0 );

        GLES20.glPixelStorei( GLES20.GL_UNPACK_ALIGNMENT, 1 );

        GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D,
                0,
                GLES20.GL_ALPHA,
                w,
                h,
                0,
                GLES20.GL_ALPHA,
                GLES20.GL_UNSIGNED_BYTE,
                imageBuffer );
    }

    // If getConfig returns null (unsupported format?), GLUtils.texImage2D works
    // incorrectly. In this case we need to load pixels manually
    public void handMade ( Bitmap bitmap, boolean recode ) {

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pixels = new int[w * h];
        bitmap.getPixels( pixels, 0, w, 0, 0, w, h );

        final int AG_MASK   = 0xFF00FF00;
        final int RB_MASK   = 0xFF;
        final int BIT_SHIFT = 16;
        // recode - components reordering is needed
        if ( recode ) {
            for ( int i = 0; i < pixels.length; i++ ) {
                int color = pixels[i];
                int ag = color & AG_MASK;
                int r = ( color >> BIT_SHIFT ) & RB_MASK;
                int b = color & RB_MASK;
                pixels[i] = ag | ( b << BIT_SHIFT ) | r;
            }
        }

        pixels( w, h, pixels );

        setPremultiplied( false );
    }

    public int getId () {
        return id;
    }

    public void setId ( int id ) {
        this.id = id;
    }

    public void setPremultiplied ( boolean premultiplied ) {
        this.premultiplied = premultiplied;
    }
}
