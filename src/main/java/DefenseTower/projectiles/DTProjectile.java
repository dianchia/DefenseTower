package DefenseTower.projectiles;

import DefenseTower.gameObject.DefenseTowerExtraObject;
import necesse.engine.Screen;
import necesse.engine.sound.SoundEffect;
import necesse.engine.tickManager.TickManager;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.projectile.Projectile;
import necesse.gfx.GameResources;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawables.EntityDrawable;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.level.gameObject.*;
import necesse.level.maps.CollisionFilter;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.util.List;

public class DTProjectile extends Projectile {
    public DTProjectile() {
    }

    @Override
    public void init() {
        super.init();
        this.height = 64.0F;
        this.heightBasedOnDistance = true;
        this.setWidth(8.0F);
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables orderableDrawables, OrderableDrawables orderableDrawables1, OrderableDrawables orderableDrawables2, Level level, TickManager tickManager, GameCamera gameCamera, PlayerMob playerMob) {
        if (!this.removed()) {
            GameLight light = level.getLightLevel(this);
            int drawX = gameCamera.getDrawX(this.x) - this.texture.getWidth() / 2;
            int drawY = gameCamera.getDrawY(this.y);
            final TextureDrawOptions options = this.texture.initDraw().light(light).rotate(this.getAngle(), this.texture.getWidth() / 2, 0).pos(drawX, drawY - (int) this.getHeight());

            list.add(new EntityDrawable(this) {
                @Override
                public void draw(TickManager tickManager) {
                    options.draw();
                }
            });
            this.addShadowDrawables(orderableDrawables, drawX, drawY, light, this.getAngle(), 0);
        }
    }

    @Override
    public boolean canHit(Mob mob) {
        return mob.isHostile;
    }

    @Override
    protected CollisionFilter getLevelCollisionFilter() {
        CollisionFilter filter = super.getLevelCollisionFilter();
        filter.addFilter((tp) -> {
            GameObject object = tp.object().object;
            return !(object instanceof DefenseTowerExtraObject || object instanceof WallObject || object instanceof FenceObject || object instanceof DoorObject);
        });
        return filter;
    }

    @Override
    public void dropItem() {
    }

    @Override
    protected void playHitSound(float x, float y) {
        Screen.playSound(GameResources.bowhit, SoundEffect.effect(x, y));
    }
}
