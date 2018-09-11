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

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.buffs.Blindness;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Invisibility;
import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ScrollOfPsionicBlast extends Scroll {

    {
        name = "Scroll of Psionic Blast";
    }

    @Override
    protected void doRead () {

        GameScene.flash( 0xFFFFFF );

        Sample.INSTANCE.play( Assets.SND_BLAST );
        Invisibility.dispel();

        for ( Mob mob : Dungeon.getLevel().mobs.toArray( new Mob[0] ) ) {
            if ( Level.fieldOfView[mob.pos] ) {
                Buff.prolong( mob, Blindness.class, Random.Int( 3, 6 ) );
                mob.damage( Random.IntRange( 1, mob.getHT() * 2 / 3 ), this );
            }
        }

        Buff.prolong( curUser, Blindness.class, Random.Int( 3, 6 ) );
        Dungeon.observe();

        setKnown();

        readAnimation();
    }

    @Override
    public String desc () {
        return
                "This scroll contains destructive energy, that can be psionically channeled to inflict a " +
                        "massive damage to all creatures within a field of view. An accompanying flash of light will " +
                        "temporarily blind everybody in the area of effect including the reader of the scroll.";
    }

    @Override
    public int price () {
        return isKnown() ? 80 * quantity : super.price();
    }
}
