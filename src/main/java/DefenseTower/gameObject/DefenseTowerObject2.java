package DefenseTower.gameObject;

import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.gfx.camera.GameCamera;
import necesse.level.maps.Level;
import necesse.level.maps.multiTile.MultiTile;
import necesse.level.maps.multiTile.StaticMultiTile;

import java.awt.*;

public class DefenseTowerObject2 extends DefenseTowerExtraObject {
    protected int counterIDLeft;

    public DefenseTowerObject2() {
    }

    @Override
    protected void setCounterIDs(int var1, int var2) {
        this.counterIDLeft = var1;
    }

    @Override
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return null;
    }

    @Override
    protected Rectangle getCollision(Level level, int x, int y, int rotation) {
        return new Rectangle(x * 32, y * 32, 32 - 3, 32);
    }

    @Override
    public MultiTile getMultiTile(int rotation) {
        return new StaticMultiTile(1, 0, 2, 1, 0, false, this.counterIDLeft, this.getID());
    }

    @Override
    public void onMouseHover(Level level, int x, int y, GameCamera camera, PlayerMob perspective, boolean debug) {
        ObjectEntity ent = level.entityManager.getObjectEntity(x - 1, y);
        if (ent != null) {
            ent.onMouseHover(perspective, debug);
        }
    }
}
