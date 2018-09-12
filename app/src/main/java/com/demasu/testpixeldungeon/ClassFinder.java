package com.demasu.testpixeldungeon;

public class ClassFinder {
    private static final String[] searchPackages = {
        "com.demasu.testpixeldungeon",
        "com.demasu.testpixeldungeon.actors",
        "com.demasu.testpixeldungeon.actors.blobs",
        "com.demasu.testpixeldungeon.actors.buffs",
        "com.demasu.testpixeldungeon.actors.hero",
        "com.demasu.testpixeldungeon.actors.mobs",
        "com.demasu.testpixeldungeon.actors.mobs.npcs",
        "com.demasu.testpixeldungeon.actors.skills",
        "com.demasu.testpixeldungeon.effects",
        "com.demasu.testpixeldungeon.effects.particles",
        "com.demasu.testpixeldungeon.items",
        "com.demasu.testpixeldungeon.items.armor",
        "com.demasu.testpixeldungeon.items.armor.glyphs",
        "com.demasu.testpixeldungeon.items.bags",
        "com.demasu.testpixeldungeon.items.food",
        "com.demasu.testpixeldungeon.items.keys",
        "com.demasu.testpixeldungeon.items.potions",
        "com.demasu.testpixeldungeon.items.quest",
        "com.demasu.testpixeldungeon.items.ring",
        "com.demasu.testpixeldungeon.items.scrolls",
        "com.demasu.testpixeldungeon.items.wands",
        "com.demasu.testpixeldungeon.items.weapon",
        "com.demasu.testpixeldungeon.items.weapon.enchantments",
        "com.demasu.testpixeldungeon.items.weapon.melee",
        "com.demasu.testpixeldungeon.items.weapon.missiles",
        "com.demasu.testpixeldungeon.levels.Campaigns",
        "com.demasu.testpixeldungeon.levels.features",
        "com.demasu.testpixeldungeon.levels.painters",
        "com.demasu.testpixeldungeon.levels.traps",
        "com.demasu.testpixeldungeon.mechanics",
        "com.demasu.testpixeldungeon.plants",
        "com.demasu.testpixeldungeon.scenes",
        "com.demasu.testpixeldungeon.sprites",
        "com.demasu.testpixeldungeon.ui",
        "com.demasu.testpixeldungeon.utils",
        "com.demasu.testpixeldungeon.windows",
        "com.watabou.glscripts",
        "com.watabou.gltextures",
        "com.watabou.glwrap",
        "com.watabou.input",
        "com.watabou.noosa",
        "com.watabou.noosa.audio",
        "com.watabou.noosa.particles",
        "com.watabou.noosa.tweeners",
        "com.watabou.noosa.ui"
    };

    public Class<?> findClassByName ( String name ) throws ClassNotFoundException {
        for ( String path : searchPackages ) {
            try {
                return Class.forName( path + "." + name );
            } catch ( ClassNotFoundException e ) {
                // Ignore it
            } catch ( Exception e ) {
                // Undecided if I want to do anything here
            }
        }

        throw new ClassNotFoundException();
    }
}
