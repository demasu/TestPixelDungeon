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

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.DungeonTilemap;
import com.demasu.testpixeldungeon.Journal;
import com.demasu.testpixeldungeon.Journal.Feature;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.FlavourBuff;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.effects.BlobEmitter;
import com.demasu.testpixeldungeon.effects.Flare;
import com.demasu.testpixeldungeon.effects.Wound;
import com.demasu.testpixeldungeon.effects.particles.SacrificialParticle;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfWipeOut;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.ui.BuffIndicator;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SacrificialFire extends Blob {

    private static final String TXT_WORTHY = "\"Your sacrifice is worthy...\" ";
    private static final String TXT_UNWORTHY = "\"Your sacrifice is unworthy...\" ";
    private static final String TXT_REWARD = "\"Your sacrifice is worthy and so you are!\" ";

    private int pos;

    @SuppressWarnings ( "FeatureEnvy" )
    private static void sacrifice ( Char ch ) {

        Wound.hit( ch );

        SacrificialFire fire = (SacrificialFire) Dungeon.getLevel().blobs.get( SacrificialFire.class );
        if ( fire != null ) {

            int exp = 0;
            if ( ch instanceof Mob ) {
                exp = ( (Mob) ch ).exp() * Random.IntRange( 1, 3 );
            } else if ( ch instanceof Hero ) {
                exp = ( (Hero) ch ).maxExp();
            }

            if ( exp > 0 ) {

                int volume = fire.getVolume() - exp;
                if ( volume > 0 ) {
                    fire.seed( fire.getPos(), volume );
                    GLog.w( TXT_WORTHY );
                } else {
                    fire.seed( fire.getPos(), 0 );
                    Journal.remove( Feature.SACRIFICIAL_FIRE );

                    GLog.w( TXT_REWARD );
                    final int RADIUS     = 32;
                    final int COLOR      = 0x66FFFF;
                    final float DURATION = 2f;
                    GameScene.effect( new Flare( 7, RADIUS ).color( COLOR, true ).show( ch.sprite.getParent(), DungeonTilemap.tileCenterToWorld( fire.getPos() ), DURATION ) );
                    Dungeon.getLevel().drop( new ScrollOfWipeOut(), fire.getPos() ).sprite.drop();
                }
            } else {

                GLog.w( TXT_UNWORTHY );

            }
        }
    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );

        for ( int i = 0; i < LENGTH; i++ ) {
            if ( getCur()[i] > 0 ) {
                setPos( i );
                break;
            }
        }
    }

    @Override
    protected void evolve () {
        getOff()[getPos()] = getCur()[getPos()];
        setVolume( getOff()[getPos()] );
        Char ch = Actor.findChar( getPos() );
        if ( ch != null ) {
            if ( Dungeon.getVisible()[getPos()] && ch.buff( Marked.class ) == null ) {
                final int QUANTITY = 20;
                ch.sprite.emitter().burst( SacrificialParticle.FACTORY, QUANTITY );
                Sample.INSTANCE.play( Assets.SND_BURNING );
            }
            Buff.prolong( ch, Marked.class, Marked.DURATION );
        }
        if ( Dungeon.getVisible()[getPos()] ) {
            Journal.add( Feature.SACRIFICIAL_FIRE );
        }
    }

    @Override
    public void seed ( int cell, int amount ) {
        getCur()[getPos()] = 0;
        setPos( cell );
        getCur()[getPos()] = amount;
        setVolume( getCur()[getPos()] );
    }

    @Override
    public void use ( BlobEmitter emitter ) {
        super.use( emitter );

        final float INTERVAL = 0.04f;
        emitter.pour( SacrificialParticle.FACTORY, INTERVAL );
    }

    @Override
    public String tileDesc () {
        return "Sacrificial fire burns here. Every creature touched by this fire is marked as an offering for the spirits of the dungeon.";
    }

    private int getPos () {
        return pos;
    }

    private void setPos ( int pos ) {
        this.pos = pos;
    }

    //TODO: Move into its own file
    @SuppressWarnings ( "PackageVisibleInnerClass" )
    static class Marked extends FlavourBuff {

        static final float DURATION = 5f;

        @Override
        public int icon () {
            return BuffIndicator.SACRIFICE;
        }

        @Override
        public String toString () {
            return "Marked for sacrifice";
        }

        @Override
        public void detach () {
            if ( !target.isAlive() ) {
                sacrifice( target );
            }
            super.detach();
        }
    }

}
