package DefenseTower.events;

import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.entity.levelEvent.explosionEvent.ExplosionEvent;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.gfx.GameResources;

import java.util.stream.Stream;

public class DefenseTowerExplosionEvent extends ExplosionEvent {
    public DefenseTowerExplosionEvent() {
        this(0.0F, 0.0F, new GameDamage(100.0F), null);
    }

    public DefenseTowerExplosionEvent(float x, float y, GameDamage damage, Mob owner) {
        super(x, y, 80, damage, false, 0, owner);
        this.sendCustomData = false;
        this.sendOwnerData = true;
    }

    @Override
    protected Stream<Mob> streamTargets() {
        Stream<Mob> targets = super.streamTargets();
        targets = targets.filter((target) -> target.isHostile);
        return targets;
    }

    @Override
    protected void playExplosionEffects() {
        SoundManager.playSound(GameResources.explosionHeavy, SoundEffect.effect(this.x, this.y).volume(2.0F).pitch(1.3F));
        this.level.getClient().startCameraShake(this.x, this.y, 400, 50, 10.0F, 10.0F, true);
    }
}
