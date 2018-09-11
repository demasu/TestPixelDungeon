package com.demasu.testpixeldungeon.actors.skills;


import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.ui.StatusPane;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class Smite extends Smash {


    {
        name = "Smite";
        castText = "Smite!";
        tier = 1;
        image = 20;
        mana = 3;
    }

    @Override
    public void execute ( Hero hero, String action ) {
        super.execute( hero, action );
        if ( action == Skill.AC_ACTIVATE ) {
            hero.heroSkills.active2.active = false; // Disable Knockback
            hero.heroSkills.active3.active = false; // Disable Rampage
        }
    }


    @Override
    public int getManaCost () {
        return (int) Math.ceil( mana * ( 1 + 0.55 * level ) );
    }

    @Override
    protected boolean upgrade () {
        return true;
    }


    @Override
    public float damageModifier () {
        if ( !active || Dungeon.getHero().getMP() < getManaCost() ) {
            return 1f;
        } else {
            castTextYell();
            Dungeon.getHero().setMP( Dungeon.getHero().getMP() - getManaCost() );
            StatusPane.manaDropping += getManaCost();
            return 1f + 0.2f * level;
        }
    }

    @Override
    public String info () {
        return "Hits target for even more damage.\n"
                + costUpgradeInfo();
    }

}
