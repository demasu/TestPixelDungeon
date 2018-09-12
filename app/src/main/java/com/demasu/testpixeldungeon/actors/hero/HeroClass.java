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
package com.demasu.testpixeldungeon.actors.hero;

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Badges;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.skills.CurrentSkills;
import com.demasu.testpixeldungeon.actors.skills.Skill;
import com.demasu.testpixeldungeon.items.Ankh;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.KindOfWeapon;
import com.demasu.testpixeldungeon.items.MerchantsBeacon;
import com.demasu.testpixeldungeon.items.TomeOfMastery;
import com.demasu.testpixeldungeon.items.armor.Armor;
import com.demasu.testpixeldungeon.items.armor.DebugArmor;
import com.demasu.testpixeldungeon.items.bags.PotionBelt;
import com.demasu.testpixeldungeon.items.bags.ScrollHolder;
import com.demasu.testpixeldungeon.items.bags.SeedPouch;
import com.demasu.testpixeldungeon.items.bags.WandHolster;
import com.demasu.testpixeldungeon.items.food.Food;
import com.demasu.testpixeldungeon.items.potions.Potion;
import com.demasu.testpixeldungeon.items.potions.PotionOfHealing;
import com.demasu.testpixeldungeon.items.potions.PotionOfStrength;
import com.demasu.testpixeldungeon.items.rings.Ring;
import com.demasu.testpixeldungeon.items.rings.RingOfShadows;
import com.demasu.testpixeldungeon.items.scrolls.Scroll;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfSkill;
import com.demasu.testpixeldungeon.items.wands.WandOfMagicMissile;
import com.demasu.testpixeldungeon.items.weapon.melee.SwordOfDebug;
import com.demasu.testpixeldungeon.items.weapon.missiles.Arrow;
import com.demasu.testpixeldungeon.items.weapon.missiles.Boomerang;
import com.demasu.testpixeldungeon.items.weapon.missiles.Shuriken;
import com.demasu.testpixeldungeon.ui.QuickSlot;
import com.watabou.utils.Bundle;

public enum HeroClass {

    WARRIOR( "warrior" ), MAGE( "mage" ), ROGUE( "rogue" ), HUNTRESS( "huntress" ), HATSUNE( "hatsune" );

    public static final String[] WAR_PERKS = {
            "Warriors start with 11 points of Strength.",
            "Warriors start with a unique short sword. This sword can be later \"reforged\" to upgrade another melee weapon.",
            "Warriors are less proficient with missile weapons.",
            "Any piece of food restores some health when eaten.",
            "Potions of Strength are identified from the beginning.",
    };
    public static final String[] MAG_PERKS = {
            "Mages start with a unique Wand of Magic Missile. This wand can be later \"disenchanted\" to upgrade another wand.",
            "Mages recharge their wands faster.",
            "When eaten, any piece of food restores 1 charge for all wands in the inventory.",
            "Mages can use wands as a melee weapon.",
            "Scrolls of Identify are identified from the beginning.",
            "Master of magic."
    };
    public static final String[] ROG_PERKS = {
            "Rogues start with a Ring of Shadows+1.",
            "Rogues identify a type of a ring on equipping it.",
            "Rogues are proficient with light armor, dodging better while wearing one.",
            "Rogues are proficient in detecting hidden doors and traps.",
            "Rogues can go without food longer.",
            "Scrolls of Magic Mapping are identified from the beginning."
    };
    public static final String[] HUN_PERKS = {
            "Huntresses start with 15 points of Health.",
            "Huntresses start with a unique upgradeable boomerang.",
            "Huntresses are proficient with missile weapons and get a damage bonus for excessive strength when using them.",
            "Huntresses gain more health from dewdrops.",
            "Huntresses sense neighbouring monsters even if they are hidden behind obstacles."
    };
    public static final String[] LEGEND_PERKS = {
            "Hatsune is believed to be a descendant of an Avatar who broke the rules and interacted with mortals.",
            "She is best known for leading the failed defence of the town of Boonamai.",
            "She is the first to give birth to twin daughters instead of one. A first in a lineage of over 10 generations.",
            "She excels in tactics and has mastered both light and dark arts.",
            "Her hair has turned blue from her massive spiritual strength."
    };
    private static final int FOOD = 300;
    private static final String CLASS = "class";
    private String title;

    HeroClass ( String title ) {
        this.title = title;
    }

    private static void initCommon ( Hero hero ) {
        initStarterStats();
        getStarterItems( hero );
    }

    public static void initStarterStats () {
        Dungeon.initStartingStats();

        Skill.availableSkill = Skill.STARTING_SKILL;
    }

