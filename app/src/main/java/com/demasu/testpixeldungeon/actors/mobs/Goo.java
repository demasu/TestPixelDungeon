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
package com.demasu.testpixeldungeon.actors.mobs;

import java.util.HashSet;

import com.demasu.testpixeldungeon.Badges;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.Statistics;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.blobs.ToxicGas;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Ooze;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.items.LloydsBeacon;
import com.demasu.testpixeldungeon.items.keys.SkeletonKey;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.demasu.testpixeldungeon.items.weapon.enchantments.Death;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.SewerBossLevel;
import com.demasu.testpixeldungeon.mechanics.Ballistica;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.sprites.CharSprite;
import com.demasu.testpixeldungeon.sprites.GooSprite;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Goo extends Mob {

    private static final float PUMP_UP_DELAY = 2f;

    {
        name = Dungeon.getDepth() == Statistics.deepestFloor ? "Goo" : "spawn of Goo";

        HP = HT = 80;
        EXP = 10;
        defenseSkill = 12;
        spriteClass = GooSprite.class;

        loot = new LloydsBeacon();
        lootChance = 0.333f;

        name = Dungeon.getCurrentDifficulty().mobPrefix() + name;
        HT *= Dungeon.getCurrentDifficulty().mobHPModifier();
        HP = HT;
    }

    private boolean pumpedUp = false;
    private boolean jumped = false;

    @Override
    public int damageRoll () {
        if ( pumpedUp ) {
            return Random.NormalIntRange( 5, 30 );
        } else {
            return Random.NormalIntRange( 2, 12 );
        }
    }

    @Override
    public int attackSkill ( Char target ) {
        return pumpedUp && !jumped ? 30 : 15;
    }

    @Override
    public int dr () {
        return 2;
    }

    @Override
    public boolean act () {

        if ( Level.water[pos] && HP < HT ) {
            sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
            HP++;
        }

        return super.act();
    }

    @Override
    protected boolean canAttack ( Char enemy ) {
        return pumpedUp ? distance( enemy ) <= 2 : super.canAttack( enemy );
    }

    @Override
    public int attackProc ( Char enemy, int damage ) {
        if ( Random.Int( 3 ) == 0 ) {
            Buff.affect( enemy, Ooze.class );
            enemy.sprite.burst( 0x000000, 5 );
        }

        return damage;
    }

    @Override
    protected boolean doAttack ( final Char enemy ) {
        if ( pumpedUp ) {

            if ( Level.adjacent( pos, enemy.pos ) ) {

                // Pumped up attack WITHOUT accuracy penalty
                jumped = false;
                return super.doAttack( enemy );

            } else {

                // Pumped up attack WITH accuracy penalty
                jumped = true;
                if ( Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos ) {
                    final int dest = Ballistica.trace[Ballistica.distance - 2];

                    Callback afterJump = new Callback() {
                        @Override
                        public void call () {
                            move( dest );
                            Dungeon.getLevel().mobPress( Goo.this );
                            Goo.super.doAttack( enemy );
                        }
                    };

                    if ( Dungeon.getVisible()[pos] || Dungeon.getVisible()[dest] ) {

                        sprite.jump( pos, dest, afterJump );
                        return false;

                    } else {

                        afterJump.call();
                        return true;

                    }
                } else {

                    sprite.idle();
                    pumpedUp = false;
                    return true;
                }
            }

        } else if ( Random.Int( 3 ) > 0 ) {

            // Normal attack
            return super.doAttack( enemy );

        } else {

            // Pumping up
            pumpedUp = true;
            spend( PUMP_UP_DELAY );

            ( (GooSprite) sprite ).pumpUp();

            if ( Dungeon.getVisible()[pos] ) {
                sprite.showStatus( CharSprite.NEGATIVE, "!!!" );
                GLog.n( "Goo is pumping itself up!" );
            }

            return true;
        }
    }

    @Override
    public boolean attack ( Char enemy ) {
        boolean result = super.attack( enemy );
        pumpedUp = false;
        return result;
    }

    @Override
    protected boolean getCloser ( int target ) {
        pumpedUp = false;
        return super.getCloser( target );
    }

    @Override
    public void move ( int step ) {
        ( (SewerBossLevel) Dungeon.getLevel() ).seal();
        super.move( step );
    }

    @Override
    public void die ( Object cause ) {

        super.die( cause );

        ( (SewerBossLevel) Dungeon.getLevel() ).unseal();

        GameScene.bossSlain();
        Dungeon.getLevel().drop( new SkeletonKey(), pos ).sprite.drop();

        Badges.validateBossSlain();

        yell( "glurp... glurp..." );
    }

    @Override
    public void notice () {
        super.notice();
        yell( "GLURP-GLURP!" );
    }

    @Override
    public String description () {
        return
                "Little known about The Goo. It's quite possible that it is not even a creature, but rather a " +
                        "conglomerate of substances from the sewers that gained rudiments of free will.";
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

    static {
        RESISTANCES.add( ToxicGas.class );
        RESISTANCES.add( Death.class );
        RESISTANCES.add( ScrollOfPsionicBlast.class );
    }

    @Override
    public HashSet<Class<?>> resistances () {
        return RESISTANCES;
    }
}
