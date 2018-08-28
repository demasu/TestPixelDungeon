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
package com.demasu.testpixeldungeon.items.armor.glyphs;

import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Burning;
import com.demasu.testpixeldungeon.actors.buffs.Frost;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.particles.FlameParticle;
import com.demasu.testpixeldungeon.effects.particles.SnowParticle;
import com.demasu.testpixeldungeon.items.armor.Armor;
import com.demasu.testpixeldungeon.items.armor.Armor.Glyph;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.sprites.ItemSprite;
import com.demasu.testpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class AntiEntropy extends Glyph {

    private static final String TXT_ANTI_ENTROPY = "%s of anti-entropy";

    private static final ItemSprite.Glowing BLUE = new ItemSprite.Glowing(0x0000FF);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max(0, armor.effectiveLevel());

        if (Level.adjacent(attacker.pos, defender.pos) && Random.Int(level + 6) >= 5) {

            Buff.prolong(attacker, Frost.class, Frost.duration(attacker) * Random.Float(1f, 1.5f));
            CellEmitter.get(attacker.pos).start(SnowParticle.FACTORY, 0.2f, 6);

            Buff.affect(defender, Burning.class).reignite(defender);
            defender.sprite.emitter().burst(FlameParticle.FACTORY, 5);

        }

        return damage;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_ANTI_ENTROPY, weaponName);
    }

    @Override
    public Glowing glowing() {
        return BLUE;
    }
}
