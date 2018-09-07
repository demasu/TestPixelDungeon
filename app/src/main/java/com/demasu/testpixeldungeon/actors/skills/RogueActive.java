package com.demasu.testpixeldungeon.actors.skills;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class RogueActive extends BranchSkill {


    {
        name = "Ninjitsu";
        image = 64;
        level = 0;
    }

    @Override
    public ArrayList<String> actions ( Hero hero ) {
        ArrayList<String> actions = new ArrayList<String>();
        if ( canUpgrade() ) {
            actions.add( AC_ADVANCE );
        }
        return actions;
    }


    @Override
    public void execute ( Hero hero, String action ) {
        if ( action == Skill.AC_ADVANCE ) {
            hero.heroSkills.advance( CurrentSkills.BRANCHES.ACTIVE );
        }
    }

    @Override
    public String info () {
        return "Rogues are experts in ninja arts.\n"
                + "You have invested a total of " + totalSpent() + " points in this branch.\n"
                + ( canUpgrade() ? "Next advancement will cost you " + nextUpgradeCost() + " skill point.\n" : "You can no longer advance in this line" );
    }

    @Override
    protected int totalSpent () {
        return Dungeon.getHero().heroSkills.totalSpent( CurrentSkills.BRANCHES.ACTIVE );
    }

    @Override
    protected int nextUpgradeCost () {
        return Dungeon.getHero().heroSkills.nextUpgradeCost( CurrentSkills.BRANCHES.ACTIVE );
    }

    @Override
    protected boolean canUpgrade () {
        return Dungeon.getHero().heroSkills.canUpgrade( CurrentSkills.BRANCHES.ACTIVE );
    }
}
