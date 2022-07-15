package DefenseTower.projectiles;

public class DTPoisonArrowPiercingProjectile extends DTPoisonArrowProjectile {
    public DTPoisonArrowPiercingProjectile() {
    }

    @Override
    public void init() {
        super.init();
        this.piercing = 10;
    }
}
