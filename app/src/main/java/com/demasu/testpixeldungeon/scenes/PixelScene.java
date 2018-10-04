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
package com.demasu.testpixeldungeon.scenes;

import android.opengl.GLES20;

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Badges;
import com.demasu.testpixeldungeon.PixelDungeon;
import com.demasu.testpixeldungeon.effects.BadgeBanner;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapText.Font;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Scene;
import com.watabou.noosa.Visual;
import com.watabou.utils.BitmapCache;

import javax.microedition.khronos.opengles.GL10;

public class PixelScene extends Scene {

    // Minimum virtual display size for portrait orientation
    public static final float MIN_WIDTH_P = 128;
    public static final float MIN_HEIGHT_P = 224;

    // Minimum virtual display size for landscape orientation
    public static final float MIN_WIDTH_L = 224;
    public static final float MIN_HEIGHT_L = 160;

    public static float defaultZoom = 0;
    public static float minZoom;
    public static float maxZoom;

    public static Camera uiCamera;

    public static BitmapText.Font font1x;
    public static BitmapText.Font font15x;
    public static BitmapText.Font font2x;
    public static BitmapText.Font font25x;
    public static BitmapText.Font font3x;
    public static BitmapText.Font font;
    public static float scale;
    public static boolean noFade = false;

    public static void chooseFont ( float size ) {
        chooseFont( size, defaultZoom );
    }

    public static void chooseFont ( float size, float zoom ) {

        float pt = size * zoom;

        if ( pt >= 19 ) {

            scale = pt / 19;
            if ( 1.5 <= scale && scale < 2 ) {
                font = font25x;
                scale = (int) ( pt / 14 );
            } else {
                font = font3x;
                scale = (int) scale;
            }

        } else if ( pt >= 14 ) {

            scale = pt / 14;
            if ( 1.8 <= scale && scale < 2 ) {
                font = font2x;
                scale = (int) ( pt / 12 );
            } else {
                font = font25x;
                scale = (int) scale;
            }

        } else if ( pt >= 12 ) {

            scale = pt / 12;
            if ( 1.7 <= scale && scale < 2 ) {
                font = font15x;
                scale = (int) ( pt / 10 );
            } else {
                font = font2x;
                scale = (int) scale;
            }

        } else if ( pt >= 10 ) {

            scale = pt / 10;
            if ( 1.4 <= scale && scale < 2 ) {
                font = font1x;
                scale = (int) ( pt / 7 );
            } else {
                font = font15x;
                scale = (int) scale;
            }

        } else {

            font = font1x;
            scale = Math.max( 1, (int) ( pt / 7 ) );

        }

        scale /= zoom;
    }

    public static BitmapText createText ( float size ) {
        return createText( null, size );
    }

    public static BitmapText createText ( String text, float size ) {

        chooseFont( size );

        BitmapText result = new BitmapText( text, font );
        result.scale.set( scale );

        return result;
    }

    public static BitmapTextMultiline createMultiline ( float size ) {
        return createMultiline( null, size );
    }

    public static BitmapTextMultiline createMultiline ( String text, float size ) {

        chooseFont( size );

        BitmapTextMultiline result = new BitmapTextMultiline( text, font );
        result.scale.set( scale );

        return result;
    }

    public static float align ( Camera camera, float pos ) {
        return ( (int) ( pos * camera.getZoom() ) ) / camera.getZoom();
    }

    // This one should be used for UI elements
    public static float align ( float pos ) {
        return ( (int) ( pos * defaultZoom ) ) / defaultZoom;
    }

    public static void align ( Visual v ) {
        Camera c = v.camera();
        v.x = align( c, v.x );
        v.y = align( c, v.y );
    }

    public static void showBadge ( Badges.Badge badge ) {
        BadgeBanner banner = BadgeBanner.show( badge.image );
        banner.setCamera( uiCamera );
        banner.x = align( banner.getCamera(), ( banner.getCamera().getWidth() - banner.width ) / 2 );
        banner.y = align( banner.getCamera(), ( banner.getCamera().getHeight() - banner.height ) / 3 );
        Game.scene().add( banner );
    }

