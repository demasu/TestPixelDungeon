/*
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

package com.watabou.noosa;

import java.util.ArrayList;

public class Group extends Gizmo {

    // Accessing it is a little faster,
    // than calling memebers.getSize()
    private int length;
    private ArrayList<Gizmo> members;

    public Group () {
        setMembers( new ArrayList<Gizmo>() );
        setLength( 0 );
    }

    @Override
    public void destroy () {
        for ( int i = 0; i < getLength(); i++ ) {
            Gizmo g = getMembers().get( i );
            if ( g != null ) {
                g.destroy();
            }
        }

        getMembers().clear();
        setMembers( null );
        setLength( 0 );
    }

    @Override
    public void update () {
        for ( int i = 0; i < getLength(); i++ ) {
            Gizmo g = getMembers().get( i );
            if ( g != null && g.isExists() && g.isActive() ) {
                g.update();
            }
        }
    }

    @Override
    public void draw () {
        for ( int i = 0; i < getLength(); i++ ) {
            Gizmo g = getMembers().get( i );
            if ( g != null && g.isExists() && g.getVisible() ) {
                g.draw();
            }
        }
    }

    @Override
    public void kill () {
        // A killed group keeps all its members,
        // but they get killed too
        for ( int i = 0; i < getLength(); i++ ) {
            Gizmo g = getMembers().get( i );
            if ( g != null && g.isExists() ) {
                g.kill();
            }
        }

        super.kill();
    }

    public Gizmo add ( Gizmo g ) {

        if ( g.getParent() == this ) {
            return g;
        }

        if ( g.getParent() != null ) {
            g.getParent().remove( g );
        }

        // Trying to find an empty space for a new member
        for ( int i = 0; i < getLength(); i++ ) {
            if ( getMembers().get( i ) == null ) {
                getMembers().set( i, g );
                g.setParent( this );
                return g;
            }
        }

        getMembers().add( g );
        g.setParent( this );
        setLength( getLength() + 1 );
        return g;
    }

    public Gizmo addToBack ( Gizmo g ) {

        if ( g.getParent() == this ) {
            sendToBack( g );
            return g;
        }

        if ( g.getParent() != null ) {
            g.getParent().remove( g );
        }

        if ( getMembers().get( 0 ) == null ) {
            getMembers().set( 0, g );
            g.setParent( this );
            return g;
        }

        getMembers().add( 0, g );
        g.setParent( this );
        setLength( getLength() + 1 );
        return g;
    }

    public Gizmo recycle ( Class<? extends Gizmo> c ) {

        Gizmo g = getFirstAvailable( c );
        if ( g != null ) {

            return g;

        } else if ( c == null ) {

            return null;

        } else {

            try {
                return add( c.newInstance() );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }

        return null;
    }

    // Fast removal - replacing with null
    public Gizmo erase ( Gizmo g ) {
        int index = getMembers().indexOf( g );
        if ( index != -1 ) {
            getMembers().set( index, null );
            g.setParent( null );
            return g;
        } else {
            return null;
        }
    }

    // Real removal
    public Gizmo remove ( Gizmo g ) {
        if ( getMembers().remove( g ) ) {
            setLength( getLength() - 1 );
            g.setParent( null );
            return g;
        } else {
            return null;
        }
    }

    private Gizmo getFirstAvailable ( Class<? extends Gizmo> c ) {

        for ( int i = 0; i < getLength(); i++ ) {
            Gizmo g = getMembers().get( i );
            if ( g != null && !g.isExists() && ( ( c == null ) || g.getClass() == c ) ) {
                return g;
            }
        }

        return null;
    }

    public int countLiving () {

        int count = 0;

        for ( int i = 0; i < getLength(); i++ ) {
            Gizmo g = getMembers().get( i );
            if ( g != null && g.isExists() && g.isAlive() ) {
                count++;
            }
        }

        return count;
    }

    public void clear () {
        for ( int i = 0; i < getLength(); i++ ) {
            Gizmo g = getMembers().get( i );
            if ( g != null ) {
                g.setParent( null );
            }
        }
        getMembers().clear();
        setLength( 0 );
    }

    public Gizmo bringToFront ( Gizmo g ) {
        if ( getMembers().contains( g ) ) {
            getMembers().remove( g );
            getMembers().add( g );
            return g;
        } else {
            return null;
        }
    }

    private void sendToBack ( Gizmo g ) {
        if ( getMembers().contains( g ) ) {
            getMembers().remove( g );
            getMembers().add( 0, g );
        }
    }

    public int getLength () {
        return length;
    }

    private void setLength ( int length ) {
        this.length = length;
    }

    @SuppressWarnings ( "AssignmentOrReturnOfFieldWithMutableType" )
    public ArrayList<Gizmo> getMembers () {
        return members;
    }

    private void setMembers ( ArrayList<Gizmo> members ) {
        this.members = members;
    }
}
