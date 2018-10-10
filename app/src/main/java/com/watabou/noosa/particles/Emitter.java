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

package com.watabou.noosa.particles;

import android.opengl.GLES20;

import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Visual;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import javax.microedition.khronos.opengles.GL10;

public class Emitter extends Group {

    private float x;
    private float y;
    private float width;
    private float height;
    private boolean on = false;
    private boolean autoKill = true;
    private boolean lightMode = false;
    private Visual target;
    private float interval;
    private int quantity;
    private int count;
    private float time;

    private Factory factory;

    public void pos ( float x, float y ) {
        pos( x, y, 0, 0 );
    }

    public void pos ( PointF p ) {
        pos( p.getX(), p.getY(), 0, 0 );
    }

    public void pos ( float x, float y, float width, float height ) {
        this.setX( x );
        this.setY( y );
        this.setWidth( width );
        this.setHeight( height );
    }

    public void pos ( Visual target ) {
        this.setTarget( target );
    }

    public void burst ( Factory factory, int quantity ) {
        start( factory, 0, quantity );
    }

    public void pour ( Factory factory, float interval ) {
        start( factory, interval, 0 );
    }

    public void start ( Factory factory, float interval, int quantity ) {

        this.setFactory( factory );
        this.lightMode = factory.lightMode();

        this.interval = interval;
        this.quantity = quantity;

        count = 0;
        time = Random.Float( interval );

        setOn( true );
    }

    @Override
    public void update () {

        if ( isOn() ) {
            time += Game.getElapsed();
            while ( time > interval ) {
                time -= interval;
                emit( count );
                count++;
                if ( quantity > 0 && count >= quantity ) {
                    setOn( false );
                    break;
                }
            }
        } else if ( isAutoKill() && countLiving() == 0 ) {
            kill();
        }

        super.update();
    }

    @SuppressWarnings ( "FeatureEnvy" )
    protected void emit ( int index ) {
        if ( getTarget() == null ) {
            getFactory().emit(
                    this,
                    index,
                    getX() + Random.Float( getWidth() ),
                    getY() + Random.Float( getHeight() ) );
        } else {
            getFactory().emit(
                    this,
                    index,
                    getTarget().getX() + Random.Float( getTarget().getWidth() ),
                    getTarget().getY() + Random.Float( getTarget().getHeight() ) );
        }
    }

    @Override
    public void draw () {
        if ( lightMode ) {
            GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
            super.draw();
            GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
        } else {
            super.draw();
        }
    }

    public Visual getTarget () {
        return target;
    }

    public void setTarget ( Visual target ) {
        this.target = target;
    }

    public Factory getFactory () {
        return factory;
    }

    private void setFactory ( Factory factory ) {
        this.factory = factory;
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

    private float getWidth () {
        return width;
    }

    public void setWidth ( float width ) {
        this.width = width;
    }

    private float getHeight () {
        return height;
    }

    public void setHeight ( float height ) {
        this.height = height;
    }

    public boolean isOn () {
        return on;
    }

    public void setOn ( boolean on ) {
        this.on = on;
    }

    private boolean isAutoKill () {
        return autoKill;
    }

    public void setAutoKill ( boolean autoKill ) {
        this.autoKill = autoKill;
    }

    // TODO: Move this to its own file
    @SuppressWarnings ( "PublicInnerClass" )
    abstract public static class Factory {

        abstract public void emit ( Emitter emitter, int index, float x, float y );

        public boolean lightMode () {
            return false;
        }
    }
}
