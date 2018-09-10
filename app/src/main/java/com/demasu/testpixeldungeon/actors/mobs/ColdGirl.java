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

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Amok;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Burning;
import com.demasu.testpixeldungeon.actors.buffs.Frost;
import com.demasu.testpixeldungeon.actors.buffs.Poison;
import com.demasu.testpixeldungeon.actors.buffs.Sleep;
import com.demasu.testpixeldungeon.actors.buffs.Terror;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.mobs.npcs.HiredMerc;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.Pushing;
import com.demasu.testpixeldungeon.effects.particles.ShadowParticle;
import com.demasu.testpixeldungeon.items.armor.glyphs.Affection;
import com.demasu.testpixeldungeon.items.wands.Wand;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.scenes.InterlevelScene;
import com.demasu.testpixeldungeon.sprites.CharSprite;
import com.demasu.testpixeldungeon.sprites.ColdGirlSprite;
import com.demasu.testpixeldungeon.sprites.MissileSprite;
import com.demasu.testpixeldungeon.utils.GLog;
import com.demasu.testpixeldungeon.utils.Utils;
import com.demasu.testpixeldungeon.windows.PersistentWndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class ColdGirl extends Mob {

    public static final String TXT_DEATH = "Killed in the ice cave";
    public static final int PASSIVE = 0;
    public static final int HUNTING = 1;
    public static final int SUPER_HUNTING = 2;
    public static final int GOD_MODE = 3;
    public static final int DONE_MODE = 4;
    public static final int DISCUSSION_STEP = 10;
    public static final int DISCUSSION_DEAD = 1000;
    public static final int FROST_DEPTH = 1000;
    private static final String TXT_SMB_MISSED = "%s %s %s's attack";
    private static final String AI_STATE = "aistate";
    private static final String CAME_FROM = "camefrom";
    private static final String CAME_FROM_POS = "cameformpos";
    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    public static int cameFrom = 1;
    public static int cameFromPos = 1;

    static {

    }

    static {
        IMMUNITIES.add( Frost.class );
        IMMUNITIES.add( Amok.class );
        IMMUNITIES.add( Sleep.class );
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Burning.class );
        IMMUNITIES.add( Affection.class );
        IMMUNITIES.add( Poison.class );
    }

    public boolean isSister = false;
    public int discussionProgress = 0;
    public boolean firstSwap = true;
    public boolean firstDamage = true;
    public boolean firstComplaint = true;
    public boolean firstTroll = true;
    public boolean firstFetch = true;
    public int skillCharge = 5;

    {
        name = "Cold Girl";
        spriteClass = ColdGirlSprite.class;

        HP = HT = 30;
        EXP = 20;
        defenseSkill = 1;
        baseSpeed = 3;
        hostile = false;
        state = new ColdGirlAI();
        champ = 1;
    }

    public void turnToSis () {
        isSister = true;
        ( (ColdGirlSprite) sprite ).turnToSis();
    }

    @Override
    public int damageRoll () {
        return Random.NormalIntRange( 8, 15 );
    }

    @Override
    public int attackSkill ( Char target ) {
        return 28;
    }

    @Override
    public int dr () {
        return 10;
    }

    @Override
    public boolean act () {

        return super.act();
    }

    @Override
    public void move ( int step ) {
        super.move( step );


    }

    @Override
    public int attackProc ( Char enemy, int damage ) {

        if ( Level.adjacent( pos, enemy.pos ) && damage < HP )  // Curse
        {
            if ( firstDamage ) {
                speak( "I have to feel your pain too?!" );
                firstDamage = false;
            }

            damage( damage, this );
        }

        if ( damage < enemy.HP && Random.Int( 5 ) < 2 && ( ( (ColdGirlAI) ColdGirl.this.state ).aiStatus == SUPER_HUNTING ) && Level.adjacent( pos, enemy.pos ) ) {
            ArrayList<Integer> skelSpawns = new ArrayList<>();
            for ( int i = 0; i < Level.NEIGHBOURS8.length; i++ ) {
                int ofs = Level.NEIGHBOURS8[i];
                if ( pos - enemy.pos == ofs ) {
                    int maxTrollPush = 2;
                    int actualPush = ofs;
                    while ( maxTrollPush > 0 && enemy.pos - actualPush > 0 && enemy.pos - actualPush < Level.passable.length && ( ( Level.passable[enemy.pos - actualPush] || Level.avoid[enemy.pos - actualPush] ) && Actor.findChar( enemy.pos - actualPush ) == null ) ) {
                        skelSpawns.add( enemy.pos - actualPush );
                        actualPush += ofs;
                        maxTrollPush--;
                    }

                    int newPos = enemy.pos - actualPush;
                    if ( ( Level.passable[newPos] || Level.avoid[newPos] ) && Actor.findChar( newPos ) == null ) {

                        Actor.addDelayed( new Pushing( enemy, enemy.pos, newPos ), -1 );

                        enemy.pos = newPos;
                        // FIXME
                        if ( enemy instanceof Mob ) {
                            Dungeon.getLevel().mobPress( (Mob) enemy );
                        } else {
                            Dungeon.getLevel().press( newPos, enemy );
                        }

                        enemy.sprite.bloodBurstA( sprite.center(), enemy.HP );
                    }

                    for ( int s = 0; s < skelSpawns.size(); s++ ) {
                        ColdGirlSkel slave = new ColdGirlSkel();
                        slave.pos = skelSpawns.get( s );
                        Sample.INSTANCE.play( Assets.SND_GHOST );

                        GameScene.add( slave );
                        Actor.addDelayed( new Pushing( slave, pos, slave.pos ), -1 );
                        slave.sprite.emitter().burst( ShadowParticle.CURSE, 5 );
                    }

                    if ( skelSpawns.size() > 0 ) {
                        speak( "This is you once I am done" );
                    }
                    Dungeon.observe();
                    break;
                }
            }
        }

        if ( damage >= enemy.HP && enemy instanceof Hero ) {
            if ( ( (ColdGirlAI) state ).aiStatus < GOD_MODE ) {
                speak( "Baka.." );
                return super.attackProc( enemy, damage );
            } else {
                Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
                enemy.sprite.bloodBurstA( sprite.center(), enemy.HP );
                speak( "Are you done yet?!" );
                hostile = false;
                if ( Level.adjacent( pos, enemy.pos ) )  // Knockback
                {
                    for ( int i = 0; i < Level.NEIGHBOURS8.length; i++ ) {
                        int ofs = Level.NEIGHBOURS8[i];
                        if ( pos - enemy.pos == ofs ) {
                            int newPos = enemy.pos - ofs;
                            if ( ( Level.passable[newPos] || Level.avoid[newPos] ) && Actor.findChar( newPos ) == null ) {

                                Actor.addDelayed( new Pushing( enemy, enemy.pos, newPos ), -1 );

                                enemy.pos = newPos;
                                // FIXME
                                if ( enemy instanceof Mob ) {
                                    Dungeon.getLevel().mobPress( (Mob) enemy );
                                } else {
                                    Dungeon.getLevel().press( newPos, enemy );
                                }

                                enemy.sprite.bloodBurstA( sprite.center(), enemy.HP );
                            }
                            break;
                        }
                    }
                }
                ( (ColdGirlAI) state ).aiStatus = DONE_MODE;
                Buff.affect( Dungeon.getHero(), Frost.class, 10f );
                return -1;
            }
        } else {
            return super.attackProc( enemy, damage );
        }
    }

    @Override
    public void damage ( int dmg, Object src ) {
        hostile = true;
        if ( src instanceof Wand ) {
            if ( ( (ColdGirlAI) state ).aiStatus == PASSIVE ) {
                ( (ColdGirlAI) state ).aiStatus = HUNTING;
            }
            speak( "Are you mocking me?" );
        } else {
            super.damage( dmg, src );
        }

    }

    @Override
    public int defenseProc ( Char enemy, int damage ) {
        hostile = true;
        if ( ( (ColdGirlAI) state ).aiStatus == PASSIVE ) {
            ( (ColdGirlAI) state ).aiStatus = HUNTING;
        }

        if ( enemy instanceof Mob && !( enemy instanceof HiredMerc ) ) {

            //if(firstTroll)
            speak( "I have no time for fodder" );
            //GameScene.flash( 0x0042ff );
            //Sample.INSTANCE.play( Assets.SND_BLAST );
            firstTroll = false;
            trollMinion( enemy );
            return -1;
        }


        if ( !Level.adjacent( pos, enemy.pos ) )  // Space-Swap
        {
            if ( skillCharge > 0 ) {
                int tmpPos = pos;
                pos = enemy.pos;
                move( enemy.pos );
                sprite.place( enemy.pos );
                CellEmitter.center( enemy.pos ).burst( ShadowParticle.UP, Random.IntRange( 1, 2 ) );
                enemy.move( tmpPos );
                enemy.sprite.place( tmpPos );
                CellEmitter.center( tmpPos ).burst( ShadowParticle.UP, Random.IntRange( 1, 2 ) );
                Sample.INSTANCE.play( Assets.SND_GHOST );
                enemy.damage( damage, enemy );
                Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
                enemy.sprite.bloodBurstA( sprite.center(), enemy.HP );
                if ( firstSwap ) {
                    speak( "I have no time for this" );
                    heroSpeak( "EH?!" );
                } else {
                    // speak("Space Swap!");
                    heroSpeak( "..." );
                }
                firstSwap = false;
                skillCharge--;
                return -1;
            } else {
                if ( firstComplaint ) {
                    speak( "Erg..." );
                    firstComplaint = false;
                }

                return super.defenseProc( enemy, damage );
            }
        } else if ( firstFetch || ( (ColdGirlAI) state ).aiStatus == GOD_MODE ) {
            if ( Dungeon.getHero().belongings.weapon != null ) {

                int throwAt = 0;

                do {
                    throwAt = pos + 3 * Level.NEIGHBOURS8[Random.Int( Level.NEIGHBOURS8.length - 1 )];
                }
                while ( throwAt < 0 || throwAt > Level.passable.length || !Level.passable[throwAt] );


                final int throwAtFinal = throwAt;
                ( (MissileSprite) this.sprite.parent.recycle( MissileSprite.class ) ).
                        reset( ColdGirl.this.pos, throwAt, Dungeon.getHero().belongings.weapon, new Callback() {
                            @Override
                            public void call () {
                                Dungeon.getHero().belongings.weapon.detach( Dungeon.getHero().belongings.backpack ).onThrowColdGirl( throwAtFinal );

                            }
                        } );


                firstFetch = false;
                speak( "Go Fetch" );
                heroSpeak( "Wha.." );
                spend( 1f );
            }
            return -1;
        } else {
            return super.defenseProc( enemy, damage );
        }
    }

    private void trollMinion ( Char minion ) {
        if ( Level.adjacent( pos, minion.pos ) )  // Knockback
        {
            for ( int i = 0; i < Level.NEIGHBOURS8.length; i++ ) {
                int ofs = Level.NEIGHBOURS8[i];
                if ( pos - minion.pos == ofs ) {
                    int newPos = minion.pos - ofs;
                    if ( ( Level.passable[newPos] || Level.avoid[newPos] ) && Actor.findChar( newPos ) == null ) {

                        Actor.addDelayed( new Pushing( minion, minion.pos, newPos ), -1 );

                        enemy.pos = newPos;
                        // FIXME
                        if ( minion instanceof Mob ) {
                            Dungeon.getLevel().mobPress( (Mob) minion );
                        } else {
                            Dungeon.getLevel().press( newPos, minion );
                        }

                    }
                    break;
                }
            }
        }
        minion.damage( 9999, this );
        Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
        minion.sprite.bloodBurstA( sprite.center(), minion.HP );
    }

    @Override
    public void die ( Object cause ) {

        if ( ( (ColdGirlAI) state ).aiStatus == HUNTING ) {
            HT = 100;
            HP = 100;
            defenseSkill = 11;
            ( (ColdGirlAI) state ).aiStatus = SUPER_HUNTING;
            GameScene.flash( 0x0042ff );
            Camera.main.shake( 3, 0.07f * ( 20 ) );
            int[] cells = {
                    pos - 1, pos + 1, pos - Level.WIDTH, pos + Level.WIDTH,
                    pos - 1 - Level.WIDTH,
                    pos - 1 + Level.WIDTH,
                    pos + 1 - Level.WIDTH,
                    pos + 1 + Level.WIDTH
            };
            for ( int i = 0; i < cells.length; i++ ) {
                int cell = cells[i];
                Char ch = Actor.findChar( cell );
                if ( ch != null && ch != this && ch != Dungeon.getHero() && !( ch instanceof HiredMerc ) && ch.HP > 0 ) {
                    trollMinion( ch );
                }
            }
            Sample.INSTANCE.play( Assets.SND_BLAST );
            if ( Level.adjacent( pos, enemy.pos ) )  // Knockback
            {
                for ( int i = 0; i < Level.NEIGHBOURS8.length; i++ ) {
                    int ofs = Level.NEIGHBOURS8[i];
                    if ( pos - enemy.pos == ofs ) {
                        int newPos = enemy.pos - ofs;
                        if ( ( Level.passable[newPos] || Level.avoid[newPos] ) && Actor.findChar( newPos ) == null ) {

                            Actor.addDelayed( new Pushing( enemy, enemy.pos, newPos ), -1 );

                            enemy.pos = newPos;
                            // FIXME
                            if ( enemy instanceof Mob ) {
                                Dungeon.getLevel().mobPress( (Mob) enemy );
                            } else {
                                Dungeon.getLevel().press( newPos, enemy );
                            }

                        }
                        break;
                    }
                }
                speak( "Leave me alone!" );
                spawnMinions();
                spawnMinions();
                spawnMinions();
            }
            spend( 1f );
            return;
        } else if ( ( (ColdGirlAI) state ).aiStatus == SUPER_HUNTING ) {
            ( (ColdGirlAI) state ).aiStatus = DONE_MODE;
            ( (ColdGirlSprite) sprite ).haloUp();

            HT = 10000;
            HP = 10000;
            defenseSkill = 1000;
            if ( Level.adjacent( pos, enemy.pos ) )  // Knockback
            {
                for ( int i = 0; i < Level.NEIGHBOURS8.length; i++ ) {
                    int ofs = Level.NEIGHBOURS8[i];
                    if ( pos - enemy.pos == ofs ) {
                        int newPos = enemy.pos - ofs;
                        if ( ( Level.passable[newPos] || Level.avoid[newPos] ) && Actor.findChar( newPos ) == null ) {

                            Actor.addDelayed( new Pushing( enemy, enemy.pos, newPos ), -1 );

                            enemy.pos = newPos;
                            // FIXME
                            if ( enemy instanceof Mob ) {
                                Dungeon.getLevel().mobPress( (Mob) enemy );
                            } else {
                                Dungeon.getLevel().press( newPos, enemy );
                            }

                        }
                        break;
                    }
                }
            }
            discussionProgress = DISCUSSION_DEAD;
            speak( "What is your malfunction?!" );

            GameScene.flash( 0x0042ff );
            Camera.main.shake( 5, 0.07f * ( 30 ) );
            spend( 1f );
            return;
        }


        HP = 10000;
        speak( "In your dreams fool" );

    }

    public void discuss () {
        //Discussion("Cold Girl", "You don't belong here... leave", "Who are you?", "I belong where I want to");
        DiscussionNext( 0 );
    }

    @Override
    public void notice () {
        super.notice();
        speak( "You don't belong here" );
    }

    @Override
    public String description () {
        return
                "A young girl somewhat not affected by the cold.";
    }

    @Override
    public void storeInBundle ( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( AI_STATE, ( (ColdGirlAI) state ).aiStatus );
        bundle.put( CAME_FROM, cameFrom );
        bundle.put( CAME_FROM_POS, cameFromPos );

    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        ( (ColdGirlAI) state ).aiStatus = bundle.getInt( AI_STATE );
        cameFrom = bundle.getInt( CAME_FROM );
        cameFromPos = bundle.getInt( CAME_FROM_POS );
    }

    public void speak ( String speakText ) {
        this.sprite.showStatus( CharSprite.NEUTRAL, speakText );
    }

    public void heroSpeak ( String speakText ) {
        Dungeon.getHero().sprite.showStatus( CharSprite.NEUTRAL, speakText );
    }

    @Override
    public boolean attack ( Char enemy ) {
        boolean visibleFight = Dungeon.getVisible()[pos] || Dungeon.getVisible()[enemy.pos];

        if ( hit( this, enemy, false ) ) {

            if ( visibleFight ) {
                GLog.i( TXT_HIT, name, enemy.name );
            }

            int dmg = damageRoll();

            if ( enemy == Dungeon.getHero() ) {
                dmg *= Dungeon.getCurrentDifficulty().damageModifier();
                dmg *= Dungeon.getHero().heroSkills.passiveA3.incomingDamageModifier(); //  <--- Warrior Toughness if present
                dmg -= Dungeon.getHero().heroSkills.passiveA3.incomingDamageReduction( dmg ); // <--- Mage SpiritArmor if present
            }


            int effectiveDamage = Math.max( dmg, 0 );

            effectiveDamage = attackProc( enemy, effectiveDamage );
            effectiveDamage = enemy.defenseProc( this, effectiveDamage );
            if ( effectiveDamage < 0 ) {
                return true;
            }
            enemy.damage( effectiveDamage, this );


            if ( visibleFight ) {
                Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
            }

            if ( enemy == Dungeon.getHero() ) {
                Dungeon.getHero().interrupt();
                if ( effectiveDamage > enemy.HT / 4 ) {
                    Camera.main.shake( GameMath.gate( 1, effectiveDamage / ( enemy.HT / 4 ), 5 ), 0.3f );
                }
            }

            enemy.sprite.bloodBurstA( sprite.center(), effectiveDamage );
            enemy.sprite.flash();


            if ( !enemy.isAlive() && visibleFight ) {
                if ( enemy == Dungeon.getHero() ) {

                    if ( Dungeon.getHero().killerGlyph != null ) {

                        // FIXME
                        //	Dungeon.fail( Utils.format( ResultDescriptions.GLYPH, Dungeon.hero.killerGlyph.name(), Dungeon.depth ) );
                        //	GLog.n( TXT_KILL, Dungeon.hero.killerGlyph.name() );

                    } else {

                        Dungeon.fail( Utils.format( TXT_DEATH ) );


                        GLog.n( TXT_KILL, name );
                    }

                } else {
                    GLog.i( TXT_DEFEAT, name, enemy.name );
                }
            }

            return true;

        } else {

            if ( visibleFight ) {
                String defense = enemy.defenseVerb();
                enemy.sprite.showStatus( CharSprite.NEUTRAL, defense );
                GLog.i( TXT_SMB_MISSED, enemy.name, defense, name );


                Sample.INSTANCE.play( Assets.SND_MISS );
            }

            return false;

        }
    }

    @Override
    public HashSet<Class<?>> resistances () {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<?>> immunities () {
        return IMMUNITIES;
    }

    private void Discussion ( String title, String message, String... options ) {
        GameScene.show( new PersistentWndOptions( title, message, options ) {
            @Override
            protected void onSelect ( int index ) {
                DiscussionNext( index );
            }
        } );
    }

    private void DiscussionNext ( int index ) {
        switch ( discussionProgress + index ) {
            case 0:
                Discussion( "Cold Girl", "My existence does not concern you... leave", "How do I leave?", "Were you raised like this?" );
                discussionProgress += DISCUSSION_STEP;
                break;
            case 1:
                speak( "Die then" );
                hostile = true;
                ( (ColdGirlAI) state ).aiStatus = HUNTING;
                break;
            case DISCUSSION_STEP:
                sendBack();
                break;
            case DISCUSSION_STEP + 1:
                Discussion( "Cold Girl", "Talk about my mother like that again and I...\n You know what? Just die..", "Ok" );
                discussionProgress += DISCUSSION_STEP;
                break;
            case 2 * DISCUSSION_STEP:
                ( (ColdGirlAI) state ).aiStatus = HUNTING;
                speak( "The dead are always silent" );
                hostile = true;
                break;
            case DISCUSSION_DEAD:
                Discussion( "Cold Girl", "What is wrong with you?!", "How are you this strong?" );
                discussionProgress += DISCUSSION_STEP;
                break;
            case DISCUSSION_DEAD + DISCUSSION_STEP:
                Discussion( "Cold Girl", "The rules cannot protect you from me fool!", "What rules?" );
                discussionProgress += DISCUSSION_STEP;
                break;
            case DISCUSSION_DEAD + 2 * DISCUSSION_STEP:
                Discussion( "Cold Girl", "LEAVE!", "Wai.." );
                discussionProgress += DISCUSSION_STEP;
                break;
            case DISCUSSION_DEAD + 3 * DISCUSSION_STEP:
                sendBack();
                Dungeon.getHero().heroSkills.unlockSkill();
                GLog.p( "Fighting that weird girl inspired you into learning " + Dungeon.getHero().heroSkills.unlockableSkillName() );
                break;
            default:
                discuss(); // fallback to prevent getting stuck
                discussionProgress = 0;
        }
    }

    public void spawnMinions () {
        ArrayList<Integer> spawnPoints = new ArrayList<Integer>();

        for ( int i = 0; i < Level.NEIGHBOURS8.length; i++ ) {
            int p = pos + Level.NEIGHBOURS8[i];
            if ( Actor.findChar( p ) == null && ( Level.passable[p] || Level.avoid[p] ) ) {
                spawnPoints.add( p );
            }
        }

        if ( spawnPoints.size() > 0 ) {
            Slaves slave = new Slaves();
            slave.pos = Random.element( spawnPoints );
            Sample.INSTANCE.play( Assets.SND_GHOST );

            GameScene.add( slave );
            Actor.addDelayed( new Pushing( slave, pos, slave.pos ), -1 );
            slave.sprite.emitter().burst( ShadowParticle.CURSE, 5 );
        }

    }

    private void sendBack () {
        InterlevelScene.mode = InterlevelScene.Mode.TELEPORT_BACK;
        Game.switchScene( InterlevelScene.class );
        Dungeon.observe();
    }

    public class ColdGirlSkel extends Skeleton {
        {
            HP = HT = 10;
            defenseSkill = 1;
            EXP = 0;
            state = HUNTING;
        }

        @Override
        public boolean act () {
            if ( ( (ColdGirlAI) ColdGirl.this.state ).aiStatus != SUPER_HUNTING ) {
                die( null );
                return true;
            }
            return super.act();
        }

        @Override
        public void die ( Object cause ) {
            if ( cause != null ) {
                ColdGirl.this.speak( "What a waste of mana" );
            }
            super.die( cause );
        }

        @Override
        public String description () {
            return "Me in the future...";

        }
    }

    public class Slaves extends EnslavedSouls {

        {
            name = "enslaved spirit";

            HP = HT = 1;
            defenseSkill = 1;

            EXP = 0;

            state = HUNTING;
        }

        @Override
        public boolean act () {
            if ( ( (ColdGirlAI) ColdGirl.this.state ).aiStatus != SUPER_HUNTING ) {
                die( null );
                return true;
            }
            return super.act();
        }

        @Override
        public int attackSkill ( Char target ) {
            return 5;
        }

        @Override
        public int damageRoll () {
            return Random.NormalIntRange( 3, 8 );
        }

        @Override
        public int dr () {
            return 8;
        }

        @Override
        public void die ( Object cause ) {
            if ( cause != null ) {
                ColdGirl.this.speak( "Useless!" );
            }
            super.die( cause );
        }

        @Override
        public String description () {
            return "A spirit in agony";

        }
    }

    public class ColdGirlAI implements AiState {

        public static final String TAG = "COLD_GIRL";
        public int aiStatus = PASSIVE;

        @Override
        public boolean act ( boolean enemyInFOV, boolean justAlerted ) {
            if ( aiStatus == PASSIVE || aiStatus == DONE_MODE ) {
                enemySeen = false;
                spend( TICK );
                sprite.idle();
                target = -1;
                return true;
            } else if ( aiStatus == HUNTING ) {
                enemySeen = enemyInFOV;
                if ( enemyInFOV && canAttack( enemy ) ) {

                    return doAttack( enemy );

                } else {

                    if ( enemyInFOV ) {
                        target = enemy.pos;
                    }

                    int oldPos = pos;
                    if ( target != -1 && getCloser( target ) ) {

                        spend( 1 / speed() );
                        return moveSprite( oldPos, pos );

                    } else {

                        spend( TICK );
                        //aiStatus = PASSIVE;
                        //state = WANDERING;
                        //target = Dungeon.level.randomDestination();
                        sprite.idle();
                        return true;
                    }
                }
            } else if ( aiStatus == SUPER_HUNTING ) {
                enemySeen = enemyInFOV;
                if ( enemyInFOV && canAttack( enemy ) ) {

                    return doAttack( enemy );

                } else {

                    if ( enemyInFOV ) {
                        target = enemy.pos;
                    }

                    int oldPos = pos;
                    if ( target != -1 && getCloser( target ) ) {

                        spend( 1 / speed() );
                        return moveSprite( oldPos, pos );

                    } else {

                        spend( TICK );
                        //aiStatus = PASSIVE;
                        //state = WANDERING;
                        //target = Dungeon.level.randomDestination();
                        sprite.idle();
                        return true;
                    }
                }
            } else if ( aiStatus == GOD_MODE ) {
                enemySeen = enemyInFOV;
                if ( enemyInFOV && canAttack( enemy ) ) {

                    return doAttack( enemy );

                } else {

                    if ( enemyInFOV ) {
                        target = enemy.pos;
                    }

                    int oldPos = pos;
                    if ( target != -1 && getCloser( target ) ) {

                        spend( 1 / speed() );
                        return moveSprite( oldPos, pos );

                    } else {

                        spend( TICK );
                        //aiStatus = PASSIVE;
                        //state = WANDERING;
                        //target = Dungeon.level.randomDestination();
                        sprite.idle();
                        return true;
                    }
                }
            }
            spend( TICK ); // Avoid getting stuck
            return true;
        }

        @Override
        public String status () {
            if ( aiStatus == PASSIVE ) {
                return Utils.format( "The %s seems passive.\n You can tell she is cold but she shows no physical signs of it.", name );
            } else if ( aiStatus == HUNTING ) {
                return Utils.format( "The %s seems upset.\n She may be young but she looks dangerous.", name );
            } else if ( aiStatus == SUPER_HUNTING ) {
                return Utils.format( "The %s seems very dangerous.\n Something is not right about her.", name );
            } else {
                return Utils.format( "The %s seems non-human.\n Taunting her was a bad idea", name );
            }
        }
    }
}
