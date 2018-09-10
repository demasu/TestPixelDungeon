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
package com.demasu.testpixeldungeon.items;

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.mobs.npcs.SummonedPet;
import com.demasu.testpixeldungeon.effects.Pushing;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.sprites.CharSprite;
import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;
import com.demasu.testpixeldungeon.sprites.RatSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SoulCrystalFilled extends Item {

    private static final String SPRITE = "sprite";
    private static final String HEALTH = "HT";
    private static final String DEFENCE_SKILL = "defenceskill";
    private static final String CAPTURED_NAME = "capturedname";
    public Class<? extends CharSprite> minionSprite;
    public int HT, defenceSkill;
    public String captured;

    {
        name = "soul crystal";
        image = ItemSpriteSheet.CRYSTAL_FULL;
        stackable = false;
        quantity = 1;
    }


    public SoulCrystalFilled () {
        this( RatSprite.class, 5, 1, "Rat" );
    }
    public SoulCrystalFilled ( Class<? extends CharSprite> minionSprite, int HT, int defenceSkill, String captured ) {
        super();
        this.minionSprite = minionSprite;
        this.HT = HT;
        this.defenceSkill = defenceSkill;
        this.captured = captured;
    }

    @Override
    protected void onThrow ( int cell ) {
        if ( Level.pit[cell] ) {
            super.onThrow( cell );
        } else {
            shatter( cell );
        }
    }

    private void shatter ( int pos ) {
        Sample.INSTANCE.play( Assets.SND_SHATTER );


        int newPos = pos;
        if ( Actor.findChar( pos ) != null ) {
            ArrayList<Integer> candidates = new ArrayList<Integer>();
            boolean[] passable = Level.passable;

            for ( int n : Level.NEIGHBOURS4 ) {
                int c = pos + n;
                if ( passable[c] && Actor.findChar( c ) == null ) {
                    candidates.add( c );
                }
            }

            newPos = candidates.size() > 0 ? Random.element( candidates ) : -1;
        }

        if ( newPos != -1 ) {

            SummonedPet minion = new SummonedPet( minionSprite );
            minion.name = captured;
            minion.HT = HT;
            minion.HP = minion.HT;
            minion.defenseSkill = defenceSkill;
            minion.pos = newPos;

            GameScene.add( minion );
            Actor.addDelayed( new Pushing( minion, pos, newPos ), -1 );

            minion.sprite.alpha( 0 );
            minion.sprite.parent.add( new AlphaTweener( minion.sprite, 1, 0.15f ) );
        }
    }

    @Override
    public void storeInBundle ( Bundle bundle ) {
        super.storeInBundle( bundle );

        bundle.put( HEALTH, HT );
        bundle.put( DEFENCE_SKILL, defenceSkill );
        bundle.put( CAPTURED_NAME, captured );
        bundle.put( SPRITE, minionSprite.toString().replace( "class ", "" ) );
    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        HT = bundle.getInt( HEALTH );
        defenceSkill = bundle.getInt( DEFENCE_SKILL );
        captured = bundle.getString( CAPTURED_NAME );
        try {
            //noinspection unchecked
            minionSprite = (Class<? extends CharSprite>) Class.forName( bundle.getString( SPRITE ) );
        } catch ( Exception ex ) {
            minionSprite = RatSprite.class;
        }
    }

    @Override
    public String desc () {
        return
                "A soul crystal holding the essence of a " + captured + ".\nThrow it to summon the spirit into your service.";
    }

    @Override
    public String info () {

        StringBuilder info = new StringBuilder( desc() );


        return info.toString();
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
        return quantity * 12;
    }
}
