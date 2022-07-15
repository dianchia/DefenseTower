package DefenseTower.projectiles;

import necesse.engine.registries.BuffRegistry;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.particle.ParticleOption;
import necesse.entity.trails.Trail;

import java.awt.*;

public class DTPoisonArrowProjectile extends DTProjectile {
    public DTPoisonArrowProjectile() {
    }

    @Override
    public void init() {
        super.init();
        this.givesLight = true;
    }

    @Override
    public Color getParticleColor() {
        return new Color(115, 146, 42);
    }

    protected void modifySpinningParticle(ParticleOption particle) {
        particle.givesLight(75.0F, 0.5F).lifeTime(1000);
    }

    public Trail getTrail() {
        return new Trail(this, this.getLevel(), new Color(71, 90, 26), 12.0F, 250, this.getHeight());
    }

    public void doHitLogic(Mob mob, float x, float y) {
        if (this.getLevel().isServerLevel()) {
            if (mob != null) {
                ActiveBuff ab = new ActiveBuff(BuffRegistry.Debuffs.SPIDER_VENOM, mob, 10.0F, this.getOwner());
                mob.addBuff(ab, true);
            }
        }
    }
}
