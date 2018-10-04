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

    private SmartTexture texture;
    private boolean flipHorizontal;
    private RectF frame;
    private final float[] vertices;
    private final FloatBuffer verticesBuffer;

    private boolean dirty;

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
        frame( getTexture().uvRect( left, top, left + width, top + height ) );
    }

    public void texture ( Object tx ) {
        setTexture( tx instanceof SmartTexture ? (SmartTexture) tx : TextureCache.get( tx ) );
        frame( new RectF( 0, 0, 1, 1 ) );
    }

    public void frame ( RectF frame ) {
        this.setFrame( frame );

        width = frame.width() * getTexture().getWidth();
        height = frame.height() * getTexture().getHeight();

        updateFrame();
        updateVertices();
    }

    public void frame ( int left, int top, int width, int height ) {
        frame( getTexture().uvRect( left, top, left + width, top + height ) );
    }

    public RectF frame () {
        return new RectF( getFrame() );
    }

    public void copy ( Image other ) {
        setTexture( other.getTexture() );
        setFrame( new RectF( other.getFrame() ) );

        width = other.width;
        height = other.height;

        updateFrame();
        updateVertices();
    }

    protected void updateFrame () {

        if ( isFlipHorizontal() ) {
            getVertices()[2] = getFrame().right;
            getVertices()[6] = getFrame().left;
            getVertices()[10] = getFrame().left;
            getVertices()[14] = getFrame().right;
        } else {
            getVertices()[2] = getFrame().left;
            getVertices()[6] = getFrame().right;
            getVertices()[10] = getFrame().right;
            getVertices()[14] = getFrame().left;
        }

        getVertices()[3] = getFrame().top;
        getVertices()[7] = getFrame().top;
        getVertices()[11] = getFrame().bottom;
        getVertices()[15] = getFrame().bottom;

        setDirty( true );
    }

    protected void updateVertices () {

        getVertices()[0] = 0;
        getVertices()[1] = 0;

        getVertices()[4] = width;
        getVertices()[5] = 0;

        getVertices()[8] = width;
        getVertices()[9] = height;

        getVertices()[12] = 0;
        getVertices()[13] = height;

        setDirty( true );
    }

    @SuppressWarnings ( "FeatureEnvy" )
    @Override
    public void draw () {

        super.draw();

        NoosaScript script = NoosaScript.get();

        getTexture().bind();

        script.camera( camera() );

        script.uModel.valueM4( matrix );
        script.lighting(
                rm, gm, bm, am,
                ra, ga, ba, aa );

        if ( isDirty() ) {
            verticesBuffer.position( 0 );
            verticesBuffer.put( getVertices() );
            setDirty( false );
        }
        script.drawQuad( verticesBuffer );

    }

    private RectF getFrame () {
        return frame;
    }

    private void setFrame ( RectF frame ) {
        this.frame = frame;
    }

    public SmartTexture getTexture () {
        return texture;
    }

    private void setTexture ( SmartTexture texture ) {
        this.texture = texture;
    }

    public boolean isFlipHorizontal () {
        return flipHorizontal;
    }

    public void setFlipHorizontal ( boolean flipHorizontal ) {
        this.flipHorizontal = flipHorizontal;
    }

    @SuppressWarnings ( "AssignmentOrReturnOfFieldWithMutableType" )
    public float[] getVertices () {
        return vertices;
    }

    private boolean isDirty () {
        return dirty;
    }

    public void setDirty ( boolean dirty ) {
        this.dirty = dirty;
    }
}
