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

    public float x;
    public float y;
    public float width;
    public float height;
    public boolean on = false;
    public boolean autoKill = true;
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
        pos( p.x, p.y, 0, 0 );
    }

    public void pos ( float x, float y, float width, float height ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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

        on = true;
    }

    @Override
    public void update () {

        if ( on ) {
            time += Game.getElapsed();
            while ( time > interval ) {
                time -= interval;
                emit( count );
                count++;
                if ( quantity > 0 && count >= quantity ) {
                    on = false;
                    break;
                }
            }
        } else if ( autoKill && countLiving() == 0 ) {
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
                    x + Random.Float( width ),
                    y + Random.Float( height ) );
        } else {
            getFactory().emit(
                    this,
                    index,
                    getTarget().x + Random.Float( getTarget().width ),
                    getTarget().y + Random.Float( getTarget().height ) );
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

    abstract public static class Factory {

        abstract public void emit ( Emitter emitter, int index, float x, float y );

        public boolean lightMode () {
            return false;
        }
    }
}
