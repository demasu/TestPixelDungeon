package com.demasu.testpixeldungeon.actors.skills;


import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.ui.StatusPane;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class Smash extends ActiveSkill1 {


    {
        name = "Smash";
        castText = "Smash!";
        tier = 1;
        image = 17;
        mana = 3;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action == Skill.AC_ACTIVATE) {
            hero.heroSkills.active2.active = false; // Disable Knockback
            hero.heroSkills.active3.active = false; // Disable Rampage
        }
    }


    @Override
    public int getManaCost() {
        return (int) Math.ceil(mana * (1 + 0.55 * level));
    }

    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public float damageModifier() {
        if (!active || Dungeon.hero.MP < getManaCost())
            return 1f;
        else {
            castTextYell();
            Dungeon.hero.MP -= getManaCost();
            StatusPane.manaDropping += getManaCost();
            return 1f + 0.1f * level;
        }
    }

    @Override
    public String info() {
        return "Hits target for more damage.\n"
                + costUpgradeInfo();
    }

}
