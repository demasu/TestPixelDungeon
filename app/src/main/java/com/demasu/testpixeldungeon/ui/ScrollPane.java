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

import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

public class ScrollPane extends Component {

    protected static final int THUMB_COLOR = 0xFF7b8073;
    protected static final float THUMB_ALPHA = 0.5f;

    protected TouchController controller;
    protected Component content;
    protected ColorBlock thumb;

    protected float minX;
    protected float minY;
    protected float maxX;
    protected float maxY;

    public ScrollPane ( Component content ) {
        super();

        this.content = content;
        addToBack( content );

        setWidth( content.width() );
        setHeight( content.height() );

        content.camera = new Camera( 0, 0, 1, 1, PixelScene.defaultZoom );
        Camera.add( content.camera );
    }

    @Override
    public void destroy () {
        super.destroy();
        Camera.remove( content.camera );
    }

    public void scrollTo ( float x, float y ) {
        content.camera.getScroll().set( x, y );
    }

    @Override
    protected void createChildren () {
        controller = new TouchController();
        add( controller );

        thumb = new ColorBlock( 1, 1, THUMB_COLOR );
        thumb.am = THUMB_ALPHA;
        add( thumb );
    }

    @Override
    protected void layout () {

        content.setPos( 0, 0 );
        controller.x = getX();
        controller.y = getY();
        controller.width = getWidth();
        controller.height = getHeight();

        Point p = camera().cameraToScreen( getX(), getY() );
        Camera cs = content.camera;
        cs.setX( p.x );
        cs.setY( p.y );
        cs.resize( (int) getWidth(), (int) getHeight() );

        thumb.setVisible( getHeight() < content.height() );
        if ( thumb.getVisible() ) {
            thumb.scale.set( 2, getHeight() * getHeight() / content.height() );
            thumb.x = right() - thumb.width();
            thumb.y = getY();
        }
    }

    public Component content () {
        return content;
    }

    public void onClick ( float x, float y ) {
    }

    public class TouchController extends TouchArea {

        private float dragThreshold;
        private boolean dragging = false;
        private PointF lastPos = new PointF();

        public TouchController () {
            super( 0, 0, 0, 0 );
            dragThreshold = PixelScene.defaultZoom * 8;
        }

        @Override
        protected void onClick ( Touch touch ) {
            if ( dragging ) {

                dragging = false;
                thumb.am = THUMB_ALPHA;

            } else {

                PointF p = content.camera.screenToCamera( (int) touch.getCurrent().x, (int) touch.getCurrent().y );
                ScrollPane.this.onClick( p.x, p.y );

            }
        }

        @Override
        protected void onDrag ( Touch t ) {
            if ( dragging ) {

                Camera c = content.camera;

                c.getScroll().offset( PointF.diff( lastPos, t.getCurrent() ).invScale( c.getZoom() ) );
                if ( c.getScroll().x + width > content.width() ) {
                    c.getScroll().x = content.width() - width;
                }
                if ( c.getScroll().x < 0 ) {
                    c.getScroll().x = 0;
                }
                if ( c.getScroll().y + height > content.height() ) {
                    c.getScroll().y = content.height() - height;
                }
                if ( c.getScroll().y < 0 ) {
                    c.getScroll().y = 0;
                }

                thumb.y = y + height * c.getScroll().y / content.height();

                lastPos.set( t.getCurrent() );

            } else if ( PointF.distance( t.getCurrent(), t.getStart() ) > dragThreshold ) {

                dragging = true;
                lastPos.set( t.getCurrent() );
                thumb.am = 1;

            }
        }
    }
}
