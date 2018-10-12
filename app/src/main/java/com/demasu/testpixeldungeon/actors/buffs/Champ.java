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
    private int type = Random.Int( 1, 5 );

    private boolean bonusApplied = false;

    private boolean haloApplied = false;

    @Override
    public void storeInBundle ( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( TYPE, getType() );
        bundle.put( BONUS_APPLIED, bonusApplied );

    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        setType( bundle.getInt( TYPE ) );
        bonusApplied = bundle.getBoolean( BONUS_APPLIED );
    }

    @SuppressWarnings ( "FeatureEnvy" )
    @Override
    public boolean act () {

        if ( !bonusApplied ) {

            bonusApplied = true;
            haloApplied = true;
            do {
                setType( Random.Int( 1, 5 ) );
                if ( getType() == 5 ) {
                    setType( 4 );
                }
            }
            while ( Dungeon.getCurrentDifficulty().disableChampion( getType() ) );

            this.getTarget().champ = getType();

            final double HT_MOD  = 1.5;
            final double DEF_MOD = 1.1;
            switch ( getType() ) {
                case CHAMP_VAMPERIC: //red
                    this.getTarget().name = "Vampiric " + this.getTarget().name;
                    this.getTarget().setHT( (int) ( this.getTarget().getHT() * HT_MOD) );
                    this.getTarget().setHP( this.getTarget().getHT() );
                    ( (Mob) this.getTarget() ).defenseSkill *= (DEF_MOD);
                    if ( getTarget().sprite != null ) {
                        if ( getTarget().sprite.champRedHalo == null ) {
                            getTarget().sprite.add( CharSprite.State.CHAMPRED );
                        }
                    }
                    break;
                case CHAMP_CHIEF: //white
                    this.getTarget().name = "Chief " + this.getTarget().name;
                    this.getTarget().setHT( this.getTarget().getHT() * 2 );
                    this.getTarget().setHP( this.getTarget().getHT() );
                    final double CHIEF_MOD = 0.2;
                    ( (Mob) this.getTarget() ).defenseSkill *= (DEF_MOD + CHIEF_MOD);
                    if ( getTarget().sprite != null ) {
                        if ( getTarget().sprite.champWhiteHalo == null ) {
                            getTarget().sprite.add( CharSprite.State.CHAMPWHITE );
                        }
                    }
                    break;
                case CHAMP_CURSED: //black
                    this.getTarget().name = "Cursed " + this.getTarget().name;
                    this.getTarget().setHT( (int) ( this.getTarget().getHT() * HT_MOD) );
                    this.getTarget().setHP( this.getTarget().getHT() );
                    final double CURSED_MOD = 0.05;
                    ( (Mob) this.getTarget() ).defenseSkill *= (DEF_MOD + CURSED_MOD);
                    if ( getTarget().sprite != null ) {
                        if ( getTarget().sprite.champBlackHalo == null ) {
                            getTarget().sprite.add( CharSprite.State.CHAMPBLACK );
                        }
                    }
                    break;
                case CHAMP_FOUL: //yellow
                    this.getTarget().name = "Foul " + this.getTarget().name;
                    this.getTarget().setHT( (int) ( this.getTarget().getHT() * HT_MOD) );
                    this.getTarget().setHP( this.getTarget().getHT() );
                    final double FOUL_MOD = 0.1;
                    ( (Mob) this.getTarget() ).defenseSkill *= (DEF_MOD + FOUL_MOD);
                    if ( getTarget().sprite != null ) {
                        if ( getTarget().sprite.champYellowHalo == null ) {
                            getTarget().sprite.add( CharSprite.State.CHAMPYELLOW );
                        }
                    }
                    break;

            }
        } else if ( !haloApplied ) {
            haloApplied = true;
            final double DEF_MOD = 1.1;
            switch ( getType() ) {
                case CHAMP_VAMPERIC: //red
                    this.getTarget().name = "Vampiric " + this.getTarget().name;
                    ( (Mob) this.getTarget() ).defenseSkill *= DEF_MOD;
                    if ( getTarget().sprite != null ) {
                        if ( getTarget().sprite.champRedHalo == null ) {
                            getTarget().sprite.add( CharSprite.State.CHAMPRED );
                        }
                    }
                    break;
                case CHAMP_CHIEF: //white
                    this.getTarget().name = "Chief " + this.getTarget().name;
                    final double CHIEF_MOD = 0.2;
                    ( (Mob) this.getTarget() ).defenseSkill *= (DEF_MOD + CHIEF_MOD);
                    if ( getTarget().sprite != null ) {
                        if ( getTarget().sprite.champWhiteHalo == null ) {
                            getTarget().sprite.add( CharSprite.State.CHAMPWHITE );
                        }
                    }
                    break;
                case CHAMP_CURSED: //black
                    this.getTarget().name = "Cursed " + this.getTarget().name;
                    final double CURSED_MOD = 0.05;
                    ( (Mob) this.getTarget() ).defenseSkill *= (DEF_MOD + CURSED_MOD);
                    if ( getTarget().sprite != null ) {
                        if ( getTarget().sprite.champBlackHalo == null ) {
                            getTarget().sprite.add( CharSprite.State.CHAMPBLACK );
                        }
                    }
                    break;
                case CHAMP_FOUL: //yellow
                    final double FOUL_MOD = 0.1;
                    ( (Mob) this.getTarget() ).defenseSkill *= (DEF_MOD + FOUL_MOD);
                    if ( getTarget().sprite != null ) {
                        if ( getTarget().sprite.champYellowHalo == null ) {
                            getTarget().sprite.add( CharSprite.State.CHAMPYELLOW );
                        }
                    }
                    break;

            }
        }

        spend( TICK );
        if ( !getTarget().isAlive() ) {
            detach();
        }

        return true;
    }

    public int getType () {
        return type;
    }

    private void setType ( int type ) {
        this.type = type;
    }
}
