package com.demasu.testpixeldungeon.actors.skills;


import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.mobs.npcs.SummonedPet;
import com.demasu.testpixeldungeon.effects.Pushing;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.ui.StatusPane;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class SummonRat extends ActiveSkill1 {


    {
        name = "Summon Rat";
        castText = "Rise Rodent!";
        tier = 1;
        image = 41;
        mana = 3;
    }

    @Override
    public ArrayList<String> actions ( Hero hero ) {
        ArrayList<String> actions = new ArrayList<String>();
        if ( level > 0 && hero.getMP() >= getManaCost() ) {
            actions.add( AC_SUMMON );
        }
        return actions;
    }

    @Override
    public void execute ( Hero hero, String action ) {
        if ( action == Skill.AC_SUMMON ) {
            boolean spawned = false;
            for ( int nu = 0; nu < 1; nu++ ) {
                int newPos = hero.pos;
                if ( Actor.findChar( newPos ) != null ) {
                    ArrayList<Integer> candidates = new ArrayList<Integer>();
                    boolean[] passable = Level.passable;

                    for ( int n : Level.NEIGHBOURS4 ) {
                        int c = hero.pos + n;
                        if ( c < 0 || c >= Level.passable.length ) {
                            continue;
                        }
                        if ( passable[c] && Actor.findChar( c ) == null ) {
                            candidates.add( c );
                        }
                    }
                    newPos = candidates.size() > 0 ? Random.element( candidates ) : -1;
                    if ( newPos != -1 ) {
                        spawned = true;
                        SummonedPet rat = new SummonedPet( SummonedPet.PET_TYPES.RAT );
                        rat.spawn( level );
                        rat.pos = newPos;
                        GameScene.add( rat );
                        Actor.addDelayed( new Pushing( rat, hero.pos, newPos ), -1 );
                        rat.sprite.alpha( 0 );
                        rat.sprite.getParent().add( new AlphaTweener( rat.sprite, 1, 0.15f ) );
                    }
                }
            }

            if ( spawned ) {
                hero.setMP( hero.getMP() - getManaCost() );
                StatusPane.manaDropping += getManaCost();
                castTextYell();
                hero.spend( TIME_TO_USE );
                hero.busy();
                hero.sprite.operate( hero.pos );
            }
            Dungeon.getHero().heroSkills.lastUsed = this;
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
    public String info () {
        return "Summons rats for your service.\n"
                + costUpgradeInfo();
    }

}
