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

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Champ extends Buff {

    public static final int CHAMP_CHIEF = 1;
    public static final int CHAMP_CURSED = 2;
    public static final int CHAMP_FOUL = 3;
    public static final int CHAMP_VAMPERIC = 4;
    private static final String TYPE = "type";
    private static final String BONUS_APPLIED = "bonusApplied";
    public int type = Random.Int( 1, 5 );

    private boolean bonusApplied = false;

    private boolean haloApplied = false;

    @Override
    public void storeInBundle ( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( TYPE, type );
        bundle.put( BONUS_APPLIED, bonusApplied );

    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        type = bundle.getInt( TYPE );
        bonusApplied = bundle.getBoolean( BONUS_APPLIED );
    }

    @Override
    public boolean act () {

        if ( !bonusApplied ) {

            bonusApplied = true;
            haloApplied = true;
            do {
                type = Random.Int( 1, 5 );
                if ( type == 5 ) {
                    type = 4;
                }
            }
            while ( Dungeon.getCurrentDifficulty().disableChampion( type ) );

            this.target.champ = type;

            switch ( type ) {
                case CHAMP_VAMPERIC: //red
                    this.target.name = "Vampiric " + this.target.name;
                    this.target.setHT( (int) (this.target.getHT() * 1.5) );
                    this.target.setHP( this.target.getHT() );
                    ( (Mob) this.target ).defenseSkill *= 1.1;
                    if ( target.sprite != null ) {
                        if ( target.sprite.champRedHalo == null ) {
                            target.sprite.add( CharSprite.State.CHAMPRED );
                        }
                    }
                    break;
                case CHAMP_CHIEF: //white
                    this.target.name = "Chief " + this.target.name;
                    this.target.setHT( this.target.getHT() * 2 );
                    this.target.setHP( this.target.getHT() );
                    ( (Mob) this.target ).defenseSkill *= 1.3;
                    if ( target.sprite != null ) {
                        if ( target.sprite.champWhiteHalo == null ) {
                            target.sprite.add( CharSprite.State.CHAMPWHITE );
                        }
                    }
                    break;
                case CHAMP_CURSED: //black
                    this.target.name = "Cursed " + this.target.name;
                    this.target.setHT( (int) (this.target.getHT() * 1.5) );
                    this.target.setHP( this.target.getHT() );
                    ( (Mob) this.target ).defenseSkill *= 1.15;
                    if ( target.sprite != null ) {
                        if ( target.sprite.champBlackHalo == null ) {
                            target.sprite.add( CharSprite.State.CHAMPBLACK );
                        }
                    }
                    break;
                case CHAMP_FOUL: //yellow
                    this.target.name = "Foul " + this.target.name;
                    this.target.setHT( (int) (this.target.getHT() * 1.5) );
                    this.target.setHP( this.target.getHT() );
                    ( (Mob) this.target ).defenseSkill *= 1.2;
                    if ( target.sprite != null ) {
                        if ( target.sprite.champYellowHalo == null ) {
                            target.sprite.add( CharSprite.State.CHAMPYELLOW );
                        }
                    }
                    break;

            }
        } else if ( !haloApplied ) {
            haloApplied = true;
            switch ( type ) {
                case CHAMP_VAMPERIC: //red
                    this.target.name = "Vampiric " + this.target.name;
                    ( (Mob) this.target ).defenseSkill *= 1.1;
                    if ( target.sprite != null ) {
                        if ( target.sprite.champRedHalo == null ) {
                            target.sprite.add( CharSprite.State.CHAMPRED );
                        }
                    }
                    break;
                case CHAMP_CHIEF: //white
                    this.target.name = "Chief " + this.target.name;
                    ( (Mob) this.target ).defenseSkill *= 1.3;
                    if ( target.sprite != null ) {
                        if ( target.sprite.champWhiteHalo == null ) {
                            target.sprite.add( CharSprite.State.CHAMPWHITE );
                        }
                    }
                    break;
                case CHAMP_CURSED: //black
                    this.target.name = "Cursed " + this.target.name;
                    ( (Mob) this.target ).defenseSkill *= 1.15;
                    if ( target.sprite != null ) {
                        if ( target.sprite.champBlackHalo == null ) {
                            target.sprite.add( CharSprite.State.CHAMPBLACK );
                        }
                    }
                    break;
                case CHAMP_FOUL: //yellow
                    ( (Mob) this.target ).defenseSkill *= 1.2;
                    if ( target.sprite != null ) {
                        if ( target.sprite.champYellowHalo == null ) {
                            target.sprite.add( CharSprite.State.CHAMPYELLOW );
                        }
                    }
                    break;

            }
        }

        spend( TICK );
        if ( target.isAlive() ) {


        } else {

            detach();

        }

        return true;
    }
}
