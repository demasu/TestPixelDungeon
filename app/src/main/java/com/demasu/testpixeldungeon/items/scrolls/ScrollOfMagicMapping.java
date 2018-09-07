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
package com.demasu.testpixeldungeon.items.scrolls;

import com.watabou.noosa.audio.Sample;
import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.buffs.Invisibility;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.effects.SpellSprite;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Terrain;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.utils.GLog;

public class ScrollOfMagicMapping extends Scroll {

    private static final String TXT_LAYOUT = "You are now aware of the level layout.";

    {
        name = "Scroll of Magic Mapping";
    }

    @Override
    protected void doRead () {

        int length = Level.LENGTH;
        int[] map = Dungeon.getLevel().map;
        boolean[] mapped = Dungeon.getLevel().mapped;
        boolean[] discoverable = Level.discoverable;

        boolean noticed = false;

        for ( int i = 0; i < length; i++ ) {

            int terr = map[i];

            if ( discoverable[i] ) {

                mapped[i] = true;
                if ( ( Terrain.flags[terr] & Terrain.SECRET ) != 0 ) {

                    Level.set( i, Terrain.discover( terr ) );
                    GameScene.updateMap( i );

                    if ( Dungeon.getVisible()[i] ) {
                        GameScene.discoverTile( i, terr );
                        discover( i );

                        noticed = true;
                    }
                }
            }
        }
        Dungeon.observe();

        GLog.i( TXT_LAYOUT );
        if ( noticed ) {
            Sample.INSTANCE.play( Assets.SND_SECRET );
        }

        SpellSprite.show( curUser, SpellSprite.MAP );
        Sample.INSTANCE.play( Assets.SND_READ );
        Invisibility.dispel();

        setKnown();

        readAnimation();
    }

    @Override
    public String desc () {
        return
                "When this scroll is read, an image of crystal clarity will be etched into your memory, " +
                        "alerting you to the precise layout of the level and revealing all hidden secrets. " +
                        "The locations of items and creatures will remain unknown.";
    }

    @Override
    public int price () {
        return isKnown() ? 25 * quantity : super.price();
    }

    public static void discover ( int cell ) {
        CellEmitter.get( cell ).start( Speck.factory( Speck.DISCOVER ), 0.1f, 4 );
    }
}
