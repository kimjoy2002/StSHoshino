package BlueArchive_Aris.events.Object;

import BlueArchive_Aris.events.SaibaMidoriEvent;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Paint {
    private int x;
    private int y;
    private static Texture canvas = new Texture("BlueArchive_ArisResources/images/events/Paint.png");

    public static FileHandle saveHandle;
    Pixmap pixmap = null;
    private Texture texture;

    public int size = 3;
    public int drawCount = 0;
    public boolean isDrawing = false;
    private Vector2 lastPos;
    private float scale;
    private int size_x;
    private int size_y;
    private SaibaMidoriEvent event;

    private Set<Color> usedColor = new HashSet<Color>();

    private Color color_white = new Color(1.0F, 1.0F, 1.0F, 1.0F);

    private Color color = new Color(0.0F, 0.0F, 0.0F, 1.0F);

    private Color paintColorList[] = {
            Color.BLACK,
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            CardHelper.getColor(255.0f, 233.0f, 212.0f),
            Color.WHITE,
            Color.GREEN,
            Color.BLUE,
            Color.PURPLE,
            Color.PINK
    };

    private List<PaintColor> paintColors = new ArrayList<PaintColor>();


    public Paint( SaibaMidoriEvent event, int size_x, int size_y, int x, int y) {
        this.event = event;
        this.scale = Settings.scale;
        this.size_x = (int) (size_x*scale);
        this.size_y = (int) (size_y*scale);

        this.x = x;
        this.y = y;
        pixmap = new Pixmap(this.size_x, this.size_y, Pixmap.Format.RGBA8888 );
        this.texture = new Texture(this.pixmap);
        setColor(color_white);
        int w_ = size_x/(paintColorList.length/2);
        for(int i = 0; i < paintColorList.length; i ++) {
            int x_ = x - size_x/2 + w_ * (i%(paintColorList.length/2));
            int y_ = y - size_y/2 - w_ * (i/(paintColorList.length/2));
            paintColors.add(new PaintColor(this, x_, y_, paintColorList[i]));
        }
        paintColors.add(new PaintColor(this, x - size_x/2+w_*(paintColorList.length/2), y - size_y/2, true));
        paintColors.add(new PaintColor(this, x - size_x/2+w_*(paintColorList.length/2), y - size_y/2- w_, false));

        this.pixmap.fillRectangle(0, 0, size_x, size_y);
        setColor(new Color(0.0F, 0.0F, 0.0F, 1.0F));
    }

    private void draw(Vector2 dot) {
        if(dot.x>0 && dot.y>0 && dot.x < size_x && dot.y < size_y) {
            this.pixmap.fillCircle((int)dot.x, (int)dot.y, this.size);
            isDrawing = true;
            usedColor.add(color);
            drawCount++;
        }
    }

    public void setColor(Color newColor) {
        this.color = newColor;
        this.pixmap.setColor(this.color);
    }
    public void endPaint() {
        Pixmap pixmap500 = new Pixmap(500, 380, pixmap.getFormat());
        pixmap500.drawPixmap(pixmap,
                0, 0, pixmap.getWidth(), pixmap.getHeight(),
                0, 0, pixmap500.getWidth(), pixmap500.getHeight()
        );
        PixmapIO.writePNG(saveHandle, pixmap500);
        event.isDrawing = isDrawing;
        if(usedColor.size() > 3 && drawCount > 15000) {
            event.isMasterPiece = true;
        }
        event.screen = SaibaMidoriEvent.CUR_SCREEN.CLEAN_UP;
    }


    private void updateTexture() {
        this.texture.dispose();
        this.texture = new Texture(this.pixmap);
    }
    public void update() {

        for(PaintColor paintColor : paintColors) {
            paintColor.update();
        }

        if (InputHelper.justClickedLeft) {
            Pixmap.setBlending(Pixmap.Blending.None);
        }

        if (!InputHelper.isMouseDown) {
            if (InputHelper.justReleasedClickLeft) {
                this.lastPos = null;
                Pixmap.setBlending(Pixmap.Blending.SourceOver);
            }
        } else {
            InputHelper.isMouseDown = false;
            InputHelper.justClickedLeft = false;

            int y_dif = y - Settings.HEIGHT/2;
            Vector2 pos = new Vector2((float)InputHelper.mX - (Settings.WIDTH - size_x)/2, (float)(Settings.HEIGHT - InputHelper.mY)+y_dif-(Settings.HEIGHT-size_y)/2);
            if (this.lastPos == null) {
                this.draw(pos);
                this.updateTexture();
                this.texture = new Texture(this.pixmap);
            } else if (!pos.equals(this.lastPos)) {
                float step = (float)this.size / (16.0F * pos.dst(this.lastPos));

                for(float a = 0.0F; a < 1.0F; a += step) {
                    this.draw(pos.lerp(this.lastPos, a));
                }

                this.updateTexture();
            }

            this.lastPos = new Vector2((float)InputHelper.mX - (Settings.WIDTH - size_x)/2, (float)(Settings.HEIGHT - InputHelper.mY)+y_dif-(Settings.HEIGHT-size_y)/2);
        }

    }
    public void render(SpriteBatch sb) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        sb.setColor(color_white);
        sb.draw(canvas, this.x - (float) w / 2.0F, this.y - (float) h / 2.0F, (float) w / 2.0F, (float) h / 2.0F, (float) w, (float) h, scale, scale, 0, 0, 0, w, h, false, false);

        sb.setColor(Color.WHITE);
        sb.draw(this.texture, x-(float)size_x/2.0f, y-(float)size_y/2.0f);
        for(PaintColor paintColor : paintColors) {
            paintColor.render(sb);
        }
    }


    static {
        saveHandle = new FileHandle(SpireConfig.makeFilePath("BlueArchive_Hoshino", "CurrentRunPaint", "png"));
    }
}
