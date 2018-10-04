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

package com.watabou.noosa;

import android.graphics.RectF;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Quad;

import java.nio.FloatBuffer;

public class Image extends Visual {

    public SmartTexture texture;
    public boolean flipHorizontal;
    private RectF frame;
    protected final float[] vertices;
    private final FloatBuffer verticesBuffer;

    protected boolean dirty;

    public Image () {
        super( 0, 0, 0, 0 );

        vertices = new float[16];
        verticesBuffer = Quad.create();
    }

    @SuppressWarnings ( "CopyConstructorMissesField" )
    public Image ( Image src ) {
        this();
        copy( src );
    }

    public Image ( Object tx ) {
        this();
        texture( tx );
    }

    public Image ( Object tx, int left, int top, int width, int height ) {
        this( tx );
        frame( texture.uvRect( left, top, left + width, top + height ) );
    }

    public void texture ( Object tx ) {
        texture = tx instanceof SmartTexture ? (SmartTexture) tx : TextureCache.get( tx );
        frame( new RectF( 0, 0, 1, 1 ) );
    }

    public void frame ( RectF frame ) {
        this.setFrame( frame );

        width = frame.width() * texture.getWidth();
        height = frame.height() * texture.getHeight();

        updateFrame();
        updateVertices();
    }

    public void frame ( int left, int top, int width, int height ) {
        frame( texture.uvRect( left, top, left + width, top + height ) );
    }

    public RectF frame () {
        return new RectF( getFrame() );
    }

    public void copy ( Image other ) {
        texture = other.texture;
        setFrame( new RectF( other.getFrame() ) );

        width = other.width;
        height = other.height;

        updateFrame();
        updateVertices();
    }

    protected void updateFrame () {

        if ( flipHorizontal ) {
            vertices[2] = getFrame().right;
            vertices[6] = getFrame().left;
            vertices[10] = getFrame().left;
            vertices[14] = getFrame().right;
        } else {
            vertices[2] = getFrame().left;
            vertices[6] = getFrame().right;
            vertices[10] = getFrame().right;
            vertices[14] = getFrame().left;
        }

        vertices[3] = getFrame().top;
        vertices[7] = getFrame().top;
        vertices[11] = getFrame().bottom;
        vertices[15] = getFrame().bottom;

        dirty = true;
    }

    protected void updateVertices () {

        vertices[0] = 0;
        vertices[1] = 0;

        vertices[4] = width;
        vertices[5] = 0;

        vertices[8] = width;
        vertices[9] = height;

        vertices[12] = 0;
        vertices[13] = height;

        dirty = true;
    }

    @Override
    public void draw () {

        super.draw();

        NoosaScript script = NoosaScript.get();

        texture.bind();

        script.camera( camera() );

        script.uModel.valueM4( matrix );
        script.lighting(
                rm, gm, bm, am,
                ra, ga, ba, aa );

        if ( dirty ) {
            verticesBuffer.position( 0 );
            verticesBuffer.put( vertices );
            dirty = false;
        }
        script.drawQuad( verticesBuffer );

    }

    private RectF getFrame () {
        return frame;
    }

    private void setFrame ( RectF frame ) {
        this.frame = frame;
    }
}
