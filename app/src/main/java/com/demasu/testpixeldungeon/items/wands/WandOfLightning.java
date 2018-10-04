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

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.ResultDescriptions;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.Lightning;
import com.demasu.testpixeldungeon.effects.particles.SparkParticle;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.traps.LightningTrap;
import com.demasu.testpixeldungeon.utils.GLog;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class WandOfLightning extends Wand {

    private ArrayList<Char> affected = new ArrayList<Char>();
    private int[] points = new int[20];
    private int nPoints;

    {
        name = "Wand of Lightning";
    }

    @Override
    protected void onZap ( int cell ) {
        // Everything is processed in fx() method
        if ( !curUser.isAlive() ) {
            Dungeon.fail( Utils.format( ResultDescriptions.WAND, name, Dungeon.getDepth() ) );
            GLog.n( "You killed yourself with your own Wand of Lightning..." );
        }
    }

    private void hit ( Char ch, int damage ) {

        if ( damage < 1 ) {
            return;
        }

        if ( ch == Dungeon.getHero() ) {
            Camera.getMain().shake( 2, 0.3f );
        } else if ( ch instanceof Mob ) {
            damage *= Dungeon.getHero().heroSkills.passiveB2.wandDamageBonus(); // <---- Mage Sorcerer if present
        }

        affected.add( ch );
        ch.damage( Level.water[ch.pos] && !ch.flying ? (int) ( damage * 2 ) : damage, LightningTrap.LIGHTNING );

        ch.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
        ch.sprite.flash();

        points[nPoints++] = ch.pos;

        HashSet<Char> ns = new HashSet<Char>();
        for ( int i = 0; i < Level.NEIGHBOURS8.length; i++ ) {
            Char n = Actor.findChar( ch.pos + Level.NEIGHBOURS8[i] );
            if ( n != null && !affected.contains( n ) ) {
                ns.add( n );
            }
        }

        if ( ns.size() > 0 ) {
            hit( Random.element( ns ), Random.Int( damage / 2, damage ) );
        }
    }

    @Override
    protected void fx ( int cell, Callback callback ) {

        nPoints = 0;
        points[nPoints++] = Dungeon.getHero().pos;

        Char ch = Actor.findChar( cell );
        if ( ch != null ) {

            affected.clear();
            int lvl = power();
            hit( ch, Random.Int( 5 + lvl / 2, 10 + lvl ) );

        } else {

            points[nPoints++] = cell;
            CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 3 );

        }
        curUser.sprite.getParent().add( new Lightning( points, nPoints, callback ) );
    }

    @Override
    public String desc () {
        return
                "This wand conjures forth deadly arcs of electricity, which deal damage " +
                        "to several creatures standing close to each other.";
    }
}
