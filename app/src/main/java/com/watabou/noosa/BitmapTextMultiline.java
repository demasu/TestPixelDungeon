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

import com.watabou.glwrap.Quad;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class BitmapTextMultiline extends BitmapText {

    private static final Pattern PARAGRAPH = Pattern.compile( "\n" );
    private static final Pattern WORD = Pattern.compile( "\\s+" );
    private int maxWidth = Integer.MAX_VALUE;
    private int nLines = 0;
    private boolean[] mask;
    private final float spaceSize;

    public BitmapTextMultiline ( Font font ) {
        this( "", font );
    }

    public BitmapTextMultiline ( String text, Font font ) {
        super( text, font );
        spaceSize = font.width( font.get( ' ' ) );
    }

    @SuppressWarnings ( { "FeatureEnvy", "MagicNumber" } )
    @Override
    protected void updateVertices () {

        if ( getText() == null ) {
            setText( "" );
        }

        setQuads( Quad.createSet( getText().length() ) );
        setRealLength( 0 );

        // This object controls lines breaking
        SymbolWriter writer = new SymbolWriter();

        // Word size
        PointF metrics = new PointF();

        String[] paragraphs = PARAGRAPH.split( getText() );

        // Current character (used in masking)
        int pos = 0;

        for ( String paragraph : paragraphs ) {

            String[] words = WORD.split( paragraph );

            for ( String word : words ) {

                if ( word.length() == 0 ) {
                    // This case is possible when there are
                    // several spaces coming along
                    continue;
                }


                getWordMetrics( word, metrics );
                writer.addSymbol( metrics.x, metrics.y );

                int length = word.length();
                float shift = 0;    // Position in pixels relative to the beginning of the word

                for ( int k = 0; k < length; k++ ) {
                    RectF rect = getFont().get( word.charAt( k ) );

                    float w = getFont().width( rect );
                    float h = getFont().height( rect );

                    if ( getMask() == null || getMask()[pos] ) {
                        getVertices()[0] = writer.getX() + shift;
                        getVertices()[1] = writer.getY();

                        getVertices()[2] = rect.left;
                        getVertices()[3] = rect.top;

                        getVertices()[4] = writer.getX() + shift + w;
                        getVertices()[5] = writer.getY();

                        getVertices()[6] = rect.right;
                        getVertices()[7] = rect.top;

                        getVertices()[8] = writer.getX() + shift + w;
                        getVertices()[9] = writer.getY() + h;

                        getVertices()[10] = rect.right;
                        getVertices()[11] = rect.bottom;

                        getVertices()[12] = writer.getX() + shift;
                        getVertices()[13] = writer.getY() + h;

                        getVertices()[14] = rect.left;
                        getVertices()[15] = rect.bottom;

                        getQuads().put( getVertices() );
                        setRealLength( getRealLength() + 1 );
                    }

                    shift += w + getFont().getTracking();

                    pos++;
                }

                writer.addSpace( spaceSize );
            }

            writer.newLine( 0, getFont().getLineHeight() );
        }

        setnLines( writer.nLines() );

        setDirty( false );
    }

    private void getWordMetrics ( String word, PointF metrics ) {

        float w = 0;
        float h = 0;

        int length = word.length();
        for ( int i = 0; i < length; i++ ) {

            RectF rect = getFont().get( word.charAt( i ) );
            w += getFont().width( rect ) + ( w > 0 ? getFont().getTracking() : 0 );
            h = Math.max( h, getFont().height( rect ) );
        }

        metrics.set( w, h );
    }

    @Override
    public void measure () {

        SymbolWriter writer = new SymbolWriter();

        PointF metrics = new PointF();

        String[] paragraphs = PARAGRAPH.split( getText() );

        for ( String paragraph : paragraphs ) {

            String[] words = WORD.split( paragraph );

            for ( int j = 0; j < words.length; j++ ) {

                if ( j > 0 ) {
                    writer.addSpace( spaceSize );
                }
                String word = words[j];
                if ( word.length() == 0 ) {
                    continue;
                }

                getWordMetrics( word, metrics );
                writer.addSymbol( metrics.x, metrics.y );
            }

            writer.newLine( 0, getFont().getLineHeight() );
        }

        width = writer.getWidth();
        height = writer.getHeight();

        setnLines( writer.nLines() );
    }

    @Override
    public float baseLine () {
        return ( height - getFont().getLineHeight() + getFont().getBaseLine() ) * scale.y;
    }

    public int getMaxWidth () {
        return maxWidth;
    }

    public void setMaxWidth ( int maxWidth ) {
        this.maxWidth = maxWidth;
    }

    public int getnLines () {
        return nLines;
    }

    private void setnLines ( int nLines ) {
        this.nLines = nLines;
    }

    private boolean[] getMask () {
        return mask;
    }

    @SuppressWarnings ( "AssignmentOrReturnOfFieldWithMutableType" )
    public void setMask ( boolean[] mask ) {
        this.mask = mask;
    }

    private class SymbolWriter {

        private float width = 0;
        private float height = 0;

        private int nLines = 0;

        private float lineWidth = 0;
        private float lineHeight = 0;

        private float x = 0;
        private float y = 0;

        void addSymbol ( float w, float h ) {
            if ( getLineWidth() > 0 && getLineWidth() + getFont().getTracking() + w > getMaxWidth() / scale.x ) {
                newLine( w, h );
            } else {

                setX( getLineWidth() );

                setLineWidth( getLineWidth() + ( getLineWidth() > 0 ? getFont().getTracking() : 0 ) + w );
                if ( h > getLineHeight() ) {
                    setLineHeight( h );
                }
            }
        }

        void addSpace ( float w ) {
            if ( getLineWidth() > 0 && getLineWidth() + getFont().getTracking() + w > getMaxWidth() / scale.x ) {
                newLine( 0, 0 );
            } else {

                setX( getLineWidth() );
                setLineWidth( getLineWidth() + ( getLineWidth() > 0 ? getFont().getTracking() : 0 ) + w );
            }
        }

        void newLine ( float w, float h ) {

            setHeight( getHeight() + getLineHeight() );
            if ( getWidth() < getLineWidth() ) {
                setWidth( getLineWidth() );
            }

            setLineWidth( w );
            setLineHeight( h );

            setX( 0 );
            setY( getHeight() );

            setnLines( getnLines() + 1 );
        }

        int nLines () {
            return getX() == 0 ? getnLines() : getnLines() + 1;
        }

        float getWidth () {
            return width;
        }

        void setWidth ( float width ) {
            this.width = width;
        }

        float getHeight () {
            return height;
        }

        void setHeight ( float height ) {
            this.height = height;
        }

        int getnLines () {
            return nLines;
        }

        void setnLines ( int nLines ) {
            this.nLines = nLines;
        }

        float getLineWidth () {
            return lineWidth;
        }

        void setLineWidth ( float lineWidth ) {
            this.lineWidth = lineWidth;
        }

        float getLineHeight () {
            return lineHeight;
        }

        void setLineHeight ( float lineHeight ) {
            this.lineHeight = lineHeight;
        }

        float getX () {
            return x;
        }

        void setX ( float x ) {
            this.x = x;
        }

        float getY () {
            return y;
        }

        void setY ( float y ) {
            this.y = y;
        }
    }

    //TODO: Split into its own file
    @SuppressWarnings ( "PublicInnerClass" )
    public class LineSplitter {

        private ArrayList<BitmapText> lines;

        private StringBuilder curLine;
        private float curLineWidth;

        private final PointF metrics = new PointF();

        private void newLine ( String str, float width ) {
            BitmapText txt = new BitmapText( curLine.toString(), getFont() );
            txt.scale.set( scale.x );
            lines.add( txt );

            curLine = new StringBuilder( str );
            curLineWidth = width;
        }

        private void append ( String str, float width ) {
            curLineWidth += ( curLineWidth > 0 ? getFont().getTracking() : 0 ) + width;
            curLine.append( str );
        }

        public ArrayList<BitmapText> split () {

            lines = new ArrayList<>();

            curLine = new StringBuilder();
            curLineWidth = 0;

            String[] paragraphs = PARAGRAPH.split( getText() );

            for ( String paragraph : paragraphs ) {

                String[] words = WORD.split( paragraph );

                for ( String word : words ) {

                    if ( word.length() == 0 ) {
                        continue;
                    }

                    getWordMetrics( word, metrics );

                    if ( curLineWidth > 0 && curLineWidth + getFont().getTracking() + metrics.x > getMaxWidth() / scale.x ) {
                        newLine( word, metrics.x );
                    } else {
                        append( word, metrics.x );
                    }

                    if ( curLineWidth > 0 && curLineWidth + getFont().getTracking() + spaceSize > getMaxWidth() / scale.x ) {
                        newLine( "", 0 );
                    } else {
                        append( " ", spaceSize );
                    }
                }

                newLine( "", 0 );
            }

            //noinspection AssignmentOrReturnOfFieldWithMutableType
            return lines;
        }
    }
}
