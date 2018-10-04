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
import com.demasu.testpixeldungeon.DungeonTilemap;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.hero.Belongings;
import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.weapon.missiles.Arrow;
import com.demasu.testpixeldungeon.items.weapon.missiles.Bow;
import com.demasu.testpixeldungeon.mechanics.Ballistica;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.windows.WndBag;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.Bundle;

public class QuickSlot extends Button implements WndBag.Listener {

    private static final String TXT_SELECT_ITEM = "Select an item for the quickslot";
    private static final String QUICKSLOT1 = "quickslot";
    private static final String QUICKSLOT2 = "quickslot2";
    private static Object primaryValue;
    private static Object secondaryValue;
    private static QuickSlot primary;
    private static QuickSlot secondary;
    private static Char lastTarget = null;
    private Item itemInSlot;
    private ItemSlot slot;
    private Image crossB;
    private Image crossM;
    private boolean targeting = false;

    public static void refresh () {
        if ( primary != null ) {
            primary.item( primary.select() );
        }
        if ( secondary != null ) {
            secondary.item( secondary.select() );
        }
    }

    public static void target ( Item item, Char target ) {
        if ( target != Dungeon.getHero() ) {
            lastTarget = target;
            HealthIndicator.instance.target( target );
        }
    }

    public static void cancel () {
        if ( primary != null && primary.targeting ) {
            primary.crossB.setVisible( false );
            primary.crossM.remove();
            primary.targeting = false;
        }
        if ( secondary != null && secondary.targeting ) {
            secondary.crossB.setVisible( false );
            secondary.crossM.remove();
            secondary.targeting = false;
        }
    }

    @SuppressWarnings ( "unchecked" )
    public static void save ( Bundle bundle ) {
        Belongings stuff = Dungeon.getHero().belongings;

        if ( getPrimaryValue() instanceof Class &&
                stuff.getItem( (Class<? extends Item>) getPrimaryValue() ) != null ) {

            bundle.put( QUICKSLOT1, ( (Class<?>) getPrimaryValue() ).getName() );
        }
        if ( QuickSlot.getSecondaryValue() instanceof Class &&
                stuff.getItem( (Class<? extends Item>) getSecondaryValue() ) != null &&
                Toolbar.secondQuickslot() ) {

            bundle.put( QUICKSLOT2, ( (Class<?>) getSecondaryValue() ).getName() );
        }
    }

    public static void save ( Bundle bundle, Item item ) {
        if ( item == getPrimaryValue() ) {
            bundle.put( QuickSlot.QUICKSLOT1, true );
        }
        if ( item == getSecondaryValue() && Toolbar.secondQuickslot() ) {
            bundle.put( QuickSlot.QUICKSLOT2, true );
        }
    }

    public static void restore ( Bundle bundle ) {
        setPrimaryValue( null );
        setSecondaryValue( null );

        String qsClass = bundle.getString( QUICKSLOT1 );
        if ( qsClass != null ) {
            try {
                setPrimaryValue( Class.forName( qsClass ) );
            } catch ( ClassNotFoundException e ) {
            }
        }

        qsClass = bundle.getString( QUICKSLOT2 );
        if ( qsClass != null ) {
            try {
                setSecondaryValue( Class.forName( qsClass ) );
            } catch ( ClassNotFoundException e ) {
            }
        }
    }

    public static void restore ( Bundle bundle, Item item ) {
        if ( bundle.getBoolean( QUICKSLOT1 ) ) {
            setPrimaryValue( item );
        }
        if ( bundle.getBoolean( QUICKSLOT2 ) ) {
            setSecondaryValue( item );
        }
    }

    public static void compress () {
        if ( ( getPrimaryValue() == null && getSecondaryValue() != null ) ||
                ( getPrimaryValue() == getSecondaryValue() ) ) {

            setPrimaryValue( getSecondaryValue() );
            setSecondaryValue( null );
        }
    }

    public static Object getPrimaryValue () {
        return primaryValue;
    }

    public static void setPrimaryValue ( Object primaryValue ) {
        QuickSlot.primaryValue = primaryValue;
    }

    public static Object getSecondaryValue () {
        return secondaryValue;
    }

