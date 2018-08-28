/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.demasu.testpixeldungeon.actors.mobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.watabou.noosa.audio.Sample;
import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.Pushing;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.items.Gold;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.sprites.MimicSprite;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Mimic extends Mob {

    private int level;

    {
        name = "mimic";
        spriteClass = MimicSprite.class;
    }

    private ArrayList<Item> items;

    private static final String LEVEL = "level";
    private static final String ITEMS = "items";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ITEMS, items);
        bundle.put(LEVEL, level);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        // Error:(67, 95) java: incompatible types: java.util.Collection<com.watabou.utils.Bundlable> cannot be converted to java.util.Collection<? extends com.demasu.testpixeldungeon.items.Item>
        //items = new ArrayList<Item>( (Collection<? extends Item>) bundle.getCollection( ITEMS ) );

        // This works
        Collection<Bundlable> tmp = bundle.getCollection(ITEMS);

        items = new ArrayList<>();

        for (Bundlable item : tmp) {
            items.add((Item) item);
        }
        adjustStats(bundle.getInt(LEVEL));
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(HT / 10, HT / 4);
    }

    @Override
    protected int attackSkill(Char target) {
        return 9 + level;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy == Dungeon.hero && Random.Int(3) == 0 && Dungeon.hero.heroSkills.passiveA1.lootBonus(100) == 0) { // <--- Rogue bandit if present
            Gold gold = new Gold(Random.Int(Dungeon.gold / 10, Dungeon.gold / 2));
            if (gold.quantity() > 0) {
                Dungeon.gold -= gold.quantity();
                Dungeon.level.drop(gold, Dungeon.hero.pos).sprite.drop();
            }
        }
        return super.attackProc(enemy, damage);
    }

    private void adjustStats(int level) {
        this.level = level;

        HT = (3 + level) * 4;
        EXP = 2 + 2 * (level - 1) / 5;
        defenseSkill = attackSkill(null) / 2;

        enemySeen = true;
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        if (items != null) {
            for (Item item : items) {
                Dungeon.level.drop(item, pos).sprite.drop();
            }
        }
    }

    @Override
    public boolean reset() {
        state = WANDERING;
        return true;
    }

    @Override
    public String description() {
        return
                "Mimics are magical creatures which can take any shape they wish. In dungeons they almost always " +
                        "choose a shape of a treasure chest, because they know how to beckon an adventurer.";
    }

    public static Mimic spawnAt(int pos, List<Item> items) {
        Char ch = Actor.findChar(pos);
        if (ch != null) {
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int n : Level.NEIGHBOURS8) {
                int cell = pos + n;
                if ((Level.passable[cell] || Level.avoid[cell]) && Actor.findChar(cell) == null) {
                    candidates.add(cell);
                }
            }
            if (candidates.size() > 0) {
                @SuppressWarnings("ConstantConditions") int newPos = Random.element(candidates);
                Actor.addDelayed(new Pushing(ch, ch.pos, newPos), -1);

                ch.pos = newPos;
                // FIXME
                if (ch instanceof Mob) {
                    Dungeon.level.mobPress((Mob) ch);
                } else {
                    Dungeon.level.press(newPos, ch);
                }
            } else {
                return null;
            }
        }

        Mimic m = new Mimic();
        m.items = new ArrayList<>(items);
        m.adjustStats(Dungeon.depth);
        m.HP = m.HT;
        m.pos = pos;
        m.state = m.HUNTING;
        GameScene.add(m, 1);

        m.sprite.turnTo(pos, Dungeon.hero.pos);

        if (Dungeon.visible[m.pos]) {
            CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
            Sample.INSTANCE.play(Assets.SND_MIMIC);
        }

        return m;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(ScrollOfPsionicBlast.class);
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
