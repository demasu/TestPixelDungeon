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
package com.demasu.testpixeldungeon.plants;

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Barkskin;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.hero.HeroSubClass;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.particles.LeafParticle;
import com.demasu.testpixeldungeon.items.Dewdrop;
import com.demasu.testpixeldungeon.items.Generator;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Terrain;
import com.demasu.testpixeldungeon.sprites.PlantSprite;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Plant implements Bundlable {

    private static final String POS = "pos";
    public String plantName;
    public int image;
    public int pos;
    public PlantSprite sprite;

    public void activate ( Char ch ) {

        if ( ch instanceof Hero && ( (Hero) ch ).subClass == HeroSubClass.WARDEN ) {
            Buff.affect( ch, Barkskin.class ).level( ch.getHT() / 3 );
        }

        wither();
    }

    public void wither () {
        Dungeon.getLevel().uproot( pos );

        sprite.kill();
        if ( Dungeon.getVisible()[pos] ) {
            CellEmitter.get( pos ).burst( LeafParticle.GENERAL, 6 );
        }

        if ( Dungeon.getHero().subClass == HeroSubClass.WARDEN ) {
            if ( Random.Int( 5 ) == 0 ) {
                Dungeon.getLevel().drop( Generator.random( Generator.Category.SEED ), pos ).sprite.drop();
            }
            if ( Random.Int( 5 ) == 0 ) {
                Dungeon.getLevel().drop( new Dewdrop(), pos ).sprite.drop();
            }
        }
    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        pos = bundle.getInt( POS );
    }

    @Override
    public void storeInBundle ( Bundle bundle ) {
        bundle.put( POS, pos );
    }

    public String desc () {
        return null;
    }

    public static class Seed extends Item {

        public static final String AC_PLANT = "PLANT";

        private static final String TXT_INFO = "Throw this seed to the place where you want to grow %s.\n\n%s";

        private static final float TIME_TO_PLANT = 1f;
        public Class<? extends Item> alchemyClass;
        protected Class<? extends Plant> plantClass;
        protected String plantName;

        {
            stackable = true;
            defaultAction = AC_THROW;
        }

        @Override
        public ArrayList<String> actions ( Hero hero ) {
            ArrayList<String> actions = super.actions( hero );
            actions.add( AC_PLANT );
            return actions;
        }

        @Override
        protected void onThrow ( int cell ) {
            if ( Dungeon.getLevel().map[cell] == Terrain.ALCHEMY || Level.pit[cell] ) {
                super.onThrow( cell );
            } else {
                Dungeon.getLevel().plant( this, cell );
            }
        }

        @Override
        public void execute ( Hero hero, String action ) {
            if ( action.equals( AC_PLANT ) ) {

                hero.spend( TIME_TO_PLANT );
                hero.busy();
                ( (Seed) detach( hero.belongings.backpack ) ).onThrow( hero.pos );

                hero.sprite.operate( hero.pos );

            } else {

                super.execute( hero, action );

            }
        }

        public Plant couch ( int pos ) {
            try {
                if ( Dungeon.getVisible()[pos] ) {
                    Sample.INSTANCE.play( Assets.SND_PLANT );
                }
                Plant plant = plantClass.newInstance();
                plant.pos = pos;
                return plant;
            } catch ( Exception e ) {
                return null;
            }
        }

        @Override
        public boolean isUpgradable () {
            return false;
        }

        @Override
        public boolean isIdentified () {
            return true;
        }

        @Override
        public int price () {
            return 10 * quantity;
        }

        @Override
        public String info () {
            return String.format( TXT_INFO, Utils.indefinite( plantName ), desc() );
        }
    }
}
