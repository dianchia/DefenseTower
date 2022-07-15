package DefenseTower.projectiles;

import necesse.entity.trails.Trail;

import java.awt.*;

public class DTStoneArrowProjectile extends DTProjectile {
    public DTStoneArrowProjectile() {
    }

    @Override
    public Trail getTrail() {
        return new Trail(this, this.getLevel(), new Color(150, 150, 150), 10.0F, 250, this.getHeight());
    }
}
