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

public class Gizmo {

    private boolean exists;
    private boolean alive;
    private boolean active;
    private boolean visible;

    private Group parent;

    private Camera camera;

    public Gizmo () {
        setExists( true );
        setAlive( true );
        setActive( true );
        setVisible( true );
    }

    public void destroy () {
        setParent( null );
    }

    public void update () {
    }

    public void draw () {
    }

    public void kill () {
        setAlive( false );
        setExists( false );
    }

    // Not exactly opposite to "kill" method
    public void revive () {
        setAlive( true );
        setExists( true );
    }

    public Camera camera () {
        if ( getCamera() != null ) {
            return getCamera();
        } else if ( getParent() != null ) {
            return getParent().camera();
        } else {
            return null;
        }
    }

    public boolean isVisible () {
        if ( getParent() == null ) {
            return getVisible();
        } else {
            return getVisible() && getParent().isVisible();
        }
    }

    public boolean isActive () {
        if ( getParent() == null ) {
            return active;
        } else {
            return active && getParent().isActive();
        }
    }

    public void killAndErase () {
        kill();
        if ( getParent() != null ) {
            getParent().erase( this );
        }
    }

    public void remove () {
        if ( getParent() != null ) {
            getParent().remove( this );
        }
    }

    boolean isExists () {
        return exists;
    }

    private void setExists ( boolean exists ) {
        this.exists = exists;
    }

    boolean isAlive () {
        return alive;
    }

    private void setAlive ( boolean alive ) {
        this.alive = alive;
    }

    public void setActive ( boolean active ) {
        this.active = active;
    }

    public boolean getVisible () {
        return visible;
    }

    public void setVisible ( boolean visible ) {
        this.visible = visible;
    }

    public Group getParent () {
        return parent;
    }

    void setParent ( Group parent ) {
        this.parent = parent;
    }

    public Camera getCamera () {
        return camera;
    }

    public void setCamera ( Camera camera ) {
        this.camera = camera;
    }
}