    public static void getStarterItems ( Hero hero ) {
        collectStarterItems( hero );
        Scroll.collectStarterScrolls();
        Potion.collectStarterPotions();
        collectDebugScrolls();      // For debugging
        collectDebugPotions();      // For debugging
        collectDebugItems();        // For debugging
        collectDebugWeapon( hero ); // For debugging
    }

    public static void collectStarterItems ( Hero hero ) {
        collectArmor( hero );
        collectFood();
        Item.collectStarterItems( hero );
    }

    @SuppressWarnings ( "FeatureEnvy" ) // Suppressed until this becomes the non-debug one
    public static void collectFood () {
        new Food().identify().collect();
        for ( int i = 1; i <= FOOD; i++ ) { // Default is 1 food
            new Food().collect();
        }
    }

    @SuppressWarnings ( "FeatureEnvy" )
    public static void collectDebugArmor ( Hero hero ) {
        // Method for debugging only
        // Remove when done debugging
        Armor armor = (Armor) new DebugArmor().identify();
        final int LVL = 15;
        for ( int i = 1; i <= LVL; i++ ) {
            armor.upgrade();
        }

        hero.belongings.armor = armor;
    }

    @SuppressWarnings ( "FeatureEnvy" )
    public static void collectDebugWeapon ( Hero hero ) {
        // Method for debugging only
        // Remove when done debugging
        KindOfWeapon sword = (KindOfWeapon) new SwordOfDebug().identify();
        final int LVL = 15;
        for ( int i = 1; i <= LVL; i++ ) {
            sword.upgrade();
        }

        hero.belongings.weapon = sword; // For debug
    }

    @SuppressWarnings ( "FeatureEnvy" )
    public static void collectDebugItems () {
        // Method for debugging only
        // Remove when done debugging
        new Ankh().collect();
        new ScrollHolder().collect();
        new SeedPouch().collect();
        new WandHolster().collect();
        new MerchantsBeacon().collect();
        new PotionBelt().collect();
    }

    public static void collectDebugScrolls () {
        // Method for debugging only
        // Remove when done debugging
        collectIdentifyScrolls();
        collectSkillScrolls();
    }

    @SuppressWarnings ( "FeatureEnvy" )
    public static void collectDebugPotions () {
        // Method for debugging only
        // Remove when done debugging
        final int NUMPOH = 50;
        for ( int i = 1; i <= NUMPOH; i++ ) {
            new PotionOfHealing().collect();
        }
    }

    @SuppressWarnings ( "FeatureEnvy" )
    public static void collectSkillScrolls () {
        final int NUMSKILLSCROLLS = 50; // There was 1 before
        for ( int i = 1; i <= NUMSKILLSCROLLS; i++ ) {
            new ScrollOfSkill().collect();
        }
    }

    @SuppressWarnings ( "FeatureEnvy" )
    public static void collectIdentifyScrolls () {
        // There were no identify scrolls before debugging
        // Remove when done debugging
        final int NUMIDENTSCROLLS = 100;
        for ( int i = 1; i <= NUMIDENTSCROLLS; i++ ) {
            new ScrollOfIdentify().collect();
        }
    }

    public static HeroClass restoreInBundle ( Bundle bundle ) {
        String value = bundle.getString( CLASS );
        return value.length() > 0 ? valueOf( value ) : ROGUE;
    }

    public void initHero ( Hero hero ) {

        hero.setHeroClass( this );

        initCommon( hero );

        switch ( this ) {
            case WARRIOR:
                initWarrior( hero );
                break;

            case MAGE:
                initMage( hero );
                break;

            case ROGUE:
                initRogue( hero );
                break;

            case HUNTRESS:
                initHuntress( hero );
                break;
        }

        if ( Badges.isUnlocked( masteryBadge() ) ) {
            new TomeOfMastery().collect();
        }

        hero.updateAwareness();
    }

    public static void collectArmor ( Hero hero ) {
        //hero.belongings.armor = (Armor) new ClothArmor().identify();
        collectDebugArmor( hero ); // For debug
    }

    public void equipStarterWeapon ( Hero hero ) {
        // Actions commented out for debugging
        switch ( this ) {
            case WARRIOR:
                //hero.belongings.weapon = (KindOfWeapon) new ShortSword().identify();
                //break;
            case MAGE:
                //hero.belongings.weapon = (KindOfWeapon) new Knuckles().identify();
                //break;
            case ROGUE:
                //hero.belongings.weapon = (KindOfWeapon) new DualSwords().identify();
                //break;
            case HUNTRESS:
                //hero.belongings.weapon = (KindOfWeapon) new Dagger().identify();
                //break;
            default:
                collectDebugWeapon( hero ); // For debug
                //hero.belongings.weapon = (KindOfWeapon) new Dagger().degrade().degrade().identify();
        }
    }

