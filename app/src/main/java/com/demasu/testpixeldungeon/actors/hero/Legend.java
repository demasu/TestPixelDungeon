package com.demasu.testpixeldungeon.actors.hero;

import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.ManaRegeneration;
import com.demasu.testpixeldungeon.actors.buffs.Regeneration;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.armor.Armor;
import com.demasu.testpixeldungeon.items.armor.PlateArmor;
import com.demasu.testpixeldungeon.items.weapon.Weapon;
import com.demasu.testpixeldungeon.items.weapon.melee.Longsword;
import com.demasu.testpixeldungeon.scenes.MissionScene;
import com.watabou.utils.Bundle;

/**
 * Created by Moussa on 04-Feb-17.
 */
public class Legend extends Hero {

    {
        heroClass = HeroClass.HATSUNE;
    }


    @Override
    public boolean act() {
        super.act();

        if (MissionScene.scenePause) {
            spendAndNext(1f);
        }

        return false;
    }

    @Override
    public boolean isStarving() {
        return false;
    }

    @Override
    public void live() {
        Buff.affect(this, ManaRegeneration.class);
        Buff.affect(this, ManaRegeneration.class);
        Buff.affect(this, ManaRegeneration.class);

        Buff.affect(this, Regeneration.class);
        Buff.affect(this, Regeneration.class);

        lvl = 100;
        HP = HT = 100;
        STR = 25;
        MP = MMP = 100;
        attackSkill = 40;
        defenseSkill = 25;
        Item tmp = new Longsword().identify();
        belongings.weapon = (Weapon) tmp;
        tmp = new PlateArmor().identify();
        belongings.armor = (Armor) tmp;
    }

    @Override
    public void die(Object reason) {
        super.die(reason);
        MissionScene.scenePause = true;
    }

    @Override
    public void storeInBundle(Bundle bundle) {


    }

    @Override
    public void restoreFromBundle(Bundle bundle) {

    }
}
