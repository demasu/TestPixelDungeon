package com.demasu.testpixeldungeon.actors.skills;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.utils.GLog;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class SpiritArmor extends PassiveSkillA3 {


    {
        name = "Spirit Armor";
        tier = 3;
        image = 27;
        level = 0;
    }

    @Override
    protected boolean upgrade () {
        return true;
    }

    @Override
    public ArrayList<String> actions ( Hero hero ) {
        ArrayList<String> actions = new ArrayList<String>();
        if ( !active && level > 0 ) {
            actions.add( AC_ACTIVATE );
        } else if ( level > 0 ) {
            actions.add( AC_DEACTIVATE );
        }

        return actions;
    }

    @Override
    public void execute ( Hero hero, String action ) {
        if ( action == Skill.AC_ACTIVATE ) {
            active = true;
        } else if ( action == Skill.AC_DEACTIVATE ) {
            active = false;
        }
    }

    @Override
    public int incomingDamageReduction ( int damage ) {
        if ( !active ) {
            return 0;
        }
        int maxReduction = (int) ( damage * 0.1f * level );
        if ( maxReduction == 0 && damage > 0 ) {
            maxReduction = 1;
        }

        if ( Dungeon.getHero().getMP() > maxReduction ) {
            Dungeon.getHero().setMP( Dungeon.getHero().getMP() - maxReduction );
        } else {
            maxReduction = Dungeon.getHero().getMP();
            Dungeon.getHero().setMP( 0 );
        }

        if ( maxReduction != 0 ) {
            GLog.p( " (Spirit Armor absorbed " + maxReduction + " damage) " );
        }

        return maxReduction;
    }

    @Override
    public String info () {
        return "When activated, 10% of damage per level is taken from mana when possible.\n"
                + costUpgradeInfo();
    }

    private int damageReduction () {
        if ( level == 0 ) {
            return 10;
        }

        return level * 10;
    }
}
