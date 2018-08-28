package com.demasu.testpixeldungeon.ui;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.ui.Component;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.watabou.utils.Highlighter;

public class HighlightedText extends Component {

    private final BitmapTextMultiline normal;
    private final BitmapTextMultiline highlighted;

    protected int nColor = 0xFFFFFF;
    protected int hColor = 0xFFFF44;

    public HighlightedText(float size) {
        normal = PixelScene.createMultiline(size);
        add(normal);

        highlighted = PixelScene.createMultiline(size);
        add(highlighted);

        setColor();
    }

    @Override
    protected void layout() {
        normal.x = highlighted.x = x;
        normal.y = highlighted.y = y;
    }

    public void text(String value, int maxWidth) {
        Highlighter hl = new Highlighter(value);

        normal.text(hl.text);
        normal.maxWidth = maxWidth;
        normal.measure();

        if (hl.isHighlighted()) {
            normal.mask = hl.inverted();

            highlighted.text(hl.text);
            highlighted.maxWidth = maxWidth;
            highlighted.measure();

            highlighted.mask = hl.mask;
            highlighted.visible = true;
        } else {
            highlighted.visible = false;
        }

        width = normal.width();
        height = normal.height();
    }

    // These were variables in commit 6832ea116027 and earlier
    private void setColor() {
        normal.hardlight(0xFFFFFF);
        highlighted.hardlight(0xFFFF44);
    }
}
