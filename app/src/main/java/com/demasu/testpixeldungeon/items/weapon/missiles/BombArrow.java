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
package com.demasu.testpixeldungeon.items.weapon.missiles;

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Paralysis;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.particles.BlastParticle;
import com.demasu.testpixeldungeon.effects.particles.SmokeParticle;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Terrain;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BombArrow extends Arrow {

    {
        name = "bomb arrow";
        image = ItemSpriteSheet.BombArrow;

        stackable = true;
    }

    public BombArrow () {
        this( 1 );
    }

    public BombArrow ( int number ) {
        super();
        quantity = number;
    }

    @Override
    public Item random () {
        quantity = Random.Int( 1, 3 );
        return this;
    }

    @Override
    public String desc () {
        return
                "An arrow with an attached bomb. Keep your distance..";
    }


    @Override
    public int price () {
        return quantity * 15;
    }

    @Override
    public ArrayList<String> actions ( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if ( Dungeon.getHero().belongings.bow != null ) {
            if ( !actions.contains( AC_THROW ) ) {
                actions.add( AC_THROW );
            }
        } else {
            actions.remove( AC_THROW );
        }
        actions.remove( AC_EQUIP );

        return actions;
    }


    @Override
    protected void onThrow ( int cell ) {
        if ( Level.pit[cell] ) {
            super.onThrow( cell );
        } else {
            Sample.INSTANCE.play( Assets.SND_BLAST, 2 );

            if ( Dungeon.getVisible()[cell] ) {
                CellEmitter.center( cell ).burst( BlastParticle.FACTORY, 30 );
            }

            boolean terrainAffected = false;
            for ( int n : Level.NEIGHBOURS9 ) {
                int c = cell + n;
                if ( c >= 0 && c < Level.LENGTH ) {
                    if ( Dungeon.getVisible()[c] ) {
                        CellEmitter.get( c ).burst( SmokeParticle.FACTORY, 4 );
                    }

                    if ( Level.flamable[c] ) {
                        Level.set( c, Terrain.EMBERS );
                        GameScene.updateMap( c );
                        terrainAffected = true;
                    }

                    Char ch = Actor.findChar( c );
                    if ( ch != null ) {
                        int dmg = Random.Int( 1 + Dungeon.getDepth(), 10 + Dungeon.getDepth() * 2 ) - Random.Int( ch.dr() );
                        if ( dmg > 0 ) {
                            ch.damage( dmg, this );
                            if ( ch.isAlive() ) {
                                Buff.prolong( ch, Paralysis.class, 2 );
                            }
                        }
                    }
                }
            }

            if ( terrainAffected ) {
                Dungeon.observe();
            }
        }
    }
}
