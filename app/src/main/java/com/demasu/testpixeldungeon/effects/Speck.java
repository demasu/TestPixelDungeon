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
package com.demasu.testpixeldungeon.effects;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import com.demasu.testpixeldungeon.Assets;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Speck extends Image {

    public static final int HEALING = 0;
    public static final int STAR = 1;
    public static final int LIGHT = 2;
    public static final int QUESTION = 3;
    public static final int UP = 4;
    public static final int SCREAM = 5;
    public static final int BONE = 6;
    public static final int WOOL = 7;
    public static final int ROCK = 8;
    public static final int NOTE = 9;
    public static final int CHANGE = 10;
    public static final int HEART = 11;
    public static final int BUBBLE = 12;
    public static final int STEAM = 13;
    public static final int COIN = 14;

    public static final int DISCOVER = 101;
    public static final int EVOKE = 102;
    public static final int MASTERY = 103;
    public static final int KIT = 104;
    public static final int RATTLE = 105;
    public static final int JET = 106;
    public static final int TOXIC = 107;
    public static final int PARALYSIS = 108;
    public static final int DUST = 109;
    public static final int FORGE = 110;
    public static final int CONFUSION = 111;

    private static final int SIZE = 7;
    private static TextureFilm film;
    private static SparseArray<Emitter.Factory> factories = new SparseArray<Emitter.Factory>();
    private int type;
    private float lifespan;
    private float left;

    public Speck () {
        super();

        texture( Assets.SPECKS );
        if ( film == null ) {
            film = new TextureFilm( getTexture(), SIZE, SIZE );
        }

        getOrigin().set( SIZE / 2f );
    }

    public static Emitter.Factory factory ( final int type ) {
        return factory( type, false );
    }

    public static Emitter.Factory factory ( final int type, final boolean lightMode ) {

        Emitter.Factory factory = factories.get( type );

        if ( factory == null ) {
            factory = new Emitter.Factory() {
                @Override
                public void emit ( Emitter emitter, int index, float x, float y ) {
                    Speck p = (Speck) emitter.recycle( Speck.class );
                    p.reset( index, x, y, type );
                }

                @Override
                public boolean lightMode () {
                    return lightMode;
                }
            };
            factories.put( type, factory );
        }

        return factory;
    }

    public void reset ( int index, float x, float y, int type ) {
        revive();

        this.type = type;
        switch ( type ) {
            case DISCOVER:
                frame( film.get( LIGHT ) );
                break;
            case EVOKE:
            case MASTERY:
            case KIT:
            case FORGE:
                frame( film.get( STAR ) );
                break;
            case RATTLE:
                frame( film.get( BONE ) );
                break;
            case JET:
            case TOXIC:
            case PARALYSIS:
            case CONFUSION:
            case DUST:
                frame( film.get( STEAM ) );
                break;
            default:
                frame( film.get( type ) );
        }

        this.setX( x - getOrigin().x );
        this.setY( y - getOrigin().y );

        resetColor();
        getScale().set( 1 );
        getSpeed().set( 0 );
        getAcc().set( 0 );
        setAngle( 0 );
        setAngularSpeed( 0 );

        switch ( type ) {

            case HEALING:
                getSpeed().set( 0, -20 );
                lifespan = 1f;
                break;

            case STAR:
                getSpeed().polar( Random.Float( 2 * 3.1415926f ), Random.Float( 128 ) );
                getAcc().set( 0, 128 );
                setAngle( Random.Float( 360 ) );
                setAngularSpeed( Random.Float( -360, +360 ) );
                lifespan = 1f;
                break;

            case FORGE:
                getSpeed().polar( -Random.Float( 3.1415926f ), Random.Float( 64 ) );
                getAcc().set( 0, 128 );
                setAngle( Random.Float( 360 ) );
                setAngularSpeed( Random.Float( -360, +360 ) );
                lifespan = 0.51f;
                break;

            case EVOKE:
                getSpeed().polar( -Random.Float( 3.1415926f ), 50 );
                getAcc().set( 0, 50 );
                setAngle( Random.Float( 360 ) );
                setAngularSpeed( Random.Float( -180, +180 ) );
                lifespan = 1f;
                break;

            case KIT:
                getSpeed().polar( index * 3.1415926f / 5, 50 );
                getAcc().set( -getSpeed().x, -getSpeed().y );
                setAngle( index * 36 );
                setAngularSpeed( 360 );
                lifespan = 1f;
                break;

            case MASTERY:
                getSpeed().set( Random.Int( 2 ) == 0 ? Random.Float( -128, -64 ) : Random.Float( +64, +128 ), 0 );
                setAngularSpeed( getSpeed().x < 0 ? -180 : +180 );
                getAcc().set( -getSpeed().x, 0 );
                lifespan = 0.5f;
                break;

            case LIGHT:
                setAngle( Random.Float( 360 ) );
                setAngularSpeed( 90 );
                lifespan = 1f;
                break;

            case DISCOVER:
                setAngle( Random.Float( 360 ) );
                setAngularSpeed( 90 );
                lifespan = 0.5f;
                setAm( 0 );
                break;

            case QUESTION:
                lifespan = 0.8f;
                break;

            case UP:
                getSpeed().set( 0, -20 );
                lifespan = 1f;
                break;

            case SCREAM:
                lifespan = 0.9f;
                break;

            case BONE:
                lifespan = 0.2f;
                getSpeed().polar( Random.Float( 2 * 3.1415926f ), 24 / lifespan );
                getAcc().set( 0, 128 );
                setAngle( Random.Float( 360 ) );
                setAngularSpeed( 360 );
                break;

            case RATTLE:
                lifespan = 0.5f;
                getSpeed().set( 0, -200 );
                getAcc().set( 0, -2 * getSpeed().y / lifespan );
                setAngle( Random.Float( 360 ) );
                setAngularSpeed( 360 );
                break;

            case WOOL:
                lifespan = 0.5f;
                getSpeed().set( 0, -50 );
                setAngle( Random.Float( 360 ) );
                setAngularSpeed( Random.Float( -360, +360 ) );
                break;

            case ROCK:
                setAngle( Random.Float( 360 ) );
                setAngularSpeed( Random.Float( -360, +360 ) );
                getScale().set( Random.Float( 1, 2 ) );
                getSpeed().set( 0, 64 );
                lifespan = 0.2f;
                y -= getSpeed().y * lifespan;
                break;

            case NOTE:
                setAngularSpeed( Random.Float( -30, +30 ) );
                getSpeed().polar( ( getAngularSpeed() - 90 ) * PointF.G2R, 30 );
                lifespan = 1f;
                break;

            case CHANGE:
                setAngle( Random.Float( 360 ) );
                getSpeed().polar( ( getAngle() - 90 ) * PointF.G2R, Random.Float( 4, 12 ) );
                lifespan = 1.5f;
                break;

            case HEART:
                getSpeed().set( Random.Int( -10, +10 ), -40 );
                setAngularSpeed( Random.Float( -45, +45 ) );
                lifespan = 1f;
                break;

            case BUBBLE:
                getSpeed().set( 0, -15 );
                getScale().set( Random.Float( 0.8f, 1 ) );
                lifespan = Random.Float( 0.8f, 1.5f );
                break;

            case STEAM:
                getSpeed().y = -Random.Float( 20, 30 );
                setAngularSpeed( Random.Float( +180 ) );
                setAngle( Random.Float( 360 ) );
                lifespan = 1f;
                break;

            case JET:
                getSpeed().y = +32;
                getAcc().y = -64;
                setAngularSpeed( Random.Float( 180, 360 ) );
                setAngle( Random.Float( 360 ) );
                lifespan = 0.5f;
                break;

            case TOXIC:
                hardlight( 0x50FF60 );
                setAngularSpeed( 30 );
                setAngle( Random.Float( 360 ) );
                lifespan = Random.Float( 1f, 3f );
                break;

            case PARALYSIS:
                hardlight( 0xFFFF66 );
                setAngularSpeed( -30 );
                setAngle( Random.Float( 360 ) );
                lifespan = Random.Float( 1f, 3f );
                break;

            case CONFUSION:
                hardlight( Random.Int( 0x1000000 ) | 0x000080 );
                setAngularSpeed( Random.Float( -20, +20 ) );
                setAngle( Random.Float( 360 ) );
                lifespan = Random.Float( 1f, 3f );
                break;

            case DUST:
                hardlight( 0xFFFF66 );
                setAngle( Random.Float( 360 ) );
                getSpeed().polar( Random.Float( 2 * 3.1415926f ), Random.Float( 16, 48 ) );
                lifespan = 0.5f;
                break;

            case COIN:
                getSpeed().polar( -PointF.PI * Random.Float( 0.3f, 0.7f ), Random.Float( 48, 96 ) );
                getAcc().y = 256;
                lifespan = -getSpeed().y / getAcc().y * 2;
                break;
        }

        left = lifespan;
    }

    @SuppressLint ( "FloatMath" )
    @Override
    public void update () {
        super.update();

        left -= Game.getElapsed();
        if ( left <= 0 ) {

            kill();

        } else {

            float p = 1 - left / lifespan;    // 0 -> 1

            switch ( type ) {

                case STAR:
                case FORGE:
                    getScale().set( 1 - p );
                    setAm( p < 0.2f ? p * 5f : ( 1 - p ) * 1.25f );
                    break;

                case KIT:
                case MASTERY:
                    setAm( 1 - p * p );
                    break;

                case EVOKE:

                case HEALING:
                    setAm( p < 0.5f ? 1 : 2 - p * 2 );
                    break;

                case LIGHT:
                    setAm( getScale().set( p < 0.2f ? p * 5f : ( 1 - p ) * 1.25f ).x );
                    break;

                case DISCOVER:
                    setAm( 1 - p );
                    getScale().set( ( p < 0.5f ? p : 1 - p ) * 2 );
                    break;

                case QUESTION:
                    getScale().set( (float) ( Math.sqrt( p < 0.5f ? p : 1 - p ) * 3 ) );
                    break;

                case UP:
                    getScale().set( (float) ( Math.sqrt( p < 0.5f ? p : 1 - p ) * 2 ) );
                    break;

                case SCREAM:
                    setAm( (float) Math.sqrt( ( p < 0.5f ? p : 1 - p ) * 2f ) );
                    getScale().set( p * 7 );
                    break;

                case BONE:
                case RATTLE:
                    setAm( p < 0.9f ? 1 : ( 1 - p ) * 10 );
                    break;

                case ROCK:
                    setAm( p < 0.2f ? p * 5 : 1 );
                    break;

                case NOTE:
                    setAm( 1 - p * p );
                    break;

                case WOOL:
                    getScale().set( 1 - p );
                    break;

                case CHANGE:
                    setAm( (float) Math.sqrt( ( p < 0.5f ? p : 1 - p ) * 2 ) );
                    getScale().y = ( 1 + p ) * 0.5f;
                    getScale().x = getScale().y * (float) Math.cos( left * 15 );
                    break;

                case HEART:
                    getScale().set( 1 - p );
                    setAm( 1 - p * p );
                    break;

                case BUBBLE:
                    setAm( p < 0.2f ? p * 5 : 1 );
                    break;

                case STEAM:
                case TOXIC:
                case PARALYSIS:
                case CONFUSION:
                case DUST:
                    setAm( p < 0.5f ? p : 1 - p );
                    getScale().set( 1 + p * 2 );
                    break;

                case JET:
                    setAm( ( p < 0.5f ? p : 1 - p ) * 2 );
                    getScale().set( p * 1.5f );
                    break;

                case COIN:
                    getScale().x = (float) Math.cos( left * 5 );
                    float finVal = ( Math.abs( getScale().x ) + 1 ) * 0.5f;
                    setRm( finVal );
                    setGm( finVal );
                    setBm( finVal );
                    setAm( p < 0.9f ? 1 : ( 1 - p ) * 10 );
                    break;
            }
        }
    }
}