    public static void setSecondaryValue ( Object secondaryValue ) {
        QuickSlot.secondaryValue = secondaryValue;
    }

    public void primary () {
        primary = this;
        item( select() );
    }

    public void secondary () {
        secondary = this;
        item( select() );
    }

    @Override
    public void destroy () {
        super.destroy();

        if ( this == primary ) {
            primary = null;
        } else {
            secondary = null;
        }

        lastTarget = null;
    }

    @Override
    protected void createChildren () {
        super.createChildren();

        slot = new ItemSlot() {
            @Override
            protected void onClick () {
                if ( targeting ) {
                    GameScene.handleCell( lastTarget.pos );
                } else {
                    useTargeting();
                    select().execute( Dungeon.getHero() );
                }
            }

            @Override
            protected boolean onLongClick () {
                return QuickSlot.this.onLongClick();
            }

            @Override
            protected void onTouchDown () {
                icon.lightness( 0.7f );
            }

            @Override
            protected void onTouchUp () {
                icon.resetColor();
            }
        };
        add( slot );

        crossB = Icons.TARGET.get();
        crossB.setVisible( false );
        add( crossB );

        crossM = new Image();
        crossM.copy( crossB );
    }

    @Override
    protected void layout () {
        super.layout();

        slot.fill( this );

        crossB.x = PixelScene.align( getX() + ( getWidth() - crossB.width ) / 2 );
        crossB.y = PixelScene.align( getY() + ( getHeight() - crossB.height ) / 2 );
    }

    @Override
    protected void onClick () {
        GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM );
    }

    @Override
    protected boolean onLongClick () {
        GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM );
        return true;
    }

    @SuppressWarnings ( "unchecked" )
    private Item select () {

        Object content = ( this == primary ? getPrimaryValue() : getSecondaryValue() );
        if ( content instanceof Item ) {

            return (Item) content;

        } else if ( content != null ) {

            Item item = Dungeon.getHero().belongings.getItem( (Class<? extends Item>) content );
            return item != null ? item : Item.virtual( (Class<? extends Item>) content );

        } else {

            return null;

        }
    }

    @Override
    public void onSelect ( Item item ) {
        if ( item != null ) {
            if ( this == primary ) {
                setPrimaryValue( ( item.stackable ? item.getClass() : item ) );
            } else {
                setSecondaryValue( ( item.stackable ? item.getClass() : item ) );
            }
            refresh();
        }
    }

    public void item ( Item item ) {
        slot.item( item );
        itemInSlot = item;
        enableSlot();
    }

    public void enable ( boolean value ) {
        setActive( value );
        if ( value ) {
            enableSlot();
        } else {
            slot.enable( false );
        }
    }

    private void enableSlot () {
        slot.enable( !( itemInSlot instanceof Bow ) && ( !( itemInSlot instanceof Arrow ) || Dungeon.getHero().belongings.bow != null ) &&
                itemInSlot != null &&
                itemInSlot.quantity() > 0 &&
                ( Dungeon.getHero().belongings.backpack.contains( itemInSlot ) || itemInSlot.isEquipped( Dungeon.getHero() ) ) );
    }

    private void useTargeting () {

        targeting = lastTarget != null && lastTarget.isAlive() && Dungeon.getVisible()[lastTarget.pos];

        if ( targeting ) {
            int pos = Ballistica.cast( Dungeon.getHero().pos, lastTarget.pos, false, true );
            if ( pos != lastTarget.pos ) {
                lastTarget = null;
                targeting = false;
            }
        }

        if ( !targeting ) {
            int n = Dungeon.getHero().visibleEnemies();
            for ( int i = 0; i < n; i++ ) {
                Mob enemy = Dungeon.getHero().visibleEnemy( i );
                int pos = Ballistica.cast( Dungeon.getHero().pos, enemy.pos, false, true );
                if ( pos == enemy.pos ) {
                    lastTarget = enemy;
                    targeting = true;
                    break;
                }
            }
        }

        if ( targeting ) {
            if ( Actor.all().contains( lastTarget ) ) {
                lastTarget.sprite.parent.add( crossM );
                crossM.point( DungeonTilemap.tileToWorld( lastTarget.pos ) );
                crossB.setVisible( true );
            } else {
                lastTarget = null;
            }
        }
    }
}
