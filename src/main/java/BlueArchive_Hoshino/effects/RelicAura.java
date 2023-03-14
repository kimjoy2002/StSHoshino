package BlueArchive_Hoshino.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class RelicAura extends AbstractGameEffect {
    private float x;
    private float y;
    private float vY;
    private TextureAtlas.AtlasRegion img;
    public static boolean switcher = true;


    public RelicAura(AbstractMonster m,  String color) {
        this.img = ImageMaster.EXHAUST_L;
        this.duration = 2.0F;
        this.scale = MathUtils.random(1.7F, 1.5F) * Settings.scale;
        if (color.equals("Red")) {
            this.color = new Color(MathUtils.random(0.6F, 0.7F), MathUtils.random(0.0F, 0.1F), MathUtils.random(0.1F, 0.2F), 0.0F);
        } else if (color.equals("Green")) {
            this.color = new Color(MathUtils.random(0.1F, 0.2F), MathUtils.random(0.6F, 0.7F), MathUtils.random(0.1F, 0.2F), 0.0F);
        } else if (color.equals("Blue")) {
            this.scale = MathUtils.random(3.7F, 4.5F) * Settings.scale;
            this.color = new Color(MathUtils.random(0.1F, 0.2F), MathUtils.random(0.1F, 0.2F), MathUtils.random(0.6F, 0.7F), 0.0F);
        }else {
            this.color = new Color(MathUtils.random(0.6F, 0.7F), MathUtils.random(0.6F, 0.7F), MathUtils.random(0.6F, 0.7F), 0.0F);
        }

        this.x = m.hb.cX + MathUtils.random(-m.hb.width / 16.0F, m.hb.width / 16.0F);
        this.y = m.hb.cY + MathUtils.random(-m.hb.height / 16.0F, m.hb.height / 12.0F);
        this.x -= (float)this.img.packedWidth / 2.0F;
        this.y -= (float)this.img.packedHeight / 2.0F;
        switcher = !switcher;
        this.renderBehind = true;
        this.rotation = MathUtils.random(360.0F);
        if (switcher) {
            this.renderBehind = true;
            this.vY = MathUtils.random(0.0F, 40.0F);
        } else {
            this.renderBehind = false;
            this.vY = MathUtils.random(0.0F, -40.0F);
        }
    }

    public void update() {
        if (this.duration > 1.0F) {
            this.color.a = Interpolation.fade.apply(0.3F, 0.0F, this.duration - 1.0F);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 0.3F, this.duration);
        }

        this.rotation += Gdx.graphics.getDeltaTime() * this.vY;
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
