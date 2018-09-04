package com.demasu.testpixeldungeon.actors.skills;


import com.watabou.noosa.tweeners.AlphaTweener;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.mobs.npcs.SummonedPet;
import com.demasu.testpixeldungeon.effects.Pushing;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.ui.StatusPane;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class SummonSkeletonArcher extends ActiveSkill3 {


    {
        name = "Summon Skeleton Archer";
        castText = "The dead shall obey!";
        tier = 3;
        image = 44;
        mana = 3;
    }


    @Override
    public ArrayList<String> actions ( Hero hero ) {
        ArrayList<String> actions = new ArrayList<String>();
        if ( level > 0 && hero.MP >= getManaCost() ) {
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
                        SummonedPet skeleton = new SummonedPet( SummonedPet.PET_TYPES.SKELETON_ARCHER );
                        skeleton.spawn( level );
                        skeleton.pos = newPos;
                        GameScene.add( skeleton );
                        Actor.addDelayed( new Pushing( skeleton, hero.pos, newPos ), -1 );
                        skeleton.sprite.alpha( 0 );
                        skeleton.sprite.parent.add( new AlphaTweener( skeleton.sprite, 1, 0.15f ) );
                    }
                }
            }

            if ( spawned ) {
                hero.MP -= getManaCost();
                StatusPane.manaDropping += getManaCost();
                castTextYell();
                hero.spend( TIME_TO_USE );
                hero.busy();
                hero.sprite.operate( hero.pos );
            }
            Dungeon.hero.heroSkills.lastUsed = this;
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
        return "Summons Skeletons for your service.\n"
                + costUpgradeInfo();
    }

}
