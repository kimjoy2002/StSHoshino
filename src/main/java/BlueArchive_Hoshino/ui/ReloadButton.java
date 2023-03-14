package BlueArchive_Hoshino.ui;




import BlueArchive_Hoshino.actions.ReloadAction;
import BlueArchive_Hoshino.powers.FreeReloadPower;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import basemod.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.ui.buttons.CardSelectConfirmButton;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ShaderHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.ShaderHelper.Shader;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.EndTurnGlowEffect;
import com.megacrit.cardcrawl.vfx.EndTurnLongPressBarFlashEffect;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.reloadKey;
import static com.megacrit.cardcrawl.helpers.input.InputAction.TEXT_CONVERSIONS;

public class ReloadButton {
    private static final TutorialStrings tutorialStrings;
    public static final String[] MSG;
    public static final String[] LABEL;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private String label;
    public static final String END_TURN_MSG;
    public static final String ENEMY_TURN_MSG;
    private static final Color DISABLED_COLOR;
    private static final float SHOW_X;
    private static final float SHOW_Y;
    private static final float HIDE_X;
    private float current_x;
    private float current_y;
    private float target_x;
    private boolean isHidden;
    public boolean enabled;
    private boolean isDisabled;
    private Color textColor;
    private ArrayList<EndTurnGlowEffect> glowList;
    private static final float GLOW_INTERVAL = 1.2F;
    private float glowTimer;
    public boolean isGlowing;
    public boolean isWarning;
    private Hitbox hb;
    private float holdProgress;
    private static final float HOLD_DUR = 0.4F;
    private Color holdBarColor;

    EndTurnButton endTurnButton;

