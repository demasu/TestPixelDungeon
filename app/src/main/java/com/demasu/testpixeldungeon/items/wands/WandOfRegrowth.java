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
package com.demasu.testpixeldungeon.items.wands;

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.blobs.Blob;
import com.demasu.testpixeldungeon.actors.blobs.Regrowth;
import com.demasu.testpixeldungeon.effects.MagicMissile;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Terrain;
import com.demasu.testpixeldungeon.mechanics.Ballistica;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class WandOfRegrowth extends Wand {

    {
        name = "Wand of Regrowth";
    }

    @Override
    protected void onZap ( int cell ) {

        for ( int i = 1; i < Ballistica.distance - 1; i++ ) {
            int p = Ballistica.trace[i];
            int c = Dungeon.getLevel().map[p];
            if ( c == Terrain.EMPTY ||
                    c == Terrain.EMBERS ||
                    c == Terrain.EMPTY_DECO ) {

                Level.set( p, Terrain.GRASS );
                GameScene.updateMap( p );
                if ( Dungeon.getVisible()[p] ) {
                    GameScene.discoverTile( p, c );
                }

            }
        }

        int c = Dungeon.getLevel().map[cell];
        if ( c == Terrain.EMPTY ||
                c == Terrain.EMBERS ||
                c == Terrain.EMPTY_DECO ||
                c == Terrain.GRASS ||
                c == Terrain.HIGH_GRASS ) {

            GameScene.add( Blob.seed( cell, ( power() + 2 ) * 20, Regrowth.class ) );

        } else {

            GLog.i( "nothing happened" );

        }
    }

    protected void fx ( int cell, Callback callback ) {
        MagicMissile.foliage( curUser.sprite.getParent(), curUser.pos, cell, callback );
        Sample.INSTANCE.play( Assets.SND_ZAP );
    }

    @Override
    public String desc () {
        return
                "\"When life ceases new life always begins to grow... The eternal cycle always remains!\"";
    }
}
