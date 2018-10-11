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

import com.demasu.testpixeldungeon.Journal;
import com.demasu.testpixeldungeon.Journal.Feature;
import com.demasu.testpixeldungeon.effects.BlobEmitter;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.items.Generator;
import com.demasu.testpixeldungeon.items.Generator.Category;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.potions.Potion;
import com.demasu.testpixeldungeon.items.potions.PotionOfMight;
import com.demasu.testpixeldungeon.items.potions.PotionOfStrength;
import com.demasu.testpixeldungeon.items.rings.Ring;
import com.demasu.testpixeldungeon.items.scrolls.Scroll;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.demasu.testpixeldungeon.items.wands.Wand;
import com.demasu.testpixeldungeon.items.weapon.melee.BattleAxe;
import com.demasu.testpixeldungeon.items.weapon.melee.Dagger;
import com.demasu.testpixeldungeon.items.weapon.melee.Glaive;
import com.demasu.testpixeldungeon.items.weapon.melee.Knuckles;
import com.demasu.testpixeldungeon.items.weapon.melee.Longsword;
import com.demasu.testpixeldungeon.items.weapon.melee.Mace;
import com.demasu.testpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.demasu.testpixeldungeon.items.weapon.melee.Quarterstaff;
import com.demasu.testpixeldungeon.items.weapon.melee.Spear;
import com.demasu.testpixeldungeon.items.weapon.melee.Sword;
import com.demasu.testpixeldungeon.items.weapon.melee.WarHammer;
import com.demasu.testpixeldungeon.plants.Plant;

public class WaterOfTransmutation extends WellWater {

    @Override
    protected Item affectItem ( Item item ) {

        Item item1 = item;
        if ( item1 instanceof MeleeWeapon ) {
            item1 = changeWeapon( (MeleeWeapon) item1 );
        } else if ( item1 instanceof Scroll ) {
            item1 = changeScroll( (Scroll) item1 );
        } else if ( item1 instanceof Potion ) {
            item1 = changePotion( (Potion) item1 );
        } else if ( item1 instanceof Ring ) {
            item1 = changeRing( (Ring) item1 );
        } else if ( item1 instanceof Wand ) {
            item1 = changeWand( (Wand) item1 );
        } else if ( item1 instanceof Plant.Seed ) {
            item1 = changeSeed( (Plant.Seed) item1 );
        } else {
            //noinspection AssignmentToNull
            item1 = null;
        }

        if ( item1 != null ) {
            Journal.remove( Feature.WELL_OF_TRANSMUTATION );
        }

        return item1;
    }

    @Override
    public void use ( BlobEmitter emitter ) {
        super.use( emitter );
        final float INTERVAL = 0.2f;
        emitter.start( Speck.factory( Speck.CHANGE ), INTERVAL, 0 );
    }

    @SuppressWarnings ( "FeatureEnvy" )
    private MeleeWeapon changeWeapon ( MeleeWeapon w ) {

        MeleeWeapon n = null;

        if ( w instanceof Knuckles ) {
            n = new Dagger();
        } else if ( w instanceof Dagger ) {
            n = new Knuckles();
        } else if ( w instanceof Spear ) {
            n = new Quarterstaff();
        } else if ( w instanceof Quarterstaff ) {
            n = new Spear();
        } else if ( w instanceof Sword ) {
            n = new Mace();
        } else if ( w instanceof Mace ) {
            n = new Sword();
        } else if ( w instanceof Longsword ) {
            n = new BattleAxe();
        } else if ( w instanceof BattleAxe ) {
            n = new Longsword();
        } else if ( w instanceof Glaive ) {
            n = new WarHammer();
        } else if ( w instanceof WarHammer ) {
            n = new Glaive();
        }

        if ( n != null ) {

            int level = w.level();
            if ( level > 0 ) {
                n.upgrade( level );
            } else if ( level < 0 ) {
                n.degrade( -level );
            }

            if ( w.isEnchanted() ) {
                n.enchant();
            }

            n.levelKnown = w.levelKnown;
            n.cursedKnown = w.cursedKnown;
            n.cursed = w.cursed;

            Journal.remove( Feature.WELL_OF_TRANSMUTATION );

            return n;
        } else {
            return null;
        }
    }

    @SuppressWarnings ( "FeatureEnvy" )
    private Ring changeRing ( Ring r ) {
        Ring n;
        //noinspection ConstantConditions
        do {
            n = (Ring) Generator.random( Category.RING );
        } while ( n.getClass() == r.getClass() );

        n.level( 0 );

        int level = r.level();
        if ( level > 0 ) {
            n.upgrade( level );
        } else if ( level < 0 ) {
            n.degrade( -level );
        }

        n.levelKnown = r.levelKnown;
        n.cursedKnown = r.cursedKnown;
        n.cursed = r.cursed;

        return n;
    }

    @SuppressWarnings ( "FeatureEnvy" )
    private Wand changeWand ( Wand w ) {

        Wand n;
        //noinspection ConstantConditions
        do {
            n = (Wand) Generator.random( Category.WAND );
        } while ( n.getClass() == w.getClass() );

        n.level( 0 );
        n.upgrade( w.level() );

        n.levelKnown = w.levelKnown;
        n.cursedKnown = w.cursedKnown;
        n.cursed = w.cursed;

        return n;
    }

    private Plant.Seed changeSeed ( Plant.Seed s ) {

        Plant.Seed n;

        //noinspection ConstantConditions
        do {
            n = (Plant.Seed) Generator.random( Category.SEED );
        } while ( n.getClass() == s.getClass() );

        return n;
    }

    private Scroll changeScroll ( Scroll s ) {
        if ( s instanceof ScrollOfUpgrade ) {

            return new ScrollOfEnchantment();

        } else if ( s instanceof ScrollOfEnchantment ) {

            return new ScrollOfUpgrade();

        } else {

            Scroll n;
            //noinspection ConstantConditions
            do {
                n = (Scroll) Generator.random( Category.SCROLL );
            } while ( n.getClass() == s.getClass() );
            return n;
        }
    }

    private Potion changePotion ( Potion p ) {
        if ( p instanceof PotionOfStrength ) {

            return new PotionOfMight();

        } else {

            Potion n;
            //noinspection ConstantConditions
            do {
                n = (Potion) Generator.random( Category.POTION );
            } while ( n.getClass() == p.getClass() );
            return n;
        }
    }

    @Override
    public String tileDesc () {
        return
                "Power of change radiates from the water of this well. " +
                        "Throw an item into the well to turn it into something else.";
    }
}
