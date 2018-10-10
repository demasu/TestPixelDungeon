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
package com.demasu.testpixeldungeon;

import com.demasu.testpixeldungeon.levels.Level;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

public class DungeonTilemap extends Tilemap {

    public static final int SIZE = 16;

    private static DungeonTilemap instance;

    public DungeonTilemap () {
        super(
                Dungeon.getLevel().tilesTex(),
                new TextureFilm( Dungeon.getLevel().tilesTex(), SIZE, SIZE ) );
        map( Dungeon.getLevel().map, Level.WIDTH );

        instance = this;
    }

    public static PointF tileToWorld ( int pos ) {
        return new PointF( pos % Level.WIDTH, pos / Level.WIDTH ).scale( SIZE );
    }

    public static PointF tileCenterToWorld ( int pos ) {
        return new PointF(
                ( pos % Level.WIDTH + 0.5f ) * SIZE,
                ( pos / Level.WIDTH + 0.5f ) * SIZE );
    }

    public static Image tile ( int index ) {
        Image img = new Image( instance.getTexture() );
        img.frame( instance.getTileset().get( index ) );
        return img;
    }

    public int screenToTile ( int x, int y ) {
        Point p = camera().screenToCamera( x, y ).
                offset( this.point().negate() ).
                invScale( SIZE ).
                floor();
        return p.getX() >= 0 && p.getX() < Level.WIDTH && p.getY() >= 0 && p.getY() < Level.HEIGHT ? p.getX() + p.getY() * Level.WIDTH : -1;
    }

    @Override
    public boolean overlapsPoint ( float x, float y ) {
        return true;
    }

    public void discover ( int pos, int oldValue ) {

        final Image tile = tile( oldValue );
        tile.point( tileToWorld( pos ) );

        // For bright mode
        float rm = getRm();
        float ra = getRa();
        tile.setRm( rm );
        tile.setGm( rm );
        tile.setBm( rm );
        tile.setRa( ra );
        tile.setGa( ra );
        tile.setBa( ra );
        getParent().add( tile );

        getParent().add( new AlphaTweener( tile, 0, 0.6f ) {
            protected void onComplete () {
                tile.killAndErase();
                killAndErase();
            }

        } );
    }

    @Override
    public boolean overlapsScreenPoint ( int x, int y ) {
        return true;
    }
}
