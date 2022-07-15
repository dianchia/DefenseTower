package DefenseTower.projectiles;

import DefenseTower.events.DefenseTowerExplosionEvent;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.trails.Trail;

import java.awt.*;

public class DTCannonBallProjectile extends DTProjectile {
    private long spawnTime;

    public DTCannonBallProjectile() {
    }

    public DTCannonBallProjectile(float x, float y, float targetX, float targetY, int speed, int distance, GameDamage damage, int knockback, Mob owner) {
        this();
        this.setLevel(owner.getLevel());
        this.applyData(x, y, targetX, targetY, (float) speed, distance, damage, knockback, owner);
    }

    @Override
    public void init() {
        super.init();
        this.setWidth(15.0F);
        this.spawnTime = this.getWorldEntity().getTime();
        this.doesImpactDmg = false;
        this.trailOffset = 0.0F;
    }

    @Override
    public Trail getTrail() {
        return new Trail(this, this.getLevel(), new Color(30, 30, 30), 14.0F, 250, this.getHeight());
    }

    @Override
    public float getAngle() {
        return (float) (this.getWorldEntity().getTime() - this.spawnTime);
    }

    @Override
    protected void doHitLogic(Mob mob, float x, float y) {
        if (this.getLevel().isServerLevel()) {
            DefenseTowerExplosionEvent event = new DefenseTowerExplosionEvent(x, y, this.getDamage(), this.getOwner());
            this.getLevel().entityManager.addLevelEvent(event);
        }
    }

    @Override
    protected void playHitSound(float x, float y) {
    }
}
