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
package com.demasu.testpixeldungeon.actors.blobs;

import com.watabou.noosa.audio.Sample;
import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.Journal;
import com.demasu.testpixeldungeon.Journal.Feature;
import com.demasu.testpixeldungeon.actors.buffs.Hunger;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.effects.BlobEmitter;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.effects.particles.ShaftParticle;
import com.demasu.testpixeldungeon.items.DewVial;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.potions.PotionOfHealing;
import com.demasu.testpixeldungeon.utils.GLog;

public class WaterOfHealth extends WellWater {

    private static final String TXT_PROCCED =
            "As you take a sip, you feel your wounds heal completely.";

    @Override
    protected boolean affectHero ( Hero hero ) {

        Sample.INSTANCE.play( Assets.SND_DRINK );

        PotionOfHealing.heal( hero );
        hero.belongings.uncurseEquipped();
        ( (Hunger) hero.buff( Hunger.class ) ).satisfy( Hunger.STARVING );

        CellEmitter.get( pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );

        Dungeon.getHero().interrupt();

        GLog.p( TXT_PROCCED );

        Journal.remove( Feature.WELL_OF_HEALTH );

        return true;
    }

    @Override
    protected Item affectItem ( Item item ) {
        if ( item instanceof DewVial && !( (DewVial) item ).isFull() ) {
            ( (DewVial) item ).fill();
            Journal.remove( Feature.WELL_OF_HEALTH );
            return item;
        }

        return null;
    }

    @Override
    public void use ( BlobEmitter emitter ) {
        super.use( emitter );
        emitter.start( Speck.factory( Speck.HEALING ), 0.5f, 0 );
    }

    @Override
    public String tileDesc () {
        return
                "Power of health radiates from the water of this well. " +
                        "Take a sip from it to heal your wounds and satisfy hunger.";
    }
}
