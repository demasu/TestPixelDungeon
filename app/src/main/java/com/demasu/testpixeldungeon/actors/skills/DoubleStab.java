package com.demasu.testpixeldungeon.actors.skills;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.ui.StatusPane;


public class DoubleStab extends ActiveSkill2 {

    {
        name = "Double Stab";
        castText = "Got 'em'";
        image = 90;
        tier = 2;
        mana = 5;
    }

    private boolean onDouble = false; // prevent infinite loop

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action == Skill.AC_ACTIVATE) {
            hero.heroSkills.active3.active = false; // Disable Bombvoyage
        }
    }

    @Override
    public boolean doubleStab() {
        if (!active || Dungeon.hero.MP < getManaCost())
            return false;
        else if (!onDouble) {
            onDouble = true;
            castTextYell();
            Dungeon.hero.MP -= getManaCost();
            StatusPane.manaDropping += getManaCost();
            return true;
        }
        onDouble = false;
        return false;
    }

    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public String info() {
        return "Stabs: two for one.\n"
                + costUpgradeInfo();
    }
}
