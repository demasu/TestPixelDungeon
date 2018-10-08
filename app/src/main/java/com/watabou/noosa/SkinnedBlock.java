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

import com.watabou.glwrap.Texture;

public class SkinnedBlock extends Image {

    private boolean autoAdjust = false;
    private float scaleX;
    private float scaleY;
    private float offsetX;
    private float offsetY;

    public SkinnedBlock ( float width, float height, Object tx ) {
        super( tx );

        getTexture().wrap( Texture.REPEAT, Texture.REPEAT );

        size( width, height );
    }

    @Override
    public void frame ( RectF frame ) {
        scaleX = 1;
        scaleY = 1;

        offsetX = 0;
        offsetY = 0;

        super.frame( new RectF( 0, 0, 1, 1 ) );
    }

    @SuppressWarnings ( "MagicNumber" )
    @Override
    protected void updateFrame () {

        if ( isAutoAdjust() ) {
            while ( offsetX > getTexture().getWidth() ) {
                offsetX -= getTexture().getWidth();
            }
            while ( offsetX < -getTexture().getWidth() ) {
                offsetX += getTexture().getWidth();
            }
            while ( offsetY > getTexture().getHeight() ) {
                offsetY -= getTexture().getHeight();
            }
            while ( offsetY < -getTexture().getHeight() ) {
                offsetY += getTexture().getHeight();
            }
        }

        float tw = 1f / getTexture().getWidth();
        float th = 1f / getTexture().getHeight();

        float u0 = offsetX * tw;
        float v0 = offsetY * th;
        float u1 = u0 + width * tw / scaleX;
        float v1 = v0 + height * th / scaleY;

        getVertices()[2] = u0;
        getVertices()[3] = v0;

        getVertices()[6] = u1;
        getVertices()[7] = v0;

        getVertices()[10] = u1;
        getVertices()[11] = v1;

        getVertices()[14] = u0;
        getVertices()[15] = v1;

        setDirty( true );
    }

    public void offsetTo ( float x, float y ) {
        offsetX = x;
        offsetY = y;
        updateFrame();
    }

    public void offset ( float x, float y ) {
        offsetX += x;
        offsetY += y;
        updateFrame();
    }

    public float offsetY () {
        return offsetY;
    }

    public void size ( float w, float h ) {
        this.width = w;
        this.height = h;
        updateFrame();
        updateVertices();
    }

    private boolean isAutoAdjust () {
        return autoAdjust;
    }

    public void setAutoAdjust ( boolean autoAdjust ) {
        this.autoAdjust = autoAdjust;
    }
}
