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

package com.watabou.noosa.ui;

import com.watabou.noosa.Group;

public class Component extends Group {

    private float x;
    private float y;
    private float width;
    private float height;

    public Component () {
        super();
        createChildren();
    }

    public Component setPos ( float x, float y ) {
        this.setX( x );
        this.setY( y );
        layout();

        return this;
    }

    public Component setSize ( float width, float height ) {
        this.setWidth( width );
        this.setHeight( height );
        layout();

        return this;
    }

    public Component setRect ( float x, float y, float width, float height ) {
        this.setX( x );
        this.setY( y );
        this.setWidth( width );
        this.setHeight( height );
        layout();

        return this;
    }

    public boolean inside ( float x, float y ) {
        return x >= this.getX() && y >= this.getY() && x < this.getX() + getWidth() && y < this.getY() + getHeight();
    }

    public void fill ( Component c ) {
        setRect( c.getX(), c.getY(), c.getWidth(), c.getHeight() );
    }

    public float left () {
        return getX();
    }

    public float right () {
        return getX() + getWidth();
    }

    public float centerX () {
        return getX() + getWidth() / 2;
    }

    public float top () {
        return getY();
    }

    public float bottom () {
        return getY() + getHeight();
    }

    public float centerY () {
        return getY() + getHeight() / 2;
    }

    public float width () {
        return getWidth();
    }

    public float height () {
        return getHeight();
    }

    protected void createChildren () {
    }

    protected void layout () {
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

    public float getHeight () {
        return height;
    }

    public void setHeight ( float height ) {
        this.height = height;
    }

    public float getWidth () {
        return width;
    }

    public void setWidth ( float width ) {
        this.width = width;
    }
}
