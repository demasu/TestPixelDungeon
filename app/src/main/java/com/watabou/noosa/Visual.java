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
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;

public class Visual extends Gizmo {

    private float x;
    private float y;
    private float width;
    private float height;

    private PointF scale;
    private final PointF origin;
    private float rm;
    private float gm;
    private float bm;
    private float am;
    private float ra;
    private float ga;
    private float ba;
    private float aa;
    private final PointF speed;
    private final PointF acc;
    private float angle;
    private float angularSpeed;
    private final float[] matrix;

    public Visual ( float x, float y, float width, float height ) {
        this.setX( x );
        this.setY( y );
        this.setWidth( width );
        this.setHeight( height );

        setScale( new PointF( 1, 1 ) );
        origin = new PointF();

        matrix = new float[16];

        resetColor();

        speed = new PointF();
        acc = new PointF();
    }

    @Override
    public void update () {
        updateMotion();
    }

    @Override
    public void draw () {
        updateMatrix();
    }

    @SuppressWarnings ( "FeatureEnvy" )
    protected void updateMatrix () {
        Matrix.setIdentity( getMatrix() );
        Matrix.translate( getMatrix(), getX(), getY() );
        Matrix.translate( getMatrix(), getOrigin().getX(), getOrigin().getY() );
        if ( getAngle() != 0 ) {
            Matrix.rotate( getMatrix(), getAngle() );
        }
        if ( getScale().getX() != 1 || getScale().getY() != 1 ) {
            Matrix.scale( getMatrix(), getScale().getX(), getScale().getY() );
        }
        Matrix.translate( getMatrix(), -getOrigin().getX(), -getOrigin().getY() );
    }

    public PointF point () {
        return new PointF( getX(), getY() );
    }

    public PointF point ( PointF p ) {
        setX( p.getX() );
        setY( p.getY() );
        return p;
    }

    public PointF center () {
        return new PointF( getX() + getWidth() / 2, getY() + getHeight() / 2 );
    }

    public float width () {
        return getWidth() * getScale().getX();
    }

    public float height () {
        return getHeight() * getScale().getY();
    }

    @SuppressWarnings ( "FeatureEnvy" )
    private void updateMotion () {

        float elapsed = Game.getElapsed();

        float d = ( GameMath.speed( getSpeed().getX(), getAcc().getX() ) - getSpeed().getX() ) / 2;
        getSpeed().setX( getSpeed().getX() + d );
        setX( getX() + getSpeed().getX() * elapsed );
        getSpeed().setX( getSpeed().getX() + d );

        d = ( GameMath.speed( getSpeed().getY(), getAcc().getY() ) - getSpeed().getY() ) / 2;
        getSpeed().setY( getSpeed().getY() + d );
        setY( getY() + getSpeed().getY() * elapsed );
        getSpeed().setY( getSpeed().getY() + d );

        setAngle( getAngle() + getAngularSpeed() * elapsed );
    }

    public void alpha ( float value ) {
        setAm( value );
        setAa( 0 );
    }

    public float alpha () {
        return getAm() + getAa();
    }

    public void lightness ( float value ) {
        final float HALF  = 0.5f;
        final float TWICE = 2f;
        if ( value < HALF ) {
            setRm( value * TWICE );
            setGm( value * TWICE );
            setBm( value * TWICE );
            setRa( 0 );
            setGa( 0 );
            setBa( 0 );
        } else {
            setRm( TWICE - value * TWICE );
            setGm( TWICE - value * TWICE );
            setBm( TWICE - value * TWICE );
            setRa( value * TWICE - 1f );
            setGa( value * TWICE - 1f );
            setBa( value * TWICE - 1f );
        }
    }

    public void brightness ( float value ) {
        setRm( value );
        setGm( value );
        setBm( value );
    }

    public void tint ( float r, float g, float b, float strength ) {
        setRm( 1f - strength );
        setGm( 1f - strength );
        setBm( 1f - strength );
        setRa( r * strength );
        setGa( g * strength );
        setBa( b * strength );
    }

    public void tint ( int color, float strength ) {
        setRm( 1f - strength );
        setGm( 1f - strength );
        setBm( 1f - strength );
        final int BITSHIFT  = 16;
        final int BIT_MASK  = 0xFF;
        final float STR_MOD = 255f;
        setRa( ( ( color >> BITSHIFT ) & BIT_MASK ) / STR_MOD * strength );
        setGa( ( ( color >> 8 ) & BIT_MASK ) / STR_MOD * strength );
        setBa( ( color & BIT_MASK ) / STR_MOD * strength );
    }

