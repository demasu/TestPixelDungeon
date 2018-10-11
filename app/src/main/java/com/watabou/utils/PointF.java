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

package com.watabou.utils;

import android.annotation.SuppressLint;

@SuppressLint ( "FloatMath" )
public class PointF {

    public static final float PI = 3.1415926f;
    public static final float PI2 = PI * 2;
    public static final float G2R = PI / 180;

    private float x;
    private float y;

    public PointF () {
    }

    public PointF ( float x, float y ) {
        this.setX( x );
        this.setY( y );
    }

    public PointF ( PointF p ) {
        this.setX( p.getX() );
        this.setY( p.getY() );
    }

    public static PointF diff ( PointF a, PointF b ) {
        return new PointF( a.getX() - b.getX(), a.getY() - b.getY() );
    }

    public static PointF inter ( PointF a, PointF b, float d ) {
        return new PointF( a.getX() + ( b.getX() - a.getX() ) * d, a.getY() + ( b.getY() - a.getY() ) * d );
    }

    public static float distance ( PointF a, PointF b ) {
        float dx = a.getX() - b.getX();
        float dy = a.getY() - b.getY();
        return (float) Math.sqrt( dx * dx + dy * dy );
    }

    public static float angle ( PointF start, PointF end ) {
        return (float) Math.atan2( end.getY() - start.getY(), end.getX() - start.getX() );
    }

    public PointF scale ( float f ) {
        this.setX( this.getX() * f );
        this.setY( this.getY() * f );
        return this;
    }

    public PointF invScale ( float f ) {
        this.setX( this.getX() / f );
        this.setY( this.getY() / f );
        return this;
    }

    public PointF set ( float x, float y ) {
        this.setX( x );
        this.setY( y );
        return this;
    }

    public PointF set ( PointF p ) {
        this.setX( p.getX() );
        this.setY( p.getY() );
        return this;
    }

    public PointF set ( float v ) {
        this.setX( v );
        this.setY( v );
        return this;
    }

    public PointF polar ( float a, float l ) {
        this.setX( l * (float) Math.cos( a ) );
        this.setY( l * (float) Math.sin( a ) );
        return this;
    }

    public PointF offset ( float dx, float dy ) {
        setX( getX() + dx );
        setY( getY() + dy );
        return this;
    }

    public PointF offset ( PointF p ) {
        setX( getX() + p.getX() );
        setY( getY() + p.getY() );
        return this;
    }

    public PointF negate () {
        setX( -getX() );
        setY( -getY() );
        return this;
    }

    public PointF normalize () {
        float l = length();
        setX( getX() / l );
        setY( getY() / l );
        return this;
    }

    public Point floor () {
        return new Point( (int) getX(), (int) getY() );
    }

    public float length () {
        return (float) Math.sqrt( getX() * getX() + getY() * getY() );
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
}
