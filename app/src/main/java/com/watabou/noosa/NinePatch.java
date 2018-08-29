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

import java.nio.FloatBuffer;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Quad;

import android.graphics.RectF;

public class NinePatch extends Visual {

    protected final SmartTexture texture;

    private final float[] vertices;
    private final FloatBuffer verticesBuffer;

    private final RectF outterF;
    private final RectF innerF;

    private final int marginLeft;
    private final int marginRight;
    private final int marginTop;
    private final int marginBottom;

    // --Commented out by Inspection (8/28/18, 6:49 PM):private final float nWidth;
    // --Commented out by Inspection (8/28/18, 6:49 PM):private final float nHeight;

    protected NinePatch() {
        this(com.demasu.testpixeldungeon.Assets.SHADOW, 1, 1, 1, 1);
    }

    public NinePatch(Object tx, int left, int top, int right, int bottom) {
        this(tx, 0, 0, 0, 0, left, top, right, bottom);
    }

    public NinePatch(Object tx, int x, int y, int w, int h, int margin) {
        this(tx, x, y, w, h, margin, margin, margin, margin);
    }

    public NinePatch(Object tx, int x, int y, int w, int h, int left, int top, int right, int bottom) {
        super(0, 0, 0, 0);

        texture = TextureCache.get(tx);
        w = w == 0 ? texture.width : w;
        h = h == 0 ? texture.height : h;

        //nWidth = width = w;
        //nHeight = height = h;

        vertices = new float[16];
        verticesBuffer = Quad.createSet(9);

        marginLeft = left;
        marginRight = right;
        marginTop = top;
        marginBottom = bottom;

        outterF = texture.uvRect(x, y, x + w, y + h);
        innerF = texture.uvRect(x + left, y + top, x + w - right, y + h - bottom);

        updateVertices();
    }

    private void updateVertices() {

        verticesBuffer.position(0);

        float right = width - marginRight;
        float bottom = height - marginBottom;

        Quad.fill(vertices,
                0, marginLeft, 0, marginTop, outterF.left, innerF.left, outterF.top, innerF.top);
        verticesBuffer.put(vertices);
        Quad.fill(vertices,
                marginLeft, right, 0, marginTop, innerF.left, innerF.right, outterF.top, innerF.top);
        verticesBuffer.put(vertices);
        Quad.fill(vertices,
                right, width, 0, marginTop, innerF.right, outterF.right, outterF.top, innerF.top);
        verticesBuffer.put(vertices);

        Quad.fill(vertices,
                0, marginLeft, marginTop, bottom, outterF.left, innerF.left, innerF.top, innerF.bottom);
        verticesBuffer.put(vertices);
        Quad.fill(vertices,
                marginLeft, right, marginTop, bottom, innerF.left, innerF.right, innerF.top, innerF.bottom);
        verticesBuffer.put(vertices);
        Quad.fill(vertices,
                right, width, marginTop, bottom, innerF.right, outterF.right, innerF.top, innerF.bottom);
        verticesBuffer.put(vertices);

        Quad.fill(vertices,
                0, marginLeft, bottom, height, outterF.left, innerF.left, innerF.bottom, outterF.bottom);
        verticesBuffer.put(vertices);
        Quad.fill(vertices,
                marginLeft, right, bottom, height, innerF.left, innerF.right, innerF.bottom, outterF.bottom);
        verticesBuffer.put(vertices);
        Quad.fill(vertices,
                right, width, bottom, height, innerF.right, outterF.right, innerF.bottom, outterF.bottom);
        verticesBuffer.put(vertices);
    }

    public int marginLeft() {
        return marginLeft;
    }

    public int marginRight() {
        return marginRight;
    }

    public int marginTop() {
        return marginTop;
    }

    public int marginBottom() {
        return marginBottom;
    }

    public int marginHor() {
        return marginLeft + marginRight;
    }

    public int marginVer() {
        return marginTop + marginBottom;
    }

// --Commented out by Inspection START (8/28/18, 6:49 PM):
//    public float innerWidth() {
//        return width - marginLeft - marginRight;
//    }
// --Commented out by Inspection STOP (8/28/18, 6:49 PM)

// --Commented out by Inspection START (8/28/18, 6:49 PM):
//    public float innerHeight() {
//        return height - marginTop - marginBottom;
//    }
// --Commented out by Inspection STOP (8/28/18, 6:49 PM)

// --Commented out by Inspection START (8/28/18, 6:49 PM):
//    public float innerRight() {
//        return width - marginRight;
//    }
// --Commented out by Inspection STOP (8/28/18, 6:49 PM)

// --Commented out by Inspection START (8/28/18, 6:49 PM):
//    public float innerBottom() {
//        return height - marginBottom;
//    }
// --Commented out by Inspection STOP (8/28/18, 6:49 PM)

    public void size(float width, float height) {
        this.width = width;
        this.height = height;
        updateVertices();
    }

    @Override
    public void draw() {

        super.draw();

        NoosaScript script = NoosaScript.get();

        texture.bind();

        script.camera(camera());

        script.uModel.valueM4(matrix);
        script.lighting(
                rm, gm, bm, am,
                ra, ga, ba, aa);

        script.drawQuadSet(verticesBuffer, 9);

    }
}
