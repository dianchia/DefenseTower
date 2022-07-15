package DefenseTower.objectEntity;

import DefenseTower.mobs.DefenseTowerAttackerMob;
import necesse.engine.GameTileRange;
import necesse.engine.Screen;
import necesse.engine.control.Control;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.registries.ProjectileRegistry;
import necesse.engine.tickManager.TickManager;
import necesse.engine.util.gameAreaSearch.GameAreaStream;
import necesse.entity.mobs.*;
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

public class DefenseTowerEntity extends ObjectEntity implements Attacker {

    //region Fields
    private final long cooldown;
    private final int attackDistance;
    private final int searchDistance;
    private final float damage;
    private final String projectileStringID;
    private final GameTileRange range;
    private final Mob attacker;
    private final boolean targetBoss;
    private HudDrawElement rangeElement;
    private boolean showRange = false;
    private long cooldownTime = 0L;
    //endregion

    public DefenseTowerEntity(Level level, String type, int x, int y, String projectileStringID, int attackDistance, float damage, long cooldown, PlayerMob owner, boolean targetBoss) {
        super(level, type, x, y);

        this.projectileStringID = projectileStringID;
        this.attackDistance = attackDistance;
        this.searchDistance = attackDistance - 32;
        this.damage = damage;
        this.cooldown = cooldown;
        this.targetBoss = targetBoss;

        this.attacker = new DefenseTowerAttackerMob();
        //this.getLevel().entityManager.addMob(this.attacker, x, y);

        MultiTile multiTile = this.getObject().getMultiTile(0);
        Rectangle tileRectangle = multiTile.getTileRectangle(0, 0);
        this.range = new GameTileRange((float) (searchDistance / 32) - 1, tileRectangle);
    }

    @Override
    public void init() {
        super.init();
        if (this.rangeElement != null) this.rangeElement.remove();
        this.rangeElement = new HudDrawElement() {
            @Override
            public void addDrawables(List<SortedDrawable> list, GameCamera gameCamera, PlayerMob playerMob) {
                if (DefenseTowerEntity.this.showRange) {
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
            }
        };
        this.getLevel().hudManager.addElement(this.rangeElement);
    }

    @Override
    public void serverTick() {
        super.serverTick();
        if (this.getLevel().isServerLevel() && !this.onCooldown()) {
            GameAreaStream<Mob> mobs = this.getLevel().entityManager.mobs.streamArea(this.getPosX(), this.getPosY(), this.searchDistance)
                    .filter((target) -> target.isHostile)
                    .filter((target) -> target.getDistance(this.getPosX(), this.getPosY()) <= this.searchDistance);

            if (!this.targetBoss) mobs = mobs.filter((target) -> !target.isBoss());
            mobs.findFirst().ifPresent(this::attackMob);
            this.startCooldown();
        }
    }

    @Override
    public void onMouseHover(PlayerMob perspective, boolean debug) {
        super.onMouseHover(perspective, debug);

        String attackType = this.type.split("_")[1];
        float attackSpeed = 1000.0F / this.cooldown;

        StringTooltips tooltips = new StringTooltips(this.getObject().getDisplayName());
        if (this.targetBoss) tooltips.add(Localization.translate("itemtooltip", "defensetowertargetboss"));

        if (Screen.isKeyDown(Control.getControl("invquickmove").getKey())) {
            this.showRange = true;
            tooltips.add(Localization.translate("defensetower", "attackstats", "damage", this.damage));
            tooltips.add(Localization.translate("defensetower", "attacktype", "type", attackType));
            tooltips.add(Localization.translate("defensetower", "attackspeed", "speed", String.format("%.2f", attackSpeed)));
            tooltips.add(Localization.translate("defensetower", "rangestats", "range", (this.attackDistance / 32) - 2));
        } else {
            this.showRange = false;
            tooltips.add(Localization.translate("defensetower", "pressshift"));
        }
        Screen.addTooltip(tooltips);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.rangeElement != null) this.rangeElement.remove();
        if (this.attacker != null) this.attacker.remove();
    }

    private boolean onCooldown() {
        return this.cooldownTime > this.getWorldEntity().getTime();
    }

    private void startCooldown() {
        this.cooldownTime = this.getWorldEntity().getTime() + this.cooldown;
    }

    private void attackMob(Mob target) {
        if (target.isVisible() && !target.removed() && this.isSamePlace(target)) {
            GameDamage damage = new GameDamage(GameDamage.DamageType.RANGED, this.damage);
            Projectile projectile;
            if (projectileStringID.contains("cannonball")) {
                projectile = ProjectileRegistry.getProjectile(this.projectileStringID, this.getLevel(), this.getPosX(), this.getPosY(), target.x, target.y, 200.0F, this.attackDistance + 96, damage, 50, null);
            } else {
                projectile = ProjectileRegistry.getProjectile(this.projectileStringID, this.getLevel(), this.getPosX(), this.getPosY(), target.x, target.y, 200.0F, this.attackDistance + 96, damage, null);
            }
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

    @Override
    public GameMessage getAttackerName() {
        return new LocalMessage("defensetower", "attackername");
    }

    @Override
    public DeathMessageTable getDeathMessages() {
        return this.getDeathMessages("explosion", 3);
    }

    @Override
    public Mob getFirstAttackOwner() {
        return this.attacker;
    }
}
