package DefenseTower.objectEntity;

import necesse.engine.GameTileRange;
import necesse.engine.Screen;
import necesse.engine.localization.Localization;
import necesse.engine.registries.ProjectileRegistry;
import necesse.engine.tickManager.TickManager;
import necesse.engine.util.gameAreaSearch.GameAreaStream;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.entity.projectile.Projectile;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.SharedTextureDrawOptions;
import necesse.gfx.drawables.SortedDrawable;
import necesse.gfx.gameTooltips.StringTooltips;
import necesse.level.maps.Level;
import necesse.level.maps.hudManager.HudDrawElement;
import necesse.level.maps.multiTile.MultiTile;

import java.awt.*;
import java.util.List;

public class DefenseTowerEntity extends ObjectEntity {

    public long cooldown = 1000L;
    public long cooldownTime = 0L;
    public int attackDistance;
    public int searchDistance;
    public float damage;

    public GameTileRange range;
    public HudDrawElement rangeElement;

    public DefenseTowerEntity(Level level, String type, int x, int y) {
        super(level, type, x, y);
        attackDistance = 512;
        searchDistance = attackDistance - 32;
        damage = 50.0F;
        MultiTile multiTile = this.getObject().getMultiTile(0);
        Rectangle tileRectangle = multiTile.getTileRectangle(0, 0);
        this.range = new GameTileRange((float)(searchDistance / 32) - 1, tileRectangle);
    }

    @Override
    public void init() {
        super.init();
        if (this.rangeElement != null) this.rangeElement.remove();
        this.rangeElement = new HudDrawElement() {
            @Override
            public void addDrawables(List<SortedDrawable> list, GameCamera gameCamera, PlayerMob playerMob) {
                final SharedTextureDrawOptions options = DefenseTowerEntity.this.range.getDrawOptions(new Color(255, 255, 255, 120), new Color(255, 255, 255, 50), DefenseTowerEntity.this.getTileX(), DefenseTowerEntity.this.getTileY(), gameCamera);
                if (options != null) {
                    list.add(new SortedDrawable() {
                        @Override
                        public int getPriority() {
                            return -1000000;
                        }

                        @Override
                        public void draw(TickManager tickManager) {
                            options.draw();
                        }
                    });
                }
            }
        };
//        this.getLevel().hudManager.addElement(this.rangeElement);
    }

    @Override
    public void serverTick() {
        super.serverTick();
        if (this.getLevel().isServerLevel() && !this.onCooldown()) {
            this.getLevel().entityManager.mobs.streamArea(this.getPosX(), this.getPosY(), this.searchDistance)
                    .filter((target) -> target.isHostile)
                    .filter((target) -> target.getDistance(this.getPosX(), this.getPosY()) <= this.searchDistance)
                    .findFirst()
                    .ifPresent(this::attackMob);
//            mobs.findFirst().ifPresent(this::attackMob);
            this.startCooldown();
        }
    }

    @Override
    public void onMouseHover(PlayerMob perspective, boolean debug) {
        super.onMouseHover(perspective, debug);
        StringTooltips tooltips = new StringTooltips(this.getObject().getDisplayName());
        tooltips.add(Localization.translate("defensetower", "attackstats", "damage", this.damage));
        tooltips.add(Localization.translate("defensetower", "rangestats", "range", this.attackDistance / 32));
        Screen.addTooltip(tooltips);
    }

    private boolean onCooldown() {
        return this.cooldownTime > this.getWorldEntity().getTime();
    }

    private void startCooldown() {
        this.cooldownTime = this.getWorldEntity().getTime() + this.cooldown;
    }

    private void attackMob(Mob target) {
        if (target.isVisible() && this.isSamePlace(target)){
            GameDamage damage = new GameDamage(GameDamage.DamageType.RANGED, this.damage);
            Projectile projectile = ProjectileRegistry.getProjectile("defensetowerarrow", this.getLevel(), this.getPosX(), this.getPosY(), target.x, target.y, 200.0F, this.attackDistance + 96, damage, null);
            projectile.setTargetPrediction(target, -10.0F);
            projectile.moveDist(10.0);
            this.getLevel().entityManager.projectiles.add(projectile);
        }
    }

    private float getPosX() {
        return this.x * 32 + 32;
    }

    private float getPosY() {
        return this.y * 32 - 16;
    }
}