    public ReloadButton(EndTurnButton endTurnButton) {
        this.label = TEXT[0];
        this.current_x = HIDE_X;
        this.current_y = SHOW_Y;
        this.target_x = this.current_x;
        this.isHidden = true;
        this.enabled = false;
        this.isDisabled = false;
        this.glowList = new ArrayList();
        this.glowTimer = 0.0F;
        this.isGlowing = false;
        this.isWarning = false;
        this.hb = new Hitbox(0.0F, 0.0F, 230.0F * Settings.scale, 110.0F * Settings.scale);
        this.holdProgress = 0.0F;
        this.holdBarColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);
        this.endTurnButton = endTurnButton;
    }

    public int getKeyCode() {
        return reloadKey;
    }
    public boolean isJustPressed() {
        return Gdx.input.isKeyJustPressed(getKeyCode());
    }

    public boolean isPressed() {
        return Gdx.input.isKeyPressed(getKeyCode());
    }

    public String getKeyString() {
        String keycodeStr = Input.Keys.toString(getKeyCode());
        return (String)TEXT_CONVERSIONS.getOrDefault(keycodeStr, keycodeStr);
    }

    public void update() {
        if (this.endTurnButton.enabled) {
            if(ableUse()) {
                this.enable();
            }
            else {
                this.disable();
            }
        }

        if (!this.endTurnButton.enabled) {
            this.disable();
        }


        this.glow();
        this.updateHoldProgress();
        if (this.current_x != this.target_x) {
            this.current_x = MathUtils.lerp(this.current_x, this.target_x, Gdx.graphics.getDeltaTime() * 9.0F);
            if (Math.abs(this.current_x - this.target_x) < Settings.UI_SNAP_THRESHOLD) {
                this.current_x = this.target_x;
            }
        }

        this.hb.move(this.current_x, this.current_y);
        if (this.enabled) {
            this.isDisabled = false;
            if (AbstractDungeon.isScreenUp || AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.inSingleTargetMode) {
                this.isDisabled = true;
            }

            if (AbstractDungeon.player.hoveredCard == null) {
                this.hb.update();
            }

            if (!Settings.USE_LONG_PRESS && InputHelper.justClickedLeft && this.hb.hovered && !this.isDisabled && !AbstractDungeon.isScreenUp) {
                this.hb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            }

            if (this.hb.hovered && !this.isDisabled && !AbstractDungeon.isScreenUp) {
                this.isWarning = this.showWarning();
                if (this.hb.justHovered && AbstractDungeon.player.hoveredCard == null) {
                    CardCrawlGame.sound.play("UI_HOVER");
                    Iterator var1 = AbstractDungeon.player.hand.group.iterator();

                    while(var1.hasNext()) {
                        AbstractCard c = (AbstractCard)var1.next();
                        if (c.isGlowing) {
                            c.superFlash(c.glowColor);
                        }
                    }
                }
            }
        }

        if (this.holdProgress == 0.4F && !this.isDisabled && !AbstractDungeon.isScreenUp) {
            this.disable(true);
            this.holdProgress = 0.0F;
            AbstractDungeon.effectsQueue.add(new EndTurnLongPressBarFlashEffect());
        }

        if ((!Settings.USE_LONG_PRESS || !Settings.isControllerMode && !isPressed())
                && (this.hb.clicked || (isJustPressed() || CInputActionSet.proceed.isJustPressed()) && !this.isDisabled && this.enabled)) {
            this.hb.clicked = false;
            if (!this.isDisabled && !AbstractDungeon.isScreenUp) {
                this.disable(true);
            }
        }

    }

    private void updateHoldProgress() {
        if (!Settings.USE_LONG_PRESS || !Settings.isControllerMode && !InputHelper.isMouseDown) {
            this.holdProgress -= Gdx.graphics.getDeltaTime();
            if (this.holdProgress < 0.0F) {
                this.holdProgress = 0.0F;
            }

        } else {
            if ((this.hb.hovered && InputHelper.isMouseDown) && !this.isDisabled && this.enabled) {
                this.holdProgress += Gdx.graphics.getDeltaTime();
                if (this.holdProgress > 0.4F) {
                    this.holdProgress = 0.4F;
                }
            } else {
                this.holdProgress -= Gdx.graphics.getDeltaTime();
                if (this.holdProgress < 0.0F) {
                    this.holdProgress = 0.0F;
                }
            }

        }
    }

    private boolean showWarning() {
        Iterator var1 = AbstractDungeon.player.hand.group.iterator();

        AbstractCard card;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            card = (AbstractCard)var1.next();
        } while(!card.isGlowing);

        return true;
    }

    public void enable() {
        this.enabled = true;
        this.updateText(END_TURN_MSG);
    }

    public boolean ableUse() {
        if((FreeReloadPower.freeReload == true || EnergyPanel.totalCount > 0) && BulletSubscriber.getBullet() < BulletSubscriber.getMaxBullet()) {
            return true;
        }
        return false;
    }

    public void disable(boolean isEnemyTurn) {
        AbstractDungeon.actionManager.addToBottom(new ReloadAction(1));
    }

    public void disable() {
        this.enabled = false;
        this.hb.hovered = false;
        this.isGlowing = false;
    }

    public void updateText(String msg) {
        this.label = msg;
    }

    private void glow() {
        if (this.isGlowing && !this.isHidden) {
            if (this.glowTimer < 0.0F) {
                this.glowList.add(new EndTurnGlowEffect());
                this.glowTimer = 1.2F;
            } else {
                this.glowTimer -= Gdx.graphics.getDeltaTime();
            }
        }

        Iterator<EndTurnGlowEffect> i = this.glowList.iterator();

        while(i.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)i.next();
            e.update();
            if (e.isDone) {
                i.remove();
            }
        }

    }

    public void hide() {
        if (!this.isHidden) {
            this.target_x = HIDE_X;
            this.isHidden = true;
        }

    }

    public void show() {
        if (this.isHidden) {
            this.target_x = SHOW_X;
            this.isHidden = false;
            if (this.isGlowing) {
                this.glowTimer = -1.0F;
            }
        }

    }

    public void render(SpriteBatch sb) {
        if (!Settings.hideEndTurn) {
            float tmpY = this.current_y;
            this.renderHoldEndTurn(sb);
            if (!this.isDisabled && this.enabled) {
                if (this.hb.hovered) {
                    if (this.isWarning) {
                        this.textColor = Settings.RED_TEXT_COLOR;
                    } else {
                        this.textColor = Color.CYAN;
                    }
                } else if (this.isGlowing) {
                    this.textColor = Settings.GOLD_COLOR;
                } else {
                    this.textColor = Settings.CREAM_COLOR;
                }

                if (this.hb.hovered && !AbstractDungeon.isScreenUp && !Settings.isTouchScreen) {
                    TipHelper.renderGenericTip(this.current_x - 90.0F * Settings.scale, this.current_y + 300.0F * Settings.scale, LABEL[0] + " (" + getKeyString() + ")", MSG[0] );
                }
            } else if (this.label.equals(ENEMY_TURN_MSG)) {
                this.textColor = Settings.CREAM_COLOR;
            } else {
                this.textColor = Color.LIGHT_GRAY;
            }

            if (this.hb.clickStarted && !AbstractDungeon.isScreenUp) {
                tmpY -= 2.0F * Settings.scale;
            } else if (this.hb.hovered && !AbstractDungeon.isScreenUp) {
                tmpY += 2.0F * Settings.scale;
            }

            if (!this.enabled) {
                ShaderHelper.setShader(sb, Shader.GRAYSCALE);
            } else if (!this.isDisabled && (!this.hb.clickStarted || !this.hb.hovered)) {
                sb.setColor(Color.WHITE);
            } else {
                sb.setColor(DISABLED_COLOR);
            }

            Texture buttonImg;
            if (this.isGlowing && !this.hb.clickStarted) {
                buttonImg = ImageMaster.END_TURN_BUTTON_GLOW;
            } else {
                buttonImg = ImageMaster.END_TURN_BUTTON;
            }

            if (this.hb.hovered && !this.isDisabled && !AbstractDungeon.isScreenUp) {
                sb.draw(ImageMaster.END_TURN_HOVER, this.current_x - 128.0F, tmpY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
            }

            sb.draw(buttonImg, this.current_x - 128.0F, tmpY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
            if (!this.enabled) {
                ShaderHelper.setShader(sb, Shader.DEFAULT);
            }

            this.renderGlowEffect(sb, this.current_x, this.current_y);
            if ((this.hb.hovered || this.holdProgress > 0.0F) && !this.isDisabled && !AbstractDungeon.isScreenUp) {
                sb.setBlendFunction(770, 1);
                sb.setColor(Settings.HALF_TRANSPARENT_WHITE_COLOR);
                sb.draw(buttonImg, this.current_x - 128.0F, tmpY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
                sb.setBlendFunction(770, 771);
            }

            if (Settings.isControllerMode && this.enabled) {
                sb.setColor(Color.WHITE);
                sb.draw(CInputActionSet.proceed.getKeyImg(), this.current_x - 32.0F - 42.0F * Settings.scale - FontHelper.getSmartWidth(FontHelper.panelEndTurnFont, this.label, 99999.0F, 0.0F) / 2.0F, tmpY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }

            FontHelper.renderFontCentered(sb, FontHelper.panelEndTurnFont, this.label, this.current_x - 0.0F * Settings.scale, tmpY - 3.0F * Settings.scale, this.textColor);
            if (!this.isHidden) {
                this.hb.render(sb);
            }
        }

    }

    private void renderHoldEndTurn(SpriteBatch sb) {
        if (Settings.USE_LONG_PRESS) {
            this.holdBarColor.r = 0.0F;
            this.holdBarColor.g = 0.0F;
            this.holdBarColor.b = 0.0F;
            this.holdBarColor.a = this.holdProgress * 1.5F;
            sb.setColor(this.holdBarColor);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, this.current_x - 107.0F * Settings.scale, this.current_y + 53.0F * Settings.scale - 7.0F * Settings.scale, 525.0F * Settings.scale * this.holdProgress + 14.0F * Settings.scale, 20.0F * Settings.scale);
            this.holdBarColor.r = this.holdProgress * 2.5F;
            this.holdBarColor.g = 0.6F + this.holdProgress;
            this.holdBarColor.b = 0.6F;
            this.holdBarColor.a = 1.0F;
            sb.setColor(this.holdBarColor);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, this.current_x - 100.0F * Settings.scale, this.current_y + 53.0F * Settings.scale, 525.0F * Settings.scale * this.holdProgress, 6.0F * Settings.scale);
        }
    }

    private void renderGlowEffect(SpriteBatch sb, float x, float y) {
        Iterator var4 = this.glowList.iterator();

        while(var4.hasNext()) {
            EndTurnGlowEffect e = (EndTurnGlowEffect)var4.next();
            e.render(sb, x, y);
        }

    }

    static {
        tutorialStrings = CardCrawlGame.languagePack.getTutorialString("BlueArchive_Hoshino:ReloadButtonUITip");
        MSG = tutorialStrings.TEXT;
        LABEL = tutorialStrings.LABEL;
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:ReloadButtonUI"); //CardCrawlGame.languagePack.getUIString("End Turn Button");
        TEXT = uiStrings.TEXT;
        END_TURN_MSG = TEXT[0];
        ENEMY_TURN_MSG = TEXT[1];
        DISABLED_COLOR = new Color(0.7F, 0.7F, 0.7F, 1.0F);
        SHOW_X = 1640.0F * Settings.xScale;
        SHOW_Y = 215.0F * Settings.yScale;
        HIDE_X = SHOW_X + 500.0F * Settings.xScale;
    }
}