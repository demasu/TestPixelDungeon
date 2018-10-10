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

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.DungeonTilemap;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.utils.PointF;

public class Compass extends Image {

    private static final float RAD_2_G = 180f / 3.1415926f;
    private static final float RADIUS = 12;

    private int cell;
    private PointF cellCenter;

    private PointF lastScroll = new PointF();

    public Compass ( int cell ) {

        super();
        copy( Icons.COMPASS.get() );
        getOrigin().set( getWidth() / 2, RADIUS );

        this.cell = cell;
        cellCenter = DungeonTilemap.tileCenterToWorld( cell );
        setVisible( false );
    }

    @Override
    public void update () {
        super.update();

        if ( !getVisible() ) {
            setVisible( Dungeon.getLevel().visited[cell] || Dungeon.getLevel().mapped[cell] );
        }

        if ( getVisible() ) {
            PointF scroll = Camera.getMain().getScroll();
            if ( !scroll.equals( lastScroll ) ) {
                lastScroll.set( scroll );
                PointF center = Camera.getMain().center().offset( scroll );
                setAngle( (float) Math.atan2( cellCenter.getX() - center.getX(), center.getY() - cellCenter.getY() ) * RAD_2_G );
            }
        }
    }
}
