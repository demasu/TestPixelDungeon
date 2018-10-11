/*
 * Pixel Dungeon
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
package com.demasu.testpixeldungeon.actors.blobs;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.PixelDungeon;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.effects.BlobEmitter;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.utils.BArray;
import com.watabou.utils.Bundle;

import java.util.Arrays;

public class Blob extends Actor {

    public static final int WIDTH = Level.WIDTH;
    private static final int HEIGHT = Level.HEIGHT;
    public static final int LENGTH = Level.LENGTH;
    private static final String CUR = "cur";
    private static final String START = "start";
    private int volume;
    private int[] cur;
    private BlobEmitter emitter;
    private int[] off;
    protected Blob () {

        setCur( new int[LENGTH] );
        setOff( new int[LENGTH] );

        setVolume( 0 );
    }

    @SuppressWarnings ( "unchecked" )
    public static <T extends Blob> T seed ( int cell, int amount, Class<T> type ) {
        try {

            T gas = (T) Dungeon.getLevel().blobs.get( type );
            if ( gas == null ) {
                gas = type.newInstance();
                Dungeon.getLevel().blobs.put( type, gas );
            }

            gas.seed( cell, amount );

            return gas;

        } catch ( Exception e ) {
            PixelDungeon.reportException( e );
            return null;
        }
    }

    @Override
    public void storeInBundle ( Bundle bundle ) {
        super.storeInBundle( bundle );

        if ( getVolume() > 0 ) {

            int start;
            for ( start = 0; start < LENGTH; start++ ) {
                if ( getCur()[start] > 0 ) {
                    break;
                }
            }
            int end;
            for ( end = LENGTH - 1; end > start; end-- ) {
                if ( getCur()[end] > 0 ) {
                    break;
                }
            }

            bundle.put( START, start );
            bundle.put( CUR, trim( start, end + 1 ) );

        }
    }

    private int[] trim ( int start, int end ) {
        int len = end - start;
        int[] copy = new int[len];
        System.arraycopy( getCur(), start, copy, 0, len );
        return copy;
    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {

        super.restoreFromBundle( bundle );

        int[] data = bundle.getIntArray( CUR );
        if ( data != null ) {
            int start = bundle.getInt( START );
            for ( int i = 0; i < data.length; i++ ) {
                getCur()[i + start] = data[i];
                setVolume( getVolume() + data[i] );
            }
        }

        if ( Level.resizingNeeded ) {
            int[] cur = new int[Level.LENGTH];
            Arrays.fill( cur, 0 );

            int loadedMapSize = Level.loadedMapSize;
            for ( int i = 0; i < loadedMapSize; i++ ) {
                System.arraycopy( this.getCur(), i * loadedMapSize, cur, i * Level.WIDTH, loadedMapSize );
            }

            this.setCur( cur );
        }
    }

    @Override
    public boolean act () {

        spend( TICK );

        if ( getVolume() > 0 ) {

            setVolume( 0 );
            evolve();

            int[] tmp = getOff();
            setOff( getCur() );
            setCur( tmp );

        }

        return true;
    }

    public void use ( BlobEmitter emitter ) {
        this.setEmitter( emitter );
    }

    protected void evolve () {

        boolean[] notBlocking = BArray.not( Level.solid, null );

        for ( int i = 1; i < HEIGHT - 1; i++ ) {

            int from = i * WIDTH + 1;
            int to = from + WIDTH - 2;

            for ( int pos = from; pos < to; pos++ ) {
                if ( notBlocking[pos] ) {

                    int count = 1;
                    int sum = getCur()[pos];

                    if ( notBlocking[pos - 1] ) {
                        sum += getCur()[pos - 1];
                        count++;
                    }
                    if ( notBlocking[pos + 1] ) {
                        sum += getCur()[pos + 1];
                        count++;
                    }
                    if ( notBlocking[pos - WIDTH] ) {
                        sum += getCur()[pos - WIDTH];
                        count++;
                    }
                    if ( notBlocking[pos + WIDTH] ) {
                        sum += getCur()[pos + WIDTH];
                        count++;
                    }

                    int value = sum >= count ? ( sum / count ) - 1 : 0;
                    getOff()[pos] = value;

                    setVolume( getVolume() + value );
                } else {
                    getOff()[pos] = 0;
                }
            }
        }
    }

    public void seed ( int cell, int amount ) {
        getCur()[cell] += amount;
        setVolume( getVolume() + amount );
    }

    public void clear ( int cell ) {
        setVolume( getVolume() - getCur()[cell] );
        getCur()[cell] = 0;
    }

    public String tileDesc () {
        return null;
    }

    public int getVolume () {
        return volume;
    }

    public void setVolume ( int volume ) {
        this.volume = volume;
    }

    @SuppressWarnings ( "AssignmentOrReturnOfFieldWithMutableType" )
    public int[] getCur () {
        return cur;
    }

    private void setCur ( int[] cur ) {
        this.cur = cur;
    }

    public BlobEmitter getEmitter () {
        return emitter;
    }

    public void setEmitter ( BlobEmitter emitter ) {
        this.emitter = emitter;
    }

    @SuppressWarnings ( "AssignmentOrReturnOfFieldWithMutableType" )
    public int[] getOff () {
        return off;
    }

    private void setOff ( int[] off ) {
        this.off = off;
    }
}
