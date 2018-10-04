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
package com.demasu.testpixeldungeon.ui;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class AttackIndicator extends Tag {

    private static final float ENABLED = 1.0f;
    private static final float DISABLED = 0.3f;

    private static AttackIndicator instance;
    private static Mob lastTarget = null;
    private CharSprite sprite = null;
    private ArrayList<Mob> candidates = new ArrayList<Mob>();
    private boolean enabled = true;

    public AttackIndicator () {
        super( DangerIndicator.COLOR );

        instance = this;

        setSize( 24, 24 );
        visible( false );
        enable( false );
    }

    public static void target ( Char target ) {
        lastTarget = (Mob) target;
        instance.updateImage();

        HealthIndicator.instance.target( target );
    }

    public static void updateState () {
        instance.checkEnemies();
    }

    @Override
    protected void createChildren () {
        super.createChildren();
    }

    @Override
    protected void layout () {
        super.layout();

        if ( sprite != null ) {
            sprite.x = getX() + ( getWidth() - sprite.width() ) / 2;
            sprite.y = getY() + ( getHeight() - sprite.height() ) / 2;
            PixelScene.align( sprite );
        }
    }

    @Override
    public void update () {
        super.update();

        if ( Dungeon.getHero().isAlive() ) {

            if ( !Dungeon.getHero().ready ) {
                enable( false );
            }

        } else {
            visible( false );
            enable( false );
        }
    }

    private void checkEnemies () {

        int heroPos = Dungeon.getHero().pos;
        candidates.clear();
        int v = Dungeon.getHero().visibleEnemies();
        for ( int i = 0; i < v; i++ ) {
            Mob mob = Dungeon.getHero().visibleEnemy( i );
            if ( Level.adjacent( heroPos, mob.pos ) ) {
                candidates.add( mob );
            }
        }

        if ( !candidates.contains( lastTarget ) ) {
            if ( candidates.isEmpty() ) {
                lastTarget = null;
            } else {
                lastTarget = Random.element( candidates );
                updateImage();
                flash();
            }
        } else {
            if ( !bg.getVisible() ) {
                flash();
            }
        }

        visible( lastTarget != null );
        enable( bg.getVisible() );
    }

    private void updateImage () {

        if ( sprite != null ) {
            sprite.killAndErase();
            sprite = null;
        }

        try {
            sprite = lastTarget.spriteClass.newInstance();
            sprite.idle();
            sprite.paused = true;
            add( sprite );

            sprite.x = getX() + ( getWidth() - sprite.width() ) / 2 + 1;
            sprite.y = getY() + ( getHeight() - sprite.height() ) / 2;
            PixelScene.align( sprite );

        } catch ( Exception e ) {
        }
    }

    private void enable ( boolean value ) {
        enabled = value;
        if ( sprite != null ) {
            sprite.alpha( value ? ENABLED : DISABLED );
        }
    }

    private void visible ( boolean value ) {
        bg.setVisible( value );
        if ( sprite != null ) {
            sprite.setVisible( value );
        }
    }

    @Override
    protected void onClick () {
        if ( enabled ) {
            Dungeon.getHero().handle( lastTarget.pos );
        }
    }
}
