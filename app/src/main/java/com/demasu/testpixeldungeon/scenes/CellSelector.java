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

import com.demasu.testpixeldungeon.DungeonTilemap;
import com.demasu.testpixeldungeon.PixelDungeon;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.TouchArea;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;

public class CellSelector extends TouchArea {

    public Listener listener = null;

    public boolean enabled;

    private float dragThreshold;
    private boolean pinching = false;
    private Touch another;
    private float startZoom;
    private float startSpan;
    private boolean dragging = false;
    private PointF lastPos = new PointF();
    public CellSelector ( DungeonTilemap map ) {
        super( map );
        camera = map.camera();

        dragThreshold = PixelScene.defaultZoom * DungeonTilemap.SIZE / 2;
    }

    @Override
    protected void onClick ( Touch touch ) {
        if ( dragging ) {

            dragging = false;

        } else {

            select( ( (DungeonTilemap) target ).screenToTile(
                    (int) touch.getCurrent().x,
                    (int) touch.getCurrent().y ) );
        }
    }

    public void select ( int cell ) {
        if ( enabled && listener != null && cell != -1 ) {

            listener.onSelect( cell );
            GameScene.ready();

        } else {

            GameScene.cancel();

        }
    }

    @Override
    protected void onTouchDown ( Touch t ) {

        if ( t != touch && another == null ) {

            if ( !touch.down ) {
                touch = t;
                onTouchDown( t );
                return;
            }

            pinching = true;

            another = t;
            startSpan = PointF.distance( touch.getCurrent(), another.getCurrent() );
            startZoom = camera.zoom;

            dragging = false;
        }
    }

    @Override
    protected void onTouchUp ( Touch t ) {
        if ( pinching && ( t == touch || t == another ) ) {

            pinching = false;

            int zoom = Math.round( camera.zoom );
            camera.zoom( zoom );
            PixelDungeon.zoom( (int) ( zoom - PixelScene.defaultZoom ) );

            dragging = true;
            if ( t == touch ) {
                touch = another;
            }
            another = null;
            lastPos.set( touch.getCurrent() );
        }
    }

    @Override
    protected void onDrag ( Touch t ) {

        camera.target = null;

        if ( pinching ) {

            float curSpan = PointF.distance( touch.getCurrent(), another.getCurrent() );
            camera.zoom( GameMath.gate(
                    PixelScene.minZoom,
                    startZoom * curSpan / startSpan,
                    PixelScene.maxZoom ) );

        } else {

            if ( !dragging && PointF.distance( t.getCurrent(), t.getStart() ) > dragThreshold ) {

                dragging = true;
                lastPos.set( t.getCurrent() );

            } else if ( dragging ) {
                camera.scroll.offset( PointF.diff( lastPos, t.getCurrent() ).invScale( camera.zoom ) );
                lastPos.set( t.getCurrent() );
            }
        }

    }

    public void cancel () {
        if ( listener != null ) {
            listener.onSelect( null );
        }

        GameScene.ready();
    }

    public interface Listener {
        void onSelect ( Integer cell );

        String prompt ();
    }
}