    public Badges.Badge masteryBadge () {
        switch ( this ) {
            case WARRIOR:
                return Badges.Badge.MASTERY_WARRIOR;
            case MAGE:
                return Badges.Badge.MASTERY_MAGE;
            case ROGUE:
                return Badges.Badge.MASTERY_ROGUE;
            case HUNTRESS:
                return Badges.Badge.MASTERY_HUNTRESS;
        }
        return null;
    }

    @SuppressWarnings ( "FeatureEnvy" )
    private void initWarrior ( Hero hero ) {
        final int WARMP = 200; //Originally 20
        hero.setSTR( hero.getSTR() + 1 );
        hero.setMP( WARMP );
        hero.setMMP( WARMP );
        //new Dart( 8 ).identify().collect();                                   // For debug
        equipStarterWeapon( hero );

        //QuickSlot.primaryValue = Dart.class;
        QuickSlot.setPrimaryValue( Arrow.class );

        new PotionOfStrength().setKnown();
        new PotionOfStrength().collect();
        new PotionOfStrength().collect();
        new PotionOfStrength().collect();

        //new NecroBlade().identify().collect(); //For debug

        hero.heroSkills = CurrentSkills.WARRIOR;
        hero.heroSkills.init();
    }

    private void initMage ( Hero hero ) {
        final int MAGEMP = 40;
        hero.setMP( MAGEMP );
        hero.setMMP( MAGEMP );

        //( hero.belongings.weapon = new Knuckles() ).identify();
        equipStarterWeapon( hero );

        WandOfMagicMissile wand = new WandOfMagicMissile();
        wand.identify().collect();

        QuickSlot.setPrimaryValue( wand );

        new ScrollOfIdentify().setKnown();

        hero.heroSkills = CurrentSkills.MAGE;
        hero.heroSkills.init();
    }

    @SuppressWarnings ( "FeatureEnvy" )
    private void initRogue ( Hero hero ) {
        final int ROGUEMP = 30;
        final int SHURIKEN = 100; // Originally was 10
        hero.setMP( ROGUEMP );
        hero.setMMP( ROGUEMP );
        //(hero.belongings.weapon = new Dagger()).identify();
        //( hero.belongings.weapon = new DualSwords() ).identify();
        equipStarterWeapon( hero );
        hero.belongings.ring1 = (Ring) new RingOfShadows().upgrade().identify();
        //new Dart( 8 ).identify().collect();
        new Shuriken( SHURIKEN ).identify().collect();
        hero.belongings.ring1.activate( hero );

        //QuickSlot.primaryValue = Dart.class;
        QuickSlot.setPrimaryValue( Shuriken.class );

        new ScrollOfMagicMapping().setKnown();

        hero.heroSkills = CurrentSkills.ROGUE;
        hero.heroSkills.init();
    }

    @SuppressWarnings ( "FeatureEnvy" )
    private void initHuntress ( Hero hero ) {
        final int HUNTMP                  = 35;
        final int HUNTRESS_HEALTH_PENALTY = 5;
        hero.setMP( HUNTMP );
        hero.setMMP( HUNTMP );
        hero.setHP( hero.getHP() - HUNTRESS_HEALTH_PENALTY );
        hero.setHT( hero.getHT() - HUNTRESS_HEALTH_PENALTY );

        //( hero.belongings.weapon = new Dagger() ).identify();
        equipStarterWeapon( hero );
        Boomerang boomerang = (Boomerang) new Boomerang().identify();
        final int LVL = 15; //Originally none
        for ( int i = 1; i <= LVL; i++ ) {
            boomerang.upgrade();
        }
        boomerang.collect();

        QuickSlot.setPrimaryValue( boomerang );

        hero.heroSkills = CurrentSkills.HUNTRESS;
        hero.heroSkills.init();
    }

    public String title () {
        return title;
    }

    public String spritesheet () {

        switch ( this ) {
            case WARRIOR:
                return Assets.WARRIOR;
            case MAGE:
                return Assets.MAGE;
            case ROGUE:
                return Assets.ROGUE;
            case HUNTRESS:
                return Assets.HUNTRESS;
            case HATSUNE:
                return Assets.LEGEND;
        }

        return null;
    }

    @SuppressWarnings ( "FeatureEnvy" )
    public String[] perks () {

        switch ( this ) {
            case WARRIOR:
                return WAR_PERKS.clone();
            case MAGE:
                return MAG_PERKS.clone();
            case ROGUE:
                return ROG_PERKS.clone();
            case HUNTRESS:
                return HUN_PERKS.clone();
            case HATSUNE:
                return LEGEND_PERKS.clone();
        }

        return null;
    }

    public void storeInBundle ( Bundle bundle ) {
        bundle.put( CLASS, toString() );
    }
}
