package DefenseTower.mobs;

import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.Mob;

public class DefenseTowerAttackerMob extends Mob {
    public DefenseTowerAttackerMob() {
        super(100);
        this.setTeam(-1);
        this.isHostile = false;
        this.canDespawn = false;
        this.setSpeed(0);
    }

    @Override
    public boolean canBeHit(Attacker attacker) {
        return false;
    }

    @Override
    public boolean canCollisionHit(Mob target) {
        return false;
    }
}
