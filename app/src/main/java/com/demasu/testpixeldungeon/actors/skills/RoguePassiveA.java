package com.demasu.testpixeldungeon.actors.skills;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class RoguePassiveA extends BranchSkill {


    {
        name = "Rogue";
        image = 48;
        level = 0;
    }


    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<String>();
        if (canUpgrade())
            actions.add(AC_ADVANCE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action == Skill.AC_ADVANCE)
            hero.heroSkills.advance(CurrentSkills.BRANCHES.PASSIVEA);
    }

    @Override
    public String info() {
        return "Rogues rely on stealth and quickness in combat.\n"
                + "You have invested a total of " + totalSpent() + " points in this branch.\n"
                + (canUpgrade() ? "Next advancement will cost you " + nextUpgradeCost() + " skill point.\n" : "You can no longer advance in this line");
    }

    @Override
    protected int totalSpent() {
        return Dungeon.hero.heroSkills.totalSpent(CurrentSkills.BRANCHES.PASSIVEA);
    }

    @Override
    protected int nextUpgradeCost() {
        return Dungeon.hero.heroSkills.nextUpgradeCost(CurrentSkills.BRANCHES.PASSIVEA);
    }

    @Override
    protected boolean canUpgrade() {
        return Dungeon.hero.heroSkills.canUpgrade(CurrentSkills.BRANCHES.PASSIVEA);
    }
}
