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
package com.demasu.testpixeldungeon.ui;


import com.demasu.testpixeldungeon.Chrome;
import com.demasu.testpixeldungeon.effects.ShadowBox;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.watabou.input.Keys;
import com.watabou.input.Keys.Key;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.TouchArea;
import com.watabou.utils.Signal;

public class Window extends Group implements Signal.Listener<Key> {

    public static final int TITLE_COLOR = 0xFFFF44;
    protected int width;
    protected int height;
    protected TouchArea blocker;
    protected ShadowBox shadow;
    protected NinePatch chrome;

    public Window () {
        this( 0, 0, Chrome.get( Chrome.Type.WINDOW ) );
    }

    public Window ( int width, int height ) {
        this( width, height, Chrome.get( Chrome.Type.WINDOW ) );
    }

    public Window ( int width, int height, NinePatch chrome ) {
        super();

        blocker = new TouchArea( 0, 0, PixelScene.uiCamera.getWidth(), PixelScene.uiCamera.getHeight() ) {
            @Override
            protected void onClick ( Touch touch ) {
                if ( !Window.this.chrome.overlapsScreenPoint(
                        (int) touch.getCurrent().getX(),
                        (int) touch.getCurrent().getY() ) ) {

                    onBackPressed();
                }
            }
        };
        blocker.setCamera( PixelScene.uiCamera );
        add( blocker );

        this.chrome = chrome;

        this.width = width;
        this.height = height;

        shadow = new ShadowBox();
        shadow.setAm( 0.5f );
        shadow.setCamera( PixelScene.uiCamera.getVisible() ?
                PixelScene.uiCamera : Camera.getMain() );
        add( shadow );

        chrome.setX( -chrome.marginLeft() );
        chrome.setY( -chrome.marginTop() );
        chrome.size(
                width - chrome.getX() + chrome.marginRight(),
                height - chrome.getY() + chrome.marginBottom() );
        add( chrome );

        setCamera( new Camera( 0, 0,
                (int) chrome.getWidth(),
                (int) chrome.getHeight(),
                PixelScene.defaultZoom ) );
        getCamera().setX( (int) ( Game.getWidth() - getCamera().getWidth() * getCamera().getZoom() ) / 2 );
        getCamera().setY( (int) ( Game.getHeight() - getCamera().getHeight() * getCamera().getZoom() ) / 2 );
        getCamera().getScroll().set( chrome.getX(), chrome.getY() );
        Camera.add( getCamera() );

        shadow.boxRect(
                getCamera().getX() / getCamera().getZoom(),
                getCamera().getY() / getCamera().getZoom(),
                chrome.width(), chrome.getHeight() );

        Keys.event.add( this );
    }

    public void resize ( int w, int h ) {
        this.width = w;
        this.height = h;

        chrome.size(
                width + chrome.marginHor(),
                height + chrome.marginVer() );

        getCamera().resize( (int) chrome.getWidth(), (int) chrome.getHeight() );
        getCamera().setX( (int) ( Game.getWidth() - getCamera().screenWidth() ) / 2 );
        getCamera().setY( (int) ( Game.getHeight() - getCamera().screenHeight() ) / 2 );

        shadow.boxRect( getCamera().getX() / getCamera().getZoom(), getCamera().getY() / getCamera().getZoom(), chrome.width(), chrome.getHeight() );
    }

    public void hide () {
        getParent().erase( this );
        destroy();
    }

    @Override
    public void destroy () {
        super.destroy();

        Camera.remove( getCamera() );
        Keys.event.remove( this );
    }

    @Override
    public void onSignal ( Key key ) {
        if ( key.pressed ) {
            switch ( key.code ) {
                case Keys.BACK:
                    onBackPressed();
                    break;
                case Keys.MENU:
                    onMenuPressed();
                    break;
            }
        }

        Keys.event.cancel();
    }

    public void onBackPressed () {
        hide();
    }

    public void onMenuPressed () {
    }
}
