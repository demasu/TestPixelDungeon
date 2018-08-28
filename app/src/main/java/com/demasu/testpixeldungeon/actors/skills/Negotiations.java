package com.demasu.testpixeldungeon.actors.skills;

import com.watabou.noosa.tweeners.AlphaTweener;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.hero.HeroClass;
import com.demasu.testpixeldungeon.actors.mobs.npcs.HiredMerc;
import com.demasu.testpixeldungeon.effects.Pushing;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.sprites.MercSprite;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class Negotiations extends BranchSkill { // Not actually a skill but best way to do it


    public static final String TXT_HIRE_BRUTE = "Brute";
    public static final String TXT_HIRE_THIEF = "Thief";
    public static final String TXT_HIRE_WIZARD = "Wizard";
    public static final String TXT_HIRE_ARCHER = "Archer";
    public static final String TXT_HIRE_ARCHER_MAIDEN = "Archer-Maiden";


    {
        name = "Hire A Mercenary";
        image = 96;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<>();
        if (hero.hiredMerc == null) {
            if (hero.heroClass != HeroClass.WARRIOR)
                actions.add(TXT_HIRE_BRUTE);
            if (hero.heroClass != HeroClass.ROGUE)
                actions.add(TXT_HIRE_THIEF);
            if (hero.heroClass != HeroClass.MAGE)
                actions.add(TXT_HIRE_WIZARD);
            if (hero.heroClass != HeroClass.HUNTRESS)
                actions.add(TXT_HIRE_ARCHER);

            if (HiredMerc.archerMaidenUnlocked)
                actions.add(TXT_HIRE_ARCHER_MAIDEN);
        }
        return actions;
    }


    @Override
    public void execute(Hero hero, String action) {
        if (action == TXT_HIRE_BRUTE || action == TXT_HIRE_THIEF || action == TXT_HIRE_WIZARD || action == TXT_HIRE_ARCHER || action == TXT_HIRE_ARCHER_MAIDEN) {
            if (Dungeon.gold < getGoldCost()) {
                GLog.n("You cannot afford a merc.");
                return;
            }

            boolean spawned = false;
            for (int nu = 0; nu < 1; nu++) {
                int newPos = hero.pos;
                if (Actor.findChar(newPos) != null) {
                    ArrayList<Integer> candidates = new ArrayList<>();
                    boolean[] passable = Level.passable;

                    for (int n : Level.NEIGHBOURS4) {
                        int c = hero.pos + n;
                        if (passable[c] && Actor.findChar(c) == null) {
                            candidates.add(c);
                        }
                    }
                    newPos = candidates.size() > 0 ? Random.element(candidates) : -1;
                    if (newPos != -1) {
                        switch (action) {
                            case TXT_HIRE_ARCHER_MAIDEN:
                                hero.hiredMerc = new HiredMerc(HiredMerc.MERC_TYPES.ArcherMaiden);
                                break;
                            case TXT_HIRE_ARCHER:
                                hero.hiredMerc = new HiredMerc(HiredMerc.MERC_TYPES.Archer);
                                break;
                            default:
                                hero.hiredMerc = action == TXT_HIRE_BRUTE ? new HiredMerc(HiredMerc.MERC_TYPES.Brute) : (action == TXT_HIRE_THIEF ? new HiredMerc(HiredMerc.MERC_TYPES.Thief) : new HiredMerc(HiredMerc.MERC_TYPES.Wizard));
                                break;
                        }
                        hero.hiredMerc.spawn(Dungeon.hero.lvl);
                        hero.hiredMerc.pos = newPos;
                        GameScene.add(hero.hiredMerc);
                        Actor.addDelayed(new Pushing(hero.hiredMerc, hero.pos, newPos), -1);
                        hero.hiredMerc.sprite.alpha(0);
                        hero.hiredMerc.sprite.parent.add(new AlphaTweener(hero.hiredMerc.sprite, 1, 0.15f));
                        ((MercSprite) hero.hiredMerc.sprite).updateArmor();
                    }
                }
            }

            if (spawned) {
                Dungeon.gold -= getGoldCost();
                GLog.p(" " + action + " hired for " + getGoldCost() + " gold! ");
            }
        }
    }

    public void restoreMerc(Hero hero) {
        if (hero.hiredMerc != null) {
            int newPos = hero.pos;

            if (newPos != -1) {
                HiredMerc tmp = new HiredMerc(hero.hiredMerc.mercType);
                tmp.spawn(Dungeon.hero.lvl);
                tmp.pos = newPos;
                GameScene.add(tmp);
                Actor.addDelayed(new Pushing(tmp, hero.pos, newPos), -1);
                tmp.sprite.alpha(0);
                tmp.sprite.parent.add(new AlphaTweener(tmp.sprite, 1, 0.15f));
                tmp.weapon = hero.hiredMerc.weapon;
                tmp.armor = hero.hiredMerc.armor;
                ((MercSprite) tmp.sprite).updateArmor();

                hero.hiredMerc = tmp;

            }
        }
    }

    public int getGoldCost() {
        return 0;
    }

    public String getHireText() {
        return "\nHiring a level " + Dungeon.hero.lvl + " merc costs " + getGoldCost() + " gold.";
    }

    @Override
    public String info() {
        return HiredMerc.MERC_TYPES.Brute.getDescription() + "\n" + HiredMerc.MERC_TYPES.Thief.getDescription() + "\n" + HiredMerc.MERC_TYPES.Wizard.getDescription() + (Dungeon.hero.hiredMerc == null ? getHireText() : "");
    }
}
