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

import android.opengl.GLES20;

import com.watabou.glscripts.Script;
import com.watabou.glwrap.Attribute;
import com.watabou.glwrap.Quad;
import com.watabou.glwrap.Uniform;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class NoosaScript extends Script {

    public static final String SHADER =

            "uniform mat4 uCamera;" +
                    "uniform mat4 uModel;" +
                    "attribute vec4 aXYZW;" +
                    "attribute vec2 aUV;" +
                    "varying vec2 vUV;" +
                    "void main() {" +
                    "  gl_Position = uCamera * uModel * aXYZW;" +
                    "  vUV = aUV;" +
                    "}" +

                    "//\n" +

                    "precision mediump float;" +
                    "varying vec2 vUV;" +
                    "uniform sampler2D uTex;" +
                    "uniform vec4 uColorM;" +
                    "uniform vec4 uColorA;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( uTex, vUV ) * uColorM + uColorA;" +
                    "}";
    private final Uniform uCamera;
    private final Uniform uModel;
    @SuppressWarnings ( { "unused", "FieldCanBeLocal" } )
    private final Uniform uTex;
    private final Uniform uColorM;
    private final Uniform uColorA;
    private final Attribute aXY;
    private final Attribute aUV;
    private Camera lastCamera;

    private NoosaScript () {

        super();
        compile();

        uCamera = uniform( "uCamera" );
        uModel = uniform( "uModel" );
        uTex = uniform( "uTex" );
        uColorM = uniform( "uColorM" );
        uColorA = uniform( "uColorA" );
        aXY = attribute( "aXYZW" );
        aUV = attribute( "aUV" );

    }

    public static NoosaScript get () {
        return Script.use( NoosaScript.class );
    }

    @Override
    public void use () {

        super.use();

        aXY.enable();
        aUV.enable();

    }

    public void drawElements ( FloatBuffer vertices, ShortBuffer indices, int size ) {

        vertices.position( 0 );
        aXY.vertexPointer( 2, 4, vertices );

        vertices.position( 2 );
        aUV.vertexPointer( 2, 4, vertices );

        GLES20.glDrawElements( GLES20.GL_TRIANGLES, size, GLES20.GL_UNSIGNED_SHORT, indices );

    }

    public void drawQuad ( FloatBuffer vertices ) {

        vertices.position( 0 );
        aXY.vertexPointer( 2, 4, vertices );

        vertices.position( 2 );
        aUV.vertexPointer( 2, 4, vertices );

        GLES20.glDrawElements( GLES20.GL_TRIANGLES, Quad.SIZE, GLES20.GL_UNSIGNED_SHORT, Quad.getIndices( 1 ) );

    }

    public void drawQuadSet ( FloatBuffer vertices, int size ) {

        if ( size == 0 ) {
            return;
        }

        vertices.position( 0 );
        aXY.vertexPointer( 2, 4, vertices );

        vertices.position( 2 );
        aUV.vertexPointer( 2, 4, vertices );

        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                Quad.SIZE * size,
                GLES20.GL_UNSIGNED_SHORT,
                Quad.getIndices( size ) );

    }

    public void lighting ( float rm, float gm, float bm, float am, float ra, float ga, float ba, float aa ) {
        uColorM.value4f( rm, gm, bm, am );
        uColorA.value4f( ra, ga, ba, aa );
    }

    @SuppressWarnings ( "AssignmentToNull" )
    public void resetCamera () {
        lastCamera = null;
    }

    @SuppressWarnings ( "FeatureEnvy" )
    public void camera ( Camera camera ) {
        Camera camera1 = camera;
        if ( camera1 == null ) {
            camera1 = Camera.getMain();
        }
        if ( camera1 != lastCamera ) {
            lastCamera = camera1;
            uCamera.valueM4( camera1.getMatrix() );

            GLES20.glScissor(
                    camera1.getX(),
                    Game.getHeight() - camera1.getScreenHeight() - camera1.getY(),
                    camera1.getScreenWidth(),
                    camera1.getScreenHeight() );
        }
    }

    public Uniform getuModel () {
        return uModel;
    }
}