    private void color ( float r, float g, float b ) {
        setRm( 0 );
        setGm( 0 );
        setBm( 0 );
        setRa( r );
        setGa( g );
        setBa( b );
    }

    public void color ( int color ) {
        final int BITSHIFT = 16;
        final int BIT_MASK = 0xFF;
        final float DENOM  = 255f;
        color( ( ( color >> BITSHIFT ) & BIT_MASK ) / DENOM, ( ( color >> 8 ) & BIT_MASK ) / DENOM, ( color & BIT_MASK ) / DENOM );
    }

    public void hardlight ( float r, float g, float b ) {
        setRa( 0 );
        setGa( 0 );
        setBa( 0 );
        setRm( r );
        setGm( g );
        setBm( b );
    }

    public void hardlight ( int color ) {
        final int BITSHIFT = 16;
        final int BIT_MASK = 0xFF;
        final float DENOM  = 255f;
        hardlight( ( color >> BITSHIFT ) / DENOM, ( ( color >> 8 ) & BIT_MASK ) / DENOM, ( color & BIT_MASK ) / DENOM );
    }

    public void resetColor () {
        setRm( 1 );
        setGm( 1 );
        setBm( 1 );
        setAm( 1 );
        setRa( 0 );
        setGa( 0 );
        setBa( 0 );
        setAa( 0 );
    }

    public boolean overlapsPoint ( float x, float y ) {
        return x >= this.getX() && x < this.getX() + getWidth() * getScale().getX() && y >= this.getY() && y < this.getY() + getHeight() * getScale().getY();
    }

    public boolean overlapsScreenPoint ( int x, int y ) {
        Camera c = camera();
        if ( c != null ) {
            PointF p = c.screenToCamera( x, y );
            return overlapsPoint( p.getX(), p.getY() );
        } else {
            return false;
        }
    }

    // true if its bounding box intersects its camera's bounds
    @SuppressWarnings ( "FeatureEnvy" )
    @Override
    public boolean isVisible () {
        Camera c = camera();
        float cx = c.getScroll().getX();
        float cy = c.getScroll().getY();
        float w = width();
        float h = height();
        return getX() + w >= cx && getY() + h >= cy && getX() < cx + c.getWidth() && getY() < cy + c.getHeight();
    }

    public float getX () {
        return x;
    }

    public void setX ( float x ) {
        this.x = x;
    }

    public float getY () {
        return y;
    }

    public void setY ( float y ) {
        this.y = y;
    }

    public float getWidth () {
        return width;
    }

    public void setWidth ( float width ) {
        this.width = width;
    }

    public float getHeight () {
        return height;
    }

    public void setHeight ( float height ) {
        this.height = height;
    }

    public PointF getScale () {
        return scale;
    }

    public void setScale ( PointF scale ) {
        this.scale = scale;
    }

    public PointF getOrigin () {
        return origin;
    }

    public float getRm () {
        return rm;
    }

    public void setRm ( float rm ) {
        this.rm = rm;
    }

    public float getGm () {
        return gm;
    }

    public void setGm ( float gm ) {
        this.gm = gm;
    }

    public float getBm () {
        return bm;
    }

    public void setBm ( float bm ) {
        this.bm = bm;
    }

    public float getAm () {
        return am;
    }

    public void setAm ( float am ) {
        this.am = am;
    }

    public float getRa () {
        return ra;
    }

    public void setRa ( float ra ) {
        this.ra = ra;
    }

    public float getGa () {
        return ga;
    }

    public void setGa ( float ga ) {
        this.ga = ga;
    }

    public float getBa () {
        return ba;
    }

    public void setBa ( float ba ) {
        this.ba = ba;
    }

    public float getAa () {
        return aa;
    }

    public void setAa ( float aa ) {
        this.aa = aa;
    }

    public PointF getSpeed () {
        return speed;
    }

    public PointF getAcc () {
        return acc;
    }

    public float getAngle () {
        return angle;
    }

    public void setAngle ( float angle ) {
        this.angle = angle;
    }

    public float getAngularSpeed () {
        return angularSpeed;
    }

    public void setAngularSpeed ( float angularSpeed ) {
        this.angularSpeed = angularSpeed;
    }

    @SuppressWarnings ( "AssignmentOrReturnOfFieldWithMutableType" )
    public float[] getMatrix () {
        return matrix;
    }
}
