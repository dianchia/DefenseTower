package DefenseTower.projectiles;

import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.particle.ParticleOption;
import necesse.entity.trails.Trail;
import necesse.level.maps.LevelObjectHit;

import java.awt.*;

public class DTFireArrowProjectile extends DTProjectile {
    public DTFireArrowProjectile() {
    }

    @Override
    public void init() {
        super.init();
        this.givesLight = true;
    }

    @Override
    public Color getParticleColor() {
        return ParticleOption.randomFlameColor();
    }

    @Override
    protected void modifySpinningParticle(ParticleOption particle) {
        particle.givesLight(0.0F, 0.5F).lifeTime(1000);
    }

    @Override
    public Trail getTrail() {
        return new Trail(this, this.getLevel(), this.getParticleColor().darker().darker(), 6.0F, 250, this.getHeight());
    }

    @Override
    public void doHitLogic(Mob mob, LevelObjectHit objectHit, float x, float y) {
        if (this.getLevel().isServer()) {
            if (mob != null) {
                ActiveBuff ab = new ActiveBuff("onfire", mob, 10.0F, this.getOwner());
                mob.addBuff(ab, true);
            }
        }
    }
}
