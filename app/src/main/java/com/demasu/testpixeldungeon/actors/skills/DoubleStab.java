package com.demasu.testpixeldungeon.actors.skills;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.ui.StatusPane;


public class DoubleStab extends ActiveSkill2 {

    private boolean onDouble = false; // prevent infinite loop

    {
        name = "Double Stab";
        castText = "Got 'em'";
        image = 90;
        tier = 2;
        mana = 5;
    }

    @Override
    public void execute ( Hero hero, String action ) {
        super.execute( hero, action );
        if ( action == Skill.AC_ACTIVATE ) {
            hero.heroSkills.active3.active = false; // Disable Bombvoyage
        }
    }

    @Override
    public boolean doubleStab () {
        if ( !active || Dungeon.getHero().getMP() < getManaCost() ) {
            return false;
        } else if ( !onDouble ) {
            onDouble = true;
            castTextYell();
            Dungeon.getHero().setMP( Dungeon.getHero().getMP() - getManaCost() );
            StatusPane.manaDropping += getManaCost();
            return true;
        }
        onDouble = false;
        return false;
    }

    @Override
    protected boolean upgrade () {
        return true;
    }


    @Override
    public String info () {
        return "Stabs: two for one.\n"
                + costUpgradeInfo();
    }
}
