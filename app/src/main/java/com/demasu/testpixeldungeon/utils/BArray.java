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
package com.demasu.testpixeldungeon.utils;

public class BArray {

// --Commented out by Inspection START (8/29/18, 12:39 PM):
//    public static boolean[] and(boolean[] a, boolean[] b, boolean[] result) {
//
//        int length = a.length;
//
//        if (result == null) {
//            result = new boolean[length];
//        }
//
//        for (int i = 0; i < length; i++) {
//            result[i] = a[i] && b[i];
//        }
//
//        return result;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:39 PM)

    public static boolean[] or(boolean[] a, boolean[] b, boolean[] result) {

        int length = a.length;

        if (result == null) {
            result = new boolean[length];
        }

        for (int i = 0; i < length; i++) {
            result[i] = a[i] || b[i];
        }

        return result;
    }

    public static boolean[] not(boolean[] a, boolean[] result) {

        int length = a.length;

        if (result == null) {
            result = new boolean[length];
        }

        for (int i = 0; i < length; i++) {
            result[i] = !a[i];
        }

        return result;
    }

// --Commented out by Inspection START (8/29/18, 12:39 PM):
//    public static boolean[] is(int[] a, boolean[] result, int v1) {
//
//        int length = a.length;
//
//        if (result == null) {
//            result = new boolean[length];
//        }
//
//        for (int i = 0; i < length; i++) {
//            result[i] = a[i] == v1;
//        }
//
//        return result;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:39 PM)

// --Commented out by Inspection START (8/29/18, 12:39 PM):
//    public static boolean[] isOneOf(int[] a, boolean[] result, int... v) {
//
//        int length = a.length;
//        int nv = v.length;
//
//        if (result == null) {
//            result = new boolean[length];
//        }
//
//        for (int i = 0; i < length; i++) {
//            result[i] = false;
//            for (int aV : v) {
//                if (a[i] == aV) {
//                    result[i] = true;
//                    break;
//                }
//            }
//        }
//
//        return result;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:39 PM)

// --Commented out by Inspection START (8/29/18, 12:39 PM):
//    public static boolean[] isNot(int[] a, boolean[] result, int v1) {
//
//        int length = a.length;
//
//        if (result == null) {
//            result = new boolean[length];
//        }
//
//        for (int i = 0; i < length; i++) {
//            result[i] = a[i] != v1;
//        }
//
//        return result;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:39 PM)

// --Commented out by Inspection START (8/29/18, 12:39 PM):
//    public static boolean[] isNotOneOf(int[] a, boolean[] result, int... v) {
//
//        int length = a.length;
//        int nv = v.length;
//
//        if (result == null) {
//            result = new boolean[length];
//        }
//
//        for (int i = 0; i < length; i++) {
//            result[i] = true;
//            for (int aV : v) {
//                if (a[i] == aV) {
//                    result[i] = false;
//                    break;
//                }
//            }
//        }
//
//        return result;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:39 PM)
}
