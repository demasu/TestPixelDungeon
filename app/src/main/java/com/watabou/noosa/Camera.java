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

import com.watabou.glwrap.Matrix;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Camera extends Gizmo {

    private static Camera main;
    private static final ArrayList<Camera> all = new ArrayList<>();
    private static float invW2;
    private static float invH2;
    private float zoom;

    private int x;
    private int y;
    private int width;
    private int height;
    private float[] matrix;
    private final PointF scroll;
    private Visual target;
    private float shakeX;
    private float shakeY;
    private int screenWidth;
    private int screenHeight;
    @SuppressWarnings ( "MagicNumber" )
    private float shakeMagX = 10f;
    @SuppressWarnings ( "MagicNumber" )
    private float shakeMagY = 10f;
    private float shakeTime = 0f;
    private float shakeDuration = 1f;

    public Camera ( int x, int y, int width, int height, float zoom ) {

        this.setX( x );
        this.setY( y );
        this.setWidth( width );
        this.setHeight( height );
        this.setZoom( zoom );

        setScreenWidth( (int) ( width * zoom ) );
        setScreenHeight( (int) ( height * zoom ) );

        scroll = new PointF();

        //noinspection MagicNumber
        setMatrix( new float[16] );
        Matrix.setIdentity( getMatrix() );
    }

    public static Camera reset () {
        return reset( createFullscreen( 1 ) );
    }

    @SuppressWarnings ( "MagicNumber" )
    public static Camera reset ( Camera newCamera ) {

        setInvW2( 2f / Game.getWidth() );
        setInvH2( 2f / Game.getHeight() );

        int length = all.size();
        for ( int i = 0; i < length; i++ ) {
            all.get( i ).destroy();
        }
        all.clear();

        setMain( add( newCamera ) );
        return getMain();
    }

    public static Camera add ( Camera camera ) {
        all.add( camera );
        return camera;
    }

    public static Camera remove ( Camera camera ) {
        all.remove( camera );
        return camera;
    }

    public static void updateAll () {
        int length = all.size();
        for ( int i = 0; i < length; i++ ) {
            Camera c = all.get( i );
            if ( c.isExists() && c.isActive() ) {
                c.update();
            }
        }
    }

    @SuppressWarnings ( "FeatureEnvy" )
    public static Camera createFullscreen ( float zoom ) {
        int w = (int) Math.ceil( Game.getWidth() / zoom );
        int h = (int) Math.ceil( Game.getHeight() / zoom );
        return new Camera(
                (int) ( Game.getWidth() - w * zoom ) / 2,
                (int) ( Game.getHeight() - h * zoom ) / 2,
                w, h, zoom );
    }

    public static Camera getMain () {
        return main;
    }

    private static void setMain ( Camera main ) {
        Camera.main = main;
    }

    public static float getInvW2 () {
        return invW2;
    }

    private static void setInvW2 ( float invW2 ) {
        Camera.invW2 = invW2;
    }

    public static float getInvH2 () {
        return invH2;
    }

    private static void setInvH2 ( float invH2 ) {
        Camera.invH2 = invH2;
    }

    @Override
    public void destroy () {
        setTarget( null );
        setMatrix( null );
    }

    public void zoom ( float value ) {
        zoom( value,
                getScroll().x + getWidth() / 2,
                getScroll().y + getHeight() / 2 );
    }

    private void zoom ( float value, float fx, float fy ) {

        setZoom( value );
        setWidth( (int) ( getScreenWidth() / getZoom() ) );
        setHeight( (int) ( getScreenHeight() / getZoom() ) );

        focusOn( fx, fy );
    }

    public void resize ( int width, int height ) {
        this.setWidth( width );
        this.setHeight( height );
        setScreenWidth( (int) ( width * getZoom() ) );
        setScreenHeight( (int) ( height * getZoom() ) );
    }

    @Override
    public void update () {
        super.update();

        if ( getTarget() != null ) {
            focusOn( getTarget() );
        }

        shakeTime -= Game.getElapsed();
        if ( ( shakeTime ) > 0 ) {
            float damping = shakeTime / shakeDuration;
            setShakeX( Random.Float( -shakeMagX, +shakeMagX ) * damping );
            setShakeY( Random.Float( -shakeMagY, +shakeMagY ) * damping );
        } else {
            setShakeX( 0 );
            setShakeY( 0 );
        }

        updateMatrix();
    }

    public PointF center () {
        return new PointF( getWidth() / 2, getHeight() / 2 );
    }

    private void focusOn ( float x, float y ) {
        getScroll().set( x - getWidth() / 2, y - getHeight() / 2 );
    }

    private void focusOn ( PointF point ) {
        focusOn( point.x, point.y );
    }


    public void focusOn ( Visual visual ) {
        focusOn( visual.center() );
    }

    public PointF screenToCamera ( int x, int y ) {
        return new PointF(
                ( x - this.getX() ) / getZoom() + getScroll().x,
                ( y - this.getY() ) / getZoom() + getScroll().y );
    }

    public Point cameraToScreen ( float x, float y ) {
        return new Point(
                (int) ( ( x - getScroll().x ) * getZoom() + this.getX() ),
                (int) ( ( y - getScroll().y ) * getZoom() + this.getY() ) );
    }

    public float screenWidth () {
        return getWidth() * getZoom();
    }

    public float screenHeight () {
        return getHeight() * getZoom();
    }


    @SuppressWarnings ( "MagicNumber" )
    protected void updateMatrix () {

    /*	Matrix.setIdentity( matrix );
        Matrix.translate( matrix, -1, +1 );
        Matrix.scale( matrix, 2f / G.width, -2f / G.height );
        Matrix.translate( matrix, x, y );
        Matrix.scale( matrix, zoom, zoom );
        Matrix.translate( matrix, scroll.x, scroll.y );*/

        getMatrix()[0] = +getZoom() * getInvW2();
        getMatrix()[5] = -getZoom() * getInvH2();

        getMatrix()[12] = -1 + getX() * getInvW2() - ( getScroll().x + getShakeX() ) * getMatrix()[0];
        getMatrix()[13] = +1 - getY() * getInvH2() - ( getScroll().y + getShakeY() ) * getMatrix()[5];

    }

    public void shake ( float magnitude, float duration ) {
        shakeMagX = magnitude;
        shakeMagY = magnitude;
        shakeTime = duration;
        shakeDuration = duration;
    }

    public int getScreenWidth () {
        return screenWidth;
    }

    private void setScreenWidth ( int screenWidth ) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight () {
        return screenHeight;
    }

    private void setScreenHeight ( int screenHeight ) {
        this.screenHeight = screenHeight;
    }

    public float getZoom () {
        return zoom;
    }

    private void setZoom ( float zoom ) {
        this.zoom = zoom;
    }

    public int getX () {
        return x;
    }

    public void setX ( int x ) {
        this.x = x;
    }

    public int getY () {
        return y;
    }

    public void setY ( int y ) {
        this.y = y;
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

    @SuppressWarnings ( "AssignmentOrReturnOfFieldWithMutableType" )
    public float[] getMatrix () {
        return matrix;
    }

    private void setMatrix ( float[] matrix ) {
        this.matrix = matrix;
    }

    public PointF getScroll () {
        return scroll;
    }

    private Visual getTarget () {
        return target;
    }

    public void setTarget ( Visual target ) {
        this.target = target;
    }

    public float getShakeX () {
        return shakeX;
    }

    private void setShakeX ( float shakeX ) {
        this.shakeX = shakeX;
    }

    public float getShakeY () {
        return shakeY;
    }

    private void setShakeY ( float shakeY ) {
        this.shakeY = shakeY;
    }
}
