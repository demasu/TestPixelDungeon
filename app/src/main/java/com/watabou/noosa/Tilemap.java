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
import com.watabou.utils.Rect;

import java.nio.FloatBuffer;

public class Tilemap extends Visual {

    public final Rect updated;
    protected final SmartTexture texture;
    protected final TextureFilm tileset;
    private int[] data;
    private int mapWidth;
    private int size;
    private final float[] vertices;
    private FloatBuffer quads;
    private final float cellW;
    private final float cellH;

    @SuppressWarnings ( "FeatureEnvy" )
    public Tilemap ( Object tx, TextureFilm tileset ) {

        super( 0, 0, 0, 0 );

        this.texture = TextureCache.get( tx );
        this.tileset = tileset;

        RectF r = tileset.get( 0 );
        cellW = tileset.width( r );
        cellH = tileset.height( r );

        vertices = new float[16];

        updated = new Rect();
    }

    public void map ( int[] data, int cols ) {

        this.data = data;

        mapWidth = cols;
        int mapHeight = data.length / cols;
        size = mapWidth * mapHeight;

        width = cellW * mapWidth;
        height = cellH * mapHeight;

        quads = Quad.createSet( size );

        updated.set( 0, 0, mapWidth, mapHeight );
    }

    @SuppressWarnings ( "MagicNumber" )
    private void updateVertices () {

        float y1 = cellH * updated.top;
        float y2 = y1 + cellH;

        for ( int i = updated.top; i < updated.bottom; i++ ) {

            float x1 = cellW * updated.left;
            float x2 = x1 + cellW;

            int pos = i * mapWidth + updated.left;
            final int POS_MODIFIER = 16;
            quads.position( POS_MODIFIER * pos );

            for ( int j = updated.left; j < updated.right; j++ ) {

                RectF uv = tileset.get( data[pos] );
                pos++;

                vertices[0] = x1;
                vertices[1] = y1;

                vertices[2] = uv.left;
                vertices[3] = uv.top;

                vertices[4] = x2;
                vertices[5] = y1;

                vertices[6] = uv.right;
                vertices[7] = uv.top;

                vertices[8] = x2;
                vertices[9] = y2;

                vertices[10] = uv.right;
                vertices[11] = uv.bottom;

                vertices[12] = x1;
                vertices[13] = y2;

                vertices[14] = uv.left;
                vertices[15] = uv.bottom;

                quads.put( vertices );

                x1 = x2;
                x2 += cellW;

            }

            y1 = y2;
            y2 += cellH;
        }

        updated.setEmpty();
    }

    @SuppressWarnings ( "FeatureEnvy" )
    @Override
    public void draw () {

        super.draw();

        NoosaScript script = NoosaScript.get();

        texture.bind();

        script.getuModel().valueM4( matrix );
        script.lighting(
                rm, gm, bm, am,
                ra, ga, ba, aa );

        if ( !updated.isEmpty() ) {
            updateVertices();
        }

        script.camera( getCamera() );
        script.drawQuadSet( quads, size );

    }
}
