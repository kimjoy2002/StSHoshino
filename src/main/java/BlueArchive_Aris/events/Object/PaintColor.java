package BlueArchive_Aris.events.Object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class PaintColor {
    private Paint paint;
    private int x;
    private int y;
    private Color color;
    private Color click_color;
    private int size;
    private boolean isEraser;
    private boolean isComfirm;
    private Hitbox hb;
    private static Texture border = new Texture("BlueArchive_ArisResources/images/events/Paint_Color_Border.png");
    private static Texture colorTex = new Texture("BlueArchive_ArisResources/images/events/Paint_Color.png");
    private static Texture eraser = new Texture("BlueArchive_ArisResources/images/events/Paint_Eraser.png");
    private static Texture comfirm = new Texture("BlueArchive_ArisResources/images/events/Paint_OK.png");


    public PaintColor(Paint paint, int x, int y, Color color) {
        this.paint = paint;
        this.x = x;
        this.y = y;
        this.color = color;
        this.size = 3;
        this.isEraser = false;
        this.isComfirm = false;
        this.hb = new Hitbox(x, y-(float)colorTex.getHeight()/2.0f, border.getWidth(), border.getHeight());
    }

    public PaintColor(Paint paint, int x, int y, boolean eraser_) {
        this.paint = paint;
        this.x = x;
        this.y = y;
        this.color = Color.WHITE;
        this.size = 15;
        if(eraser_) {
            this.isEraser = true;
            this.isComfirm = false;
        } else {
            this.isEraser = false;
            this.isComfirm = true;
        }
        this.hb = new Hitbox(x, y-(float)colorTex.getHeight()/2.0f, border.getWidth(), border.getHeight());
    }


    public void update() {
        hb.update();
        if (InputHelper.justClickedLeft && this.hb.hovered) {
            this.hb.clickStarted = true;
            CardCrawlGame.sound.play("UI_CLICK_1");
        }
        if (this.hb.clicked) {
            this.hb.clicked = false;
            if(isComfirm) {
                paint.endPaint();
            } else {
                paint.size = size;
                paint.setColor(color);
            }
        }
    }
    public void render(SpriteBatch sb) {
        float y_ = y-(float)border.getHeight()/2.0f;
        if(this.hb.clickStarted) {
            y_ -= 10;
        }
        if(!isEraser && !isComfirm){
            sb.setColor(Color.WHITE);
            sb.draw(this.border, x, y_);

            sb.setColor(color);
            sb.draw(this.colorTex, x, y_);
        } else if(isEraser) {
            sb.setColor(Color.WHITE);
            sb.draw(this.eraser, x, y_);
        } else if(isComfirm) {
            sb.setColor(Color.WHITE);
            sb.draw(this.comfirm, x, y_);
        }
        this.hb.render(sb);
    }

}
