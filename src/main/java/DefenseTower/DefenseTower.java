package DefenseTower;

import DefenseTower.gameObject.DefenseTowerObject;
import DefenseTower.projectiles.DefenseTowerArrowProjectile;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.ProjectileRegistry;

@ModEntry
public class DefenseTower {


    public void init() {
        DefenseTowerObject.registerDefenseTower();
        ProjectileRegistry.registerProjectile("defensetowerarrow", DefenseTowerArrowProjectile.class, "stonearrow", "arrow_shadow");
    }
}
