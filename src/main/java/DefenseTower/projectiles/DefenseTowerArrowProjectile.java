package DefenseTower.projectiles;

import DefenseTower.gameObject.DefenseTowerExtraObject;
import necesse.entity.mobs.Mob;
import necesse.entity.projectile.StoneArrowProjectile;
import necesse.level.gameObject.WallObject;
import necesse.level.maps.CollisionFilter;

public class DefenseTowerArrowProjectile extends StoneArrowProjectile {

    @Override
    public void init() {
        super.init();
        this.height = 64.0F;
    }

    @Override
    public boolean canHit(Mob mob) {
        return mob.isHostile;
    }

    @Override
    protected CollisionFilter getLevelCollisionFilter() {
        CollisionFilter filter =  super.getLevelCollisionFilter();
        filter.addFilter((tp) -> !(tp.object().object instanceof DefenseTowerExtraObject));
        filter.addFilter((tp) -> !(tp.object().object instanceof WallObject));
        return filter;
    }

    @Override
    public void dropItem() {
    }
}
