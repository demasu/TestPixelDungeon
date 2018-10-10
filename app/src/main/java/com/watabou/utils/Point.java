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

public class Point {

    private int x;
    private int y;

    public Point () {
    }

    public Point ( int x, int y ) {
        this.setX( x );
        this.setY( y );
    }

    public Point ( Point p ) {
        this.setX( p.getX() );
        this.setY( p.getY() );
    }

    public Point set ( int x, int y ) {
        this.setX( x );
        this.setY( y );
        return this;
    }

    @SuppressWarnings ( "MethodDoesntCallSuperMethod" )
    @Override
    public Point clone () {
        return new Point( this );
    }

    public Point offset ( Point d ) {
        setX( getX() + d.getX() );
        setY( getY() + d.getY() );
        return this;
    }

    @Override
    public boolean equals ( Object obj ) {
        if ( obj instanceof Point ) {
            Point p = (Point) obj;
            return p.getX() == getX() && p.getY() == getY();
        } else {
            return false;
        }
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
}
