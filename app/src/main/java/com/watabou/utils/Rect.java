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

public class Rect {

    private int left;
    private int top;
    private int right;
    private int bottom;

    public Rect () {
        this( 0, 0, 0, 0 );
    }

    public Rect ( Rect rect ) {
        this( rect.getLeft(), rect.getTop(), rect.getRight(), rect.getBottom() );
    }

    public Rect ( int left, int top, int right, int bottom ) {
        this.setLeft( left );
        this.setTop( top );
        this.setRight( right );
        this.setBottom( bottom );
    }

    public int width () {
        return getRight() - getLeft();
    }

    public int height () {
        return getBottom() - getTop();
    }

    public int square () {
        return ( getRight() - getLeft() ) * ( getBottom() - getTop() );
    }

    public Rect set ( int left, int top, int right, int bottom ) {
        this.setLeft( left );
        this.setTop( top );
        this.setRight( right );
        this.setBottom( bottom );
        return this;
    }

    public Rect set ( Rect rect ) {
        return set( rect.getLeft(), rect.getTop(), rect.getRight(), rect.getBottom() );
    }

    public boolean isEmpty () {
        return getRight() <= getLeft() || getBottom() <= getTop();
    }

    public Rect setEmpty () {
        setLeft( 0 );
        setRight( 0 );
        setTop( 0 );
        setBottom( 0 );
        return this;
    }

    public Rect intersect ( Rect other ) {
        Rect result = new Rect();
        result.setLeft( Math.max( getLeft(), other.getLeft() ) );
        result.setRight( Math.min( getRight(), other.getRight() ) );
        result.setTop( Math.max( getTop(), other.getTop() ) );
        result.setBottom( Math.min( getBottom(), other.getBottom() ) );
        return result;
    }

    public Rect union ( int x, int y ) {
        if ( isEmpty() ) {
            return set( x, y, x + 1, y + 1 );
        } else {
            if ( x < getLeft() ) {
                setLeft( x );
            } else if ( x >= getRight() ) {
                setRight( x + 1 );
            }
            if ( y < getTop() ) {
                setTop( y );
            } else if ( y >= getBottom() ) {
                setBottom( y + 1 );
            }
            return this;
        }
    }

    public int getLeft () {
        return left;
    }

    public void setLeft ( int left ) {
        this.left = left;
    }

    public int getTop () {
        return top;
    }

    public void setTop ( int top ) {
        this.top = top;
    }

    public int getRight () {
        return right;
    }

    public void setRight ( int right ) {
        this.right = right;
    }

    public int getBottom () {
        return bottom;
    }

    public void setBottom ( int bottom ) {
        this.bottom = bottom;
    }
}
