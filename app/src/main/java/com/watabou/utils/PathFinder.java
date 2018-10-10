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

package com.watabou.utils;

import java.util.Arrays;
import java.util.LinkedList;

public class PathFinder {

    private static int[] distance;

    private static boolean[] goals;
    private static int[] queue;

    private static int size = 0;

    private static int[] dir;

    public static void setMapSize ( int width, int height ) {

        int size = width * height;

        if ( PathFinder.size != size ) {

            PathFinder.size = size;
            setDistance( new int[size] );
            goals = new boolean[size];
            queue = new int[size];

            dir = new int[] { -1, +1, -width, +width, -width - 1, -width + 1, +width - 1, +width + 1 };
        }
    }

    public static Path find ( int from, int to, boolean[] passable ) {

        if ( buildDistanceMap( from, to, passable ) ) {
            return null;
        }

        Path result = new Path();
        int s = from;

        // From the starting position we are moving downwards,
        // until we reach the ending point
        do {
            int minD = getDistance()[s];
            int mins = s;

            for ( int aDir : dir ) {

                int n = s + aDir;

                int thisD = getDistance()[n];
                if ( thisD < minD ) {
                    minD = thisD;
                    mins = n;
                }
            }
            s = mins;
            result.add( s );
        } while ( s != to );

        return result;
    }

    public static int getStep ( int from, int to, boolean[] passable ) {

        if ( buildDistanceMap( from, to, passable ) ) {
            return -1;
        }

        // From the starting position we are making one step downwards
        int minD = getDistance()[from];
        int best = from;

        for ( int aDir : dir ) {

            int step = from + aDir;
            int stepD = getDistance()[step];
            if ( stepD < minD ) {
                minD = stepD;
                best = step;
            }
        }

        return best;
    }

    public static int getStepBack ( int cur, int from, boolean[] passable ) {

        int d = buildEscapeDistanceMap( cur, from, passable );
        for ( int i = 0; i < size; i++ ) {
            goals[i] = getDistance()[i] == d;
        }
        if ( !buildDistanceMap( cur, goals, passable ) ) {
            return -1;
        }

        // From the starting position we are making one step downwards
        int minD = getDistance()[cur];
        int mins = cur;

        for ( int aDir : dir ) {

            int n = cur + aDir;
            int thisD = getDistance()[n];

            if ( thisD < minD ) {
                minD = thisD;
                mins = n;
            }
        }

        return mins;
    }

    private static boolean buildDistanceMap ( int from, int to, boolean[] passable ) {

        if ( from == to ) {
            return true;
        }

        Arrays.fill( getDistance(), Integer.MAX_VALUE );

        int tail = 0;

        // Add to queue
        queue[tail] = to;
        tail++;
        getDistance()[to] = 0;

        int head = 0;
        boolean pathFound = false;
        while ( head < tail ) {

            // Remove from queue
            int step = queue[head];
            head++;
            if ( step == from ) {
                pathFound = true;
                break;
            }
            int nextDistance = getDistance()[step] + 1;

            for ( int aDir : dir ) {

                int n = step + aDir;
                if ( n == from || ( n >= 0 && n < size && passable[n] && ( getDistance()[n] > nextDistance ) ) ) {
                    // Add to queue
                    queue[tail] = n;
                    tail++;
                    getDistance()[n] = nextDistance;
                }

            }
        }

        return !pathFound;
    }

    public static void buildDistanceMap ( int to, boolean[] passable, int limit ) {

        Arrays.fill( getDistance(), Integer.MAX_VALUE );

        int tail = 0;

        // Add to queue
        queue[tail] = to;
        tail++;
        getDistance()[to] = 0;

        int head = 0;
        while ( head < tail ) {

            // Remove from queue
            int step = queue[head];
            head++;

            int nextDistance = getDistance()[step] + 1;
            if ( nextDistance > limit ) {
                return;
            }

            for ( int aDir : dir ) {

                int n = step + aDir;
                if ( n >= 0 && n < size && passable[n] && ( getDistance()[n] > nextDistance ) ) {
                    // Add to queue
                    queue[tail] = n;
                    tail++;
                    getDistance()[n] = nextDistance;
                }

            }
        }
    }

    private static boolean buildDistanceMap ( int from, boolean[] to, boolean[] passable ) {

        if ( to[from] ) {
            return false;
        }

        Arrays.fill( getDistance(), Integer.MAX_VALUE );

        int tail = 0;

        // Add to queue
        for ( int i = 0; i < size; i++ ) {
            if ( to[i] ) {
                queue[tail] = i;
                tail++;
                getDistance()[i] = 0;
            }
        }

        int head = 0;
        boolean pathFound = false;
        while ( head < tail ) {

            // Remove from queue
            int step = queue[head];
            head++;
            if ( step == from ) {
                pathFound = true;
                break;
            }
            int nextDistance = getDistance()[step] + 1;

            for ( int aDir : dir ) {

                int n = step + aDir;
                if ( n == from || ( n >= 0 && n < size && passable[n] && ( getDistance()[n] > nextDistance ) ) ) {
                    // Add to queue
                    queue[tail] = n;
                    tail++;
                    getDistance()[n] = nextDistance;
                }

            }
        }

        return pathFound;
    }

    private static int buildEscapeDistanceMap ( int cur, int from, boolean[] passable ) {

        Arrays.fill( getDistance(), Integer.MAX_VALUE );

        int tail = 0;

        // Add to queue
        queue[tail] = from;
        tail++;
        getDistance()[from] = 0;

        int dist = 0;

        int head = 0;
        int destDist = Integer.MAX_VALUE;
        final float FACTOR = 2.0f;
        while ( head < tail ) {

            // Remove from queue
            int step = queue[head];
            head++;
            dist = getDistance()[step];

            if ( dist > destDist ) {
                return destDist;
            }

            if ( step == cur ) {
                destDist = (int) ( dist * FACTOR ) + 1;
            }

            int nextDistance = dist + 1;

            for ( int aDir : dir ) {

                int n = step + aDir;
                if ( n >= 0 && n < size && passable[n] && getDistance()[n] > nextDistance ) {
                    // Add to queue
                    queue[tail] = n;
                    tail++;
                    getDistance()[n] = nextDistance;
                }

            }
        }

        return dist;
    }

    @SuppressWarnings ( "unused" )
    private static void buildDistanceMap ( int to, boolean[] passable ) {

        Arrays.fill( getDistance(), Integer.MAX_VALUE );

        int tail = 0;

        // Add to queue
        queue[tail] = to;
        tail++;
        getDistance()[to] = 0;

        int head = 0;
        while ( head < tail ) {

            // Remove from queue
            int step = queue[head];
            head++;
            int nextDistance = getDistance()[step] + 1;

            for ( int aDir : dir ) {

                int n = step + aDir;
                if ( n >= 0 && n < size && passable[n] && ( getDistance()[n] > nextDistance ) ) {
                    // Add to queue
                    queue[tail] = n;
                    tail++;
                    getDistance()[n] = nextDistance;
                }

            }
        }
    }

    @SuppressWarnings ( "AssignmentOrReturnOfFieldWithMutableType" )
    public static int[] getDistance () {
        return distance;
    }

    private static void setDistance ( int[] distance ) {
        PathFinder.distance = distance;
    }

    //TODO: Move into its own file
    @SuppressWarnings ( { "serial", "PublicInnerClass" } )
    public static class Path extends LinkedList<Integer> {
    }
}
