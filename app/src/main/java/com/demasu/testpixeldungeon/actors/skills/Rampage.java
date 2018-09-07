package com.demasu.testpixeldungeon.actors.skills;


import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.ui.StatusPane;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class Rampage extends ActiveSkill3 {


    {
        name = "Rampage";
        castText = "Rampage!";
        tier = 3;
        image = 19;
        mana = 5;
    }

    @Override
    public float damageModifier () {
        if ( !active || Dungeon.getHero().MP < getManaCost() ) {
            return 1f;
        } else {
            return 0.4f + 0.2f * level;
        }
    }

    @Override
    public boolean AoEDamage () {
        if ( !active || Dungeon.getHero().MP < getManaCost() ) {
            return false;
        } else {
            castTextYell();
            Dungeon.getHero().MP -= getManaCost();
            StatusPane.manaDropping += getManaCost();
            return true;
        }
    }


    @Override
    public void execute ( Hero hero, String action ) {
        super.execute( hero, action );
        if ( action == Skill.AC_ACTIVATE ) {
            hero.heroSkills.active1.active = false; // Disable Smash
            hero.heroSkills.active2.active = false; // Disable Knockback
        }
    }

    @Override
    public int getManaCost () {
        return (int) Math.ceil( mana * ( 1 + 1 * level ) );
    }

    @Override
    protected boolean upgrade () {
        return true;
    }


    @Override
    public String info () {
        return "Less damage but hits all enemies around you.\n"
                + costUpgradeInfo();
    }

}
