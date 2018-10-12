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
package com.demasu.testpixeldungeon.actors.buffs;

import com.demasu.testpixeldungeon.Badges;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.ResultDescriptions;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.blobs.Blob;
import com.demasu.testpixeldungeon.actors.blobs.Fire;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.mobs.Thief;
import com.demasu.testpixeldungeon.effects.particles.ElmoParticle;
import com.demasu.testpixeldungeon.items.Heap;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.food.ChargrilledMeat;
import com.demasu.testpixeldungeon.items.food.MysteryMeat;
import com.demasu.testpixeldungeon.items.rings.RingOfElements.Resistance;
import com.demasu.testpixeldungeon.items.scrolls.Scroll;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.ui.BuffIndicator;
import com.demasu.testpixeldungeon.utils.GLog;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Burning extends Buff implements Hero.Doom {

    private static final String TXT_BURNS_UP = "%s burns up!";
    private static final String TXT_BURNED_TO_DEATH = "You burned to death...";

    private static final float DURATION = 8f;
    private static final String LEFT = "left";
    private float left;

    private static float duration ( Char ch ) {
        Resistance r = ch.buff( Resistance.class );
        return r != null ? r.durationFactor() * DURATION : DURATION;
    }

    @Override
    public void storeInBundle ( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEFT, left );
    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        left = bundle.getFloat( LEFT );
    }

    @SuppressWarnings ( "FeatureEnvy" )
    @Override
    public boolean act () {

        if ( getTarget().isAlive() ) {

            if ( getTarget() instanceof Hero ) {
                final float TICK_MOD = 1.01f;
                Buff.prolong( getTarget(), Light.class, TICK * TICK_MOD );
            }

            getTarget().damage( Random.Int( 1, 5 ), this );

            if ( getTarget() instanceof Hero ) {

                Item item = ( (Hero) getTarget() ).belongings.randomUnequipped();
                if ( item instanceof Scroll ) {

                    item = item.detach( ( (Hero) getTarget() ).belongings.backpack );
                    //noinspection ConstantConditions
                    GLog.w( TXT_BURNS_UP, item.toString() );

                    Heap.burnFX( getTarget().pos );

                } else if ( item instanceof MysteryMeat ) {

                    item = item.detach( ( (Hero) getTarget() ).belongings.backpack );
                    ChargrilledMeat steak = new ChargrilledMeat();
                    if ( !steak.collect( ( (Hero) getTarget() ).belongings.backpack ) ) {
                        Dungeon.getLevel().drop( steak, getTarget().pos ).sprite.drop();
                    }
                    //noinspection ConstantConditions
                    GLog.w( TXT_BURNS_UP, item.toString() );

                    Heap.burnFX( getTarget().pos );

                }

            } else if ( getTarget() instanceof Thief && ( (Thief) getTarget() ).item instanceof Scroll ) {

                //noinspection AssignmentToNull
                ( (Thief) getTarget() ).item = null;
                getTarget().sprite.emitter().burst( ElmoParticle.FACTORY, 6 );
            }

        } else {
            detach();
        }

        if ( Level.flamable[getTarget().pos] ) {
            GameScene.add( Blob.seed( getTarget().pos, 4, Fire.class ) );
        }

        spend( TICK );
        left -= TICK;

        if ( left <= 0 ||
                Random.Float() > ( 2 + (float) getTarget().getHP() / getTarget().getHT() ) / 3 ||
                ( Level.water[getTarget().pos] && !getTarget().flying ) ) {

            detach();
        }

        return true;
    }

    public void reignite ( Char ch ) {
        left = duration( ch );
    }

    @Override
    public int icon () {
        return BuffIndicator.FIRE;
    }

    @Override
    public String toString () {
        return "Burning";
    }

    @Override
    public void onDeath () {

        Badges.validateDeathFromFire();

        Dungeon.fail( Utils.format( ResultDescriptions.BURNING, Dungeon.getDepth() ) );
        GLog.n( TXT_BURNED_TO_DEATH );
    }
}
