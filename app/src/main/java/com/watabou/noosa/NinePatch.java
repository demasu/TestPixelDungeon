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

public class NinePatch extends Visual {

    public final SmartTexture texture;

    private final float[] vertices;
    private final FloatBuffer verticesBuffer;

    private final RectF outterF;
    private final RectF innerF;

    private final int marginLeft;
    private final int marginRight;
    private final int marginTop;
    private final int marginBottom;

    public NinePatch ( Object tx, int margin ) {
        this( tx, margin, margin, margin, margin );
    }

    public NinePatch ( Object tx, int left, int top, int right, int bottom ) {
        this( tx, 0, 0, 0, 0, left, top, right, bottom );
    }

    public NinePatch ( Object tx, int x, int y, int w, int h, int margin ) {
        this( tx, x, y, w, h, margin, margin, margin, margin );
    }

    public NinePatch ( Object tx, int x, int y, int w, int h, int left, int top, int right, int bottom ) {
        super( 0, 0, 0, 0 );

        texture = TextureCache.get( tx );
        int w1 = w == 0 ? texture.getWidth() : w;
        int h1 = h == 0 ? texture.getHeight() : h;

        width = w1;
        height = h1;

        vertices = new float[16];
        verticesBuffer = Quad.createSet( 9 );

        marginLeft = left;
        marginRight = right;
        marginTop = top;
        marginBottom = bottom;

        outterF = texture.uvRect( x, y, x + w1, y + h1 );
        innerF = texture.uvRect( x + left, y + top, x + w1 - right, y + h1 - bottom );

        updateVertices();
    }

    private void updateVertices () {

        verticesBuffer.position( 0 );

        float right = width - marginRight;
        float bottom = height - marginBottom;

        Quad.fill( vertices,
                0, marginLeft, 0, marginTop, outterF.left, innerF.left, outterF.top, innerF.top );
        verticesBuffer.put( vertices );
        Quad.fill( vertices,
                marginLeft, right, 0, marginTop, innerF.left, innerF.right, outterF.top, innerF.top );
        verticesBuffer.put( vertices );
        Quad.fill( vertices,
                right, width, 0, marginTop, innerF.right, outterF.right, outterF.top, innerF.top );
        verticesBuffer.put( vertices );

        Quad.fill( vertices,
                0, marginLeft, marginTop, bottom, outterF.left, innerF.left, innerF.top, innerF.bottom );
        verticesBuffer.put( vertices );
        Quad.fill( vertices,
                marginLeft, right, marginTop, bottom, innerF.left, innerF.right, innerF.top, innerF.bottom );
        verticesBuffer.put( vertices );
        Quad.fill( vertices,
                right, width, marginTop, bottom, innerF.right, outterF.right, innerF.top, innerF.bottom );
        verticesBuffer.put( vertices );

        Quad.fill( vertices,
                0, marginLeft, bottom, height, outterF.left, innerF.left, innerF.bottom, outterF.bottom );
        verticesBuffer.put( vertices );
        Quad.fill( vertices,
                marginLeft, right, bottom, height, innerF.left, innerF.right, innerF.bottom, outterF.bottom );
        verticesBuffer.put( vertices );
        Quad.fill( vertices,
                right, width, bottom, height, innerF.right, outterF.right, innerF.bottom, outterF.bottom );
        verticesBuffer.put( vertices );
    }

    public int marginLeft () {
        return marginLeft;
    }

    public int marginRight () {
        return marginRight;
    }

    public int marginTop () {
        return marginTop;
    }

    public int marginBottom () {
        return marginBottom;
    }

    public int marginHor () {
        return marginLeft + marginRight;
    }

    public int marginVer () {
        return marginTop + marginBottom;
    }

    public void size ( float width, float height ) {
        this.width = width;
        this.height = height;
        updateVertices();
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

        script.drawQuadSet( verticesBuffer, 9 );

    }
}
