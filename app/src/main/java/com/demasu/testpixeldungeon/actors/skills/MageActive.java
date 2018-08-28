package com.demasu.testpixeldungeon.actors.skills;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class MageActive extends BranchSkill {


    {
        name = "Summoning";
        image = 40;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<>();
        if (canUpgrade())
            actions.add(AC_ADVANCE);
        return actions;
    }


    @Override
    public void execute(Hero hero, String action) {
        if (Objects.equals(action, Skill.AC_ADVANCE))
            hero.heroSkills.advance(CurrentSkills.BRANCHES.ACTIVE);
    }

    @Override
    public String info() {
        return "Mages rely on summoned creatures to do their bidding.\n"
                + "Limited to 3  (+" + Dungeon.hero.heroSkills.passiveB3.summoningLimitBonus() + " bonus from the Summoner skill) active summons\n"
                + "You have invested a total of " + totalSpent() + " points in this branch.\n"
                + (canUpgrade() ? "Next advancement will cost you " + nextUpgradeCost() + " skill point.\n" : "You can no longer advance in this line");
    }

    @Override
    protected int totalSpent() {
        return Dungeon.hero.heroSkills.totalSpent(CurrentSkills.BRANCHES.ACTIVE);
    }

    @Override
    protected int nextUpgradeCost() {
        return Dungeon.hero.heroSkills.nextUpgradeCost(CurrentSkills.BRANCHES.ACTIVE);
    }

    @Override
    protected boolean canUpgrade() {
        return Dungeon.hero.heroSkills.canUpgrade(CurrentSkills.BRANCHES.ACTIVE);
    }
}
