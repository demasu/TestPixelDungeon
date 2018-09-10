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

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Invisibility;
import com.demasu.testpixeldungeon.actors.hero.Legend;
import com.demasu.testpixeldungeon.actors.mobs.npcs.NPC;
import com.demasu.testpixeldungeon.actors.mobs.npcs.SummonedPet;
import com.demasu.testpixeldungeon.effects.MagicMissile;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.effects.particles.ShadowParticle;
import com.demasu.testpixeldungeon.mechanics.Ballistica;
import com.demasu.testpixeldungeon.scenes.CellSelector;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.scenes.MissionScene;
import com.demasu.testpixeldungeon.sprites.WraithSprite;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfMagicCasting extends Wand {

    protected static CellSelector.Listener zapper = new CellSelector.Listener() {

        @Override
        public void onSelect ( Integer target ) {

            if ( target != null ) {

                curUser = Dungeon.getHero();
                final int cell = Ballistica.cast( curUser.pos, target, true, true );
                curUser.sprite.zap( cell );


                final Wand curWand = Legend.haxWand;

                ( (WandOfMagicCasting) curWand ).castSpellCost();
                curUser.busy();

                curWand.fx( cell, new Callback() {
                    @Override
                    public void call () {
                        curWand.onZap( cell );
                        curWand.wandUsed();
                    }
                } );

                Invisibility.dispel();


            }
        }

        @Override
        public String prompt () {
            return "Choose direction to cast";
        }
    };
    public CAST_TYPES casting = CAST_TYPES.DARK_BOLT;

    {
        name = "Wand of Hax";
    }

    public void castSpell ( CAST_TYPES casting ) {
        this.casting = casting;
        MissionScene.selectCell( zapper );
    }

    public void castSpellCost () {
        switch ( casting ) {
            case DARK_BOLT:
                Dungeon.getHero().MP -= Dungeon.getHero().heroSkills.passiveB2.getManaCost();
                Dungeon.getHero().heroSkills.passiveB2.castTextYell();
                break;
            case DOMINANCE:
                Dungeon.getHero().MP -= Dungeon.getHero().heroSkills.passiveB3.getManaCost();
                Dungeon.getHero().heroSkills.passiveB3.castTextYell();
                break;
            case SOUL_SPARK:
                Dungeon.getHero().MP -= Dungeon.getHero().heroSkills.active2.getManaCost();
                Dungeon.getHero().heroSkills.active2.castTextYell();
                break;
            case SPARK:
                Dungeon.getHero().MP -= Dungeon.getHero().heroSkills.active2.getManaCost();
                Dungeon.getHero().heroSkills.active2.castTextYell();
                break;

        }
    }

    @Override
    protected void onZap ( int cell ) {
        Char ch = Actor.findChar( cell );
        if ( ch != null ) {
            if ( ch instanceof NPC && casting != CAST_TYPES.SOUL_SPARK ) {
                return;
            }

            if ( casting == CAST_TYPES.DARK_BOLT ) {
                ch.sprite.emitter().burst( ShadowParticle.CURSE, 6 );
                Sample.INSTANCE.play( Assets.SND_CURSED );
                SummonedPet minion = new SummonedPet( WraithSprite.class );
                minion.name = "Consumed Soul";
                minion.screams = false;
                minion.HT = ch.HT;
                minion.HP = minion.HT;
                minion.defenseSkill = 5;
                minion.pos = cell;
                GameScene.add( minion );
                minion.sprite.alpha( 0 );
                minion.sprite.parent.add( new AlphaTweener( minion.sprite, 1, 0.15f ) );

                ch.die( null );
            } else if ( casting == CAST_TYPES.DOMINANCE ) {
                ch.sprite.emitter().burst( ShadowParticle.CURSE, 6 );
                Sample.INSTANCE.play( Assets.SND_CURSED );
                SummonedPet minion = new SummonedPet( ch.sprite.getClass() );
                minion.name = "Enslaved " + ch.name;
                minion.screams = false;
                minion.HT = ch.HT;
                minion.HP = minion.HT;
                minion.defenseSkill = ch.defenseSkill( Dungeon.getHero() );
                minion.pos = cell;
                GameScene.add( minion );
                minion.sprite.alpha( 0 );
                minion.sprite.parent.add( new AlphaTweener( minion.sprite, 1, 0.15f ) );
                ch.sprite.visible = false;
                ch.die( null );
            } else if ( casting == CAST_TYPES.SOUL_SPARK ) {
                ch.HP = ch.HT;
                ch.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
            } else if ( casting == CAST_TYPES.SPARK ) {
                ch.damage( Random.Int( Dungeon.getHero().heroSkills.active2.level * 3, Dungeon.getHero().heroSkills.active2.level * 5 ), Dungeon.getHero() );
            }

        } else {

            GLog.i( "nothing happened" );

        }
    }

    protected void fx ( int cell, Callback callback ) {
        if ( casting == CAST_TYPES.DARK_BOLT ) {
            MagicMissile.shadow( curUser.sprite.parent, curUser.pos, cell, callback, 1 );
        } else if ( casting == CAST_TYPES.DOMINANCE ) {
            MagicMissile.shadow( curUser.sprite.parent, curUser.pos, cell, callback, 3 );
        } else if ( casting == CAST_TYPES.SOUL_SPARK ) {
            MagicMissile.whiteLight( curUser.sprite.parent, curUser.pos, cell, callback );
        } else if ( casting == CAST_TYPES.SPARK ) {
            MagicMissile.blueLight( curUser.sprite.parent, curUser.pos, cell, callback );
        }

        Sample.INSTANCE.play( Assets.SND_ZAP );
    }

    @Override
    public String desc () {
        return
                "The vile blast of this twisted bit of wood will imbue its target " +
                        "with a deadly venom. A creature that is poisoned will suffer periodic " +
                        "damage until the effect ends. The duration of the effect increases " +
                        "with the level of the staff.";
    }

    public enum CAST_TYPES {DARK_BOLT, DOMINANCE, SOUL_SPARK, SPARK}
}
