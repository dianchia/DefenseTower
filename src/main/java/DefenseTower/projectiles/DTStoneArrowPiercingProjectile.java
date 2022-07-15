package DefenseTower.projectiles;

public class DTStoneArrowPiercingProjectile extends DTStoneArrowProjectile {
    public DTStoneArrowPiercingProjectile() {
    }

    @Override
    public void init() {
        super.init();
        this.piercing = 10;
    }
}
