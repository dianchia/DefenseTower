package DefenseTower.objectEntity;

import necesse.engine.registries.ProjectileRegistry;
import necesse.engine.util.gameAreaSearch.GameAreaStream;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.entity.projectile.Projectile;
import necesse.level.maps.Level;

public class DefenseTowerEntity extends ObjectEntity {

    public long cooldown = 1000L;
    public long cooldownTime = 0L;
    public int attackDistance;

    public DefenseTowerEntity(Level level, String type, int x, int y) {
        super(level, type, x, y);
        attackDistance = 256 + 64;
    }

    @Override
    public void serverTick() {
        super.serverTick();
        if (this.getLevel().isServerLevel() && !this.onCooldown()) {
            GameAreaStream<Mob> mobs = this.getLevel().entityManager.streamAreaMobsAndPlayers((int)this.x * 32, (int)this.y * 32, this.attackDistance - 32).filter((mob) -> mob.isHostile);
            mobs.findFirst().ifPresent(this::attackMob);
            this.startCooldown();
        }
    }

    private boolean onCooldown() {
        return this.cooldownTime > this.getWorldEntity().getTime();
    }

    private void startCooldown() {
        this.cooldownTime = this.getWorldEntity().getTime() + this.cooldown;
    }

    private void attackMob(Mob target) {
        if (target.isVisible() && this.isSamePlace(target)){
            GameDamage damage = new GameDamage(GameDamage.DamageType.RANGED, 50.0F);
            Projectile projectile = ProjectileRegistry.getProjectile("defensetowerarrow", this.getLevel(), this.x * 32 + 32, this.y * 32 - 16, target.x, target.y, 200.0F, this.attackDistance + 96, damage, null);
            projectile.setTargetPrediction(target, -10.0F);
            projectile.moveDist(10.0);
            this.getLevel().entityManager.projectiles.add(projectile);
        }
    }
}
