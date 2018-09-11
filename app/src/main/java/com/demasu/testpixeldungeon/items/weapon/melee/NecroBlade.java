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
package com.demasu.testpixeldungeon.items.weapon.melee;

import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.mobs.npcs.Skeleton;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.Pushing;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.effects.particles.ShadowParticle;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class NecroBlade extends MeleeWeapon {

    public static final String AC_HEAL = "Heal";
    public static final String AC_SUMMON = "Summon";
    public static final String AC_UPGRADE = "Consume";
    public int charge = 100;

    {
        name = "necroblade";
        image = ItemSpriteSheet.NecroBlade5;
    }

    public NecroBlade () {
        super( 1, 0.7f, 1f );
    }

    @Override
    public ArrayList<String> actions ( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if ( charge > 25 ) {
            actions.add( AC_HEAL );
        }
        if ( charge > 55 ) {
            actions.add( AC_SUMMON );
        }
        if ( charge == 100 ) {
            actions.add( AC_UPGRADE );
        }
        return actions;
    }


    @Override
    public void execute ( Hero hero, String action ) {
        if ( action.equals( AC_HEAL ) ) {

            hero.setHP( (int) (hero.getHP() + hero.getHT() * 0.35) );
            if ( hero.getHP() > hero.getHT() ) {
                hero.setHP( hero.getHT() );
            }
            GLog.p( "NecroBlade heals " + ( (int) ( hero.getHT() * 0.35 ) ) + " HP" );
            updateCharge( -25 );

            CellEmitter.center( hero.pos ).burst( Speck.factory( Speck.HEALING ), 1 );
            // CellEmitter.center(hero.pos).burst(ShadowParticle.UP, Random.IntRange(1, 2));


        } else if ( action.equals( AC_SUMMON ) ) {


            int newPos = hero.pos;
            if ( Actor.findChar( newPos ) != null ) {
                ArrayList<Integer> candidates = new ArrayList<Integer>();
                boolean[] passable = Level.passable;

                for ( int n : Level.NEIGHBOURS4 ) {
                    int c = hero.pos + n;
                    if ( passable[c] && Actor.findChar( c ) == null ) {
                        candidates.add( c );
                    }
                }
                newPos = candidates.size() > 0 ? Random.element( candidates ) : -1;
            }
            if ( newPos != -1 ) {

                updateCharge( -55 );
                Skeleton skel = new Skeleton();
                int skelLevel = this.level() > 1 ? 1 + this.level() : 1;
                if ( skelLevel > 7 ) {
                }
                skelLevel = 7;
                skel.spawn( skelLevel );
                skel.setHP( skel.getHT() );
                skel.pos = newPos;

                GameScene.add( skel );
                Actor.addDelayed( new Pushing( skel, hero.pos, newPos ), -1 );

                skel.sprite.alpha( 0 );
                skel.sprite.parent.add( new AlphaTweener( skel.sprite, 1, 0.15f ) );
                CellEmitter.center( newPos ).burst( ShadowParticle.UP, Random.IntRange( 3, 5 ) );
                GLog.p( "NecroBlade summoned a skeleton" );
            } else {
                GLog.i( "No place to summon" );
            }

        } else if ( action.equals( AC_UPGRADE ) ) {
            updateCharge( -100 );
            this.upgrade( 1 );
            GLog.p( "NecroBlade consumed the souls within. It looks much better now." );
        } else {

            super.execute( hero, action );

        }
    }

    @Override
    public int damageRoll ( Hero hero ) {
        int damage = super.damageRoll( hero );
        damage += Random.Int( (int) ( charge / 8 ) );
        return damage;
    }


    public void updateCharge ( int change ) {
        charge += change;
        if ( charge > 100 ) {
            charge = 100;
        }
        if ( charge < 0 ) {
            charge = 0;
        }

        switch ( (int) Math.floor( charge / 20 ) ) {
            case 0:
                image = ItemSpriteSheet.NecroBlade0;
                break;
            case 1:
                image = ItemSpriteSheet.NecroBlade1;
                break;
            case 2:
                image = ItemSpriteSheet.NecroBlade2;
                break;
            case 3:
                image = ItemSpriteSheet.NecroBlade3;
                break;
            case 4:
                image = ItemSpriteSheet.NecroBlade4;
                break;
            case 5:
                image = ItemSpriteSheet.NecroBlade5;
                break;
        }
    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        charge = bundle.getInt( "CHARGE" );
    }

    @Override
    public void storeInBundle ( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( "CHARGE", charge );
    }

    @Override
    public String desc () {
        return "A blade forged from dark magic. NecroBlades consume the souls of those who perish by them. The more they consume, the stronger they become.\n" +
                "NecroBlade energy at " + charge + "/100\n"
                + "The energy stored within increases damage by 0 - " + ( (int) ( charge / 8 ) ) + ".";
    }
}
