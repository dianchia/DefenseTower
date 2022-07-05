package DefenseTower.gameObject;

import necesse.engine.registries.ObjectRegistry;
import necesse.engine.tickManager.TickManager;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.gameObject.ObjectHoverHitbox;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;
import necesse.level.maps.multiTile.MultiTile;
import necesse.level.maps.multiTile.StaticMultiTile;

import java.awt.*;
import java.util.List;

public class DefenseTowerObject extends DefenseTowerExtraObject {
    public GameTexture texture_day;
    public GameTexture texture_night;
    protected int counterIDRight;

    public DefenseTowerObject() {
    }

    @Override
    protected Rectangle getCollision(Level level, int x, int y, int rotation) {
//        return  new Rectangle();
        return new Rectangle(x * 32 + 7, y * 32, 22, 32);
    }

    @Override
    public java.util.List<ObjectHoverHitbox> getHoverHitboxes(Level level, int tileX, int tileY) {
        java.util.List<ObjectHoverHitbox> list = super.getHoverHitboxes(level, tileX, tileY);
        list.add(new ObjectHoverHitbox(tileX, tileY, 0, -32, 32, 32));
        return list;
    }

    @Override
    protected void setCounterIDs(int var1, int var2) {
        this.counterIDRight = var2;
    }

    @Override
    public MultiTile getMultiTile(int rotation) {
        return new StaticMultiTile(0, 0, 2, 1, 0, true, this.getID(), this.counterIDRight);
    }

    @Override
    public void loadTextures() {
        super.loadTextures();
        this.texture_day = GameTexture.fromFile("objects/defensetower_morning");
        this.texture_night = GameTexture.fromFile("objects/defensetower_night");
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList, Level level, int tileX, int tileY, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        GameLight light = level.getLightLevel(tileX, tileY);
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        GameTexture texture = level.getWorldEntity().isNight() ? this.texture_night : this.texture_day;

        final TextureDrawOptions options = texture.initDraw().light(light).pos(drawX, drawY - texture_day.getHeight() + 32);
        list.add(new LevelSortedDrawable(this, tileX, tileY) {
            @Override
            public int getSortY() {
                return 16;
            }

            @Override
            public void draw(TickManager tickManager) {
                options.draw();
            }
        });
    }

    @Override
    public void drawPreview(Level level, int tileX, int tileY, int rotation, float alpha, PlayerMob player, GameCamera camera) {
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        texture_day.initDraw().alpha(alpha).draw(drawX, drawY - texture_day.getHeight() + 32);
    }

    @Override
    public GameTexture generateItemTexture() {
        return this.texture_day;
    }

    public static int[] registerDefenseTower() {
        DefenseTowerObject d1;
        int id1 = ObjectRegistry.registerObject("defensetower", d1 = new DefenseTowerObject(), 10.0F, true);
        DefenseTowerObject2 d2;
        int id2 = ObjectRegistry.registerObject("defensetower2", d2 = new DefenseTowerObject2(), 10.0F, false);
        d1.setCounterIDs(id1, id2);
        d2.setCounterIDs(id1, id2);
        return new int[] {id1};
    }
}
