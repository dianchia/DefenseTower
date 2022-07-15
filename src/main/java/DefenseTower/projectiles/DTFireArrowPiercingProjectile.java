package DefenseTower.projectiles;

public class DTFireArrowPiercingProjectile extends DTFireArrowProjectile {
    public DTFireArrowPiercingProjectile() {
    }

    @Override
    public void init() {
        super.init();
        this.piercing = 10;
    }
}