    @Override
    public void create () {

        super.create();

        GameScene.scene = null;

        float minWidth, minHeight;
        if ( PixelDungeon.landscape() ) {
            minWidth = MIN_WIDTH_L;
            minHeight = MIN_HEIGHT_L;
        } else {
            minWidth = MIN_WIDTH_P;
            minHeight = MIN_HEIGHT_P;
        }

        defaultZoom = (int) Math.ceil( Game.getDensity() * 2.5 );
        while ( (
                Game.getWidth() / defaultZoom < minWidth ||
                        Game.getHeight() / defaultZoom < minHeight
        ) && defaultZoom > 1 ) {

            defaultZoom--;
        }

        if ( PixelDungeon.scaleUp() ) {
            while (
                    Game.getWidth() / ( defaultZoom + 1 ) >= minWidth &&
                            Game.getHeight() / ( defaultZoom + 1 ) >= minHeight ) {

                defaultZoom++;
            }
        }
        minZoom = 1;
        maxZoom = defaultZoom * 2;

        Camera.reset( new PixelCamera( defaultZoom ) );

        float uiZoom = defaultZoom;
        uiCamera = Camera.createFullscreen( uiZoom );
        Camera.add( uiCamera );

        if ( font1x == null ) {

            // 3x5 (6)
            font1x = Font.colorMarked(
                    BitmapCache.get( Assets.FONTS1X ), 0x00000000, BitmapText.Font.LATIN_FULL );
            font1x.setBaseLine( 6 );
            font1x.setTracking( -1 );

            // 5x8 (10)
            font15x = Font.colorMarked(
                    BitmapCache.get( Assets.FONTS15X ), 12, 0x00000000, BitmapText.Font.LATIN_FULL );
            font15x.setBaseLine( 9 );
            font15x.setTracking( -1 );

            // 6x10 (12)
            font2x = Font.colorMarked(
                    BitmapCache.get( Assets.FONTS2X ), 14, 0x00000000, BitmapText.Font.LATIN_FULL );
            font2x.setBaseLine( 11 );
            font2x.setTracking( -1 );

            // 7x12 (15)
            font25x = Font.colorMarked(
                    BitmapCache.get( Assets.FONTS25X ), 17, 0x00000000, BitmapText.Font.LATIN_FULL );
            font25x.setBaseLine( 13 );
            font25x.setTracking( -1 );

            // 9x15 (18)
            font3x = Font.colorMarked(
                    BitmapCache.get( Assets.FONTS3X ), 22, 0x00000000, BitmapText.Font.LATIN_FULL );
            font3x.setBaseLine( 17 );
            font3x.setTracking( -2 );
        }
    }

    @Override
    public void destroy () {
        super.destroy();
        Touchscreen.getEvent().removeAll();
    }

    protected void fadeIn () {
        if ( noFade ) {
            noFade = false;
        } else {
            fadeIn( 0xFF000000, false );
        }
    }

    protected void fadeIn ( int color, boolean light ) {
        add( new Fader( color, light ) );
    }

    protected static class Fader extends ColorBlock {

        private static float FADE_TIME = 1f;

        private boolean light;

        private float time;

        public Fader ( int color, boolean light ) {
            super( uiCamera.getWidth(), uiCamera.getHeight(), color );

            this.light = light;

            setCamera( uiCamera );

            alpha( 1f );
            time = FADE_TIME;
        }

        @Override
        public void update () {

            super.update();

            if ( ( time -= Game.getElapsed() ) <= 0 ) {
                alpha( 0f );
                getParent().remove( this );
            } else {
                alpha( time / FADE_TIME );
            }
        }

        @Override
        public void draw () {
            if ( light ) {
                GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
                super.draw();
                GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
            } else {
                super.draw();
            }
        }
    }

    private static class PixelCamera extends Camera {

        public PixelCamera ( float zoom ) {
            super(
                    (int) ( Game.getWidth() - Math.ceil( Game.getWidth() / zoom ) * zoom ) / 2,
                    (int) ( Game.getHeight() - Math.ceil( Game.getHeight() / zoom ) * zoom ) / 2,
                    (int) Math.ceil( Game.getWidth() / zoom ),
                    (int) Math.ceil( Game.getHeight() / zoom ), zoom );
        }

        @Override
        protected void updateMatrix () {
            float sx = align( this, getScroll().x + getShakeX() );
            float sy = align( this, getScroll().y + getShakeY() );

            getMatrix()[0] = +getZoom() * getInvW2();
            getMatrix()[5] = -getZoom() * getInvH2();

            getMatrix()[12] = -1 + getX() * getInvW2() - sx * getMatrix()[0];
            getMatrix()[13] = +1 - getY() * getInvH2() - sy * getMatrix()[5];

        }
    }
}
