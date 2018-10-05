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

import android.graphics.RectF;

public class MovieClip extends Image {

    public boolean paused = false;
    public Listener listener;
    protected Animation curAnim;
    protected int curFrame;
    private float frameTimer;
    private boolean finished;

    public MovieClip () {
        super();
    }

    public MovieClip ( Object tx ) {
        super( tx );
    }

    @Override
    public void update () {
        super.update();
        if ( !paused ) {
            updateAnimation();
        }
    }

    private void updateAnimation () {
        if ( curAnim != null && curAnim.getDelay() > 0 && ( curAnim.isLooped() || !finished ) ) {

            int lastFrame = curFrame;

            frameTimer += Game.getElapsed();
            while ( frameTimer > curAnim.getDelay() ) {
                frameTimer -= curAnim.getDelay();
                if ( curFrame == curAnim.getFrames().length - 1 ) {
                    if ( curAnim.isLooped() ) {
                        curFrame = 0;
                    }
                    finished = true;
                    if ( listener != null ) {
                        listener.onComplete( curAnim );
                        // This check can probably be removed
                        if ( curAnim == null ) {
                            return;
                        }
                    }

                } else {
                    curFrame++;
                }
            }

            if ( curFrame != lastFrame ) {
                frame( curAnim.getFrames()[curFrame] );
            }

        }
    }

    public void play ( Animation anim ) {
        play( anim, false );
    }

    public void play ( Animation anim, boolean force ) {

        if ( !force && ( curAnim != null ) && ( curAnim == anim ) && ( curAnim.isLooped() || !finished ) ) {
            return;
        }

        curAnim = anim;
        curFrame = 0;
        finished = false;

        frameTimer = 0;

        if ( anim != null ) {
            frame( anim.getFrames()[curFrame] );
        }
    }

    // TODO: Move in to its own file
    @SuppressWarnings ( "PublicInnerClass" )
    public interface Listener {
        void onComplete ( Animation anim );
    }

    // TODO: Move in to its own file
    @SuppressWarnings ( "PublicInnerClass" )
    public static class Animation {

        private float delay;
        private RectF[] frames;
        private final boolean looped;

        public Animation ( int fps, boolean looped ) {
            this.setDelay( 1f / fps );
            this.looped = looped;
        }

        @SuppressWarnings ( "AssignmentOrReturnOfFieldWithMutableType" )
        Animation frames ( RectF... frames ) {
            this.setFrames( frames );
            return this;
        }

        public Animation frames ( TextureFilm film, Object... frames ) {
            this.setFrames( new RectF[frames.length] );
            for ( int i = 0; i < frames.length; i++ ) {
                this.getFrames()[i] = film.get( frames[i] );
            }
            return this;
        }

        @SuppressWarnings ( { "MethodDoesntCallSuperMethod", "override" } )
        public Animation clone () {
            return new Animation( Math.round( 1 / getDelay() ), isLooped() ).frames( getFrames() );
        }

        float getDelay () {
            return delay;
        }

        public void setDelay ( float delay ) {
            this.delay = delay;
        }

        public RectF[] getFrames () {
            return frames;
        }

        void setFrames ( RectF[] frames ) {
            this.frames = frames;
        }

        boolean isLooped () {
            return looped;
        }
    }
}
