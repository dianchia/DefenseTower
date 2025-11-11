package DefenseTower.gameObject;

import DefenseTower.objectEntity.DefenseTowerEntity;
import necesse.engine.localization.Localization;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.GameObject;
import necesse.level.gameObject.ObjectHoverHitbox;
import necesse.level.maps.Level;

import java.awt.*;
import java.util.List;

public abstract class DefenseTowerExtraObject extends GameObject {
    public DefenseTowerExtraObject() {
        super(new Rectangle(32, 32));
        this.mapColor = new Color(107, 107, 107);
        this.displayMapTooltip = true;
        this.toolType = ToolType.PICKAXE;
        this.objectHealth = 100;
        this.isLightTransparent = true;
        this.hoverHitbox = new Rectangle(0, -32, 32, 64);
        this.lightLevel = 200;
        this.lightSat = 0.2F;
        this.lightHue = 60F;
        this.roomProperties.add("light");
    }

    protected abstract void setCounterIDs(int var1, int var2);

    @Override
    public boolean canInteract(Level level, int x, int y, PlayerMob player) {
        return false;
    }

    @Override
    public abstract ObjectEntity getNewObjectEntity(Level level, int x, int y);

    @Override
    public List<ObjectHoverHitbox> getHoverHitboxes(Level level, int layerID, int tileX, int tileY) {
        List<ObjectHoverHitbox> list = super.getHoverHitboxes(level, layerID, tileX, tileY);
        list.add(new ObjectHoverHitbox(layerID, tileX, tileY, 0, -32, 32, 32));
        return list;
    }

    @Override
    public ListGameTooltips getItemTooltips(InventoryItem item, PlayerMob perspective) {
        ListGameTooltips tooltips = super.getItemTooltips(item, perspective);
        tooltips.add(Localization.translate("itemtooltip", "defensetowertip"));
        return tooltips;
    }

    @Override
    public int getLightLevel(Level level, int layerID, int tileX, int tileY) {
        return this.isActive(level, tileX, tileY) ? 0 : this.lightLevel;
    }

    @Override
    public void onWireUpdate(Level level, int layerID, int tileX, int tileY, int wireID, boolean active) {
        Rectangle rect = this.getMultiTile(0).getTileRectangle(tileX, tileY);
        level.lightManager.updateStaticLight(rect.x, rect.y, rect.x + rect.width - 1, rect.y + rect.height - 1, true);
    }

    protected boolean isActive(Level level, int x, int y) {
        return this.getMultiTile(0).streamIDs(x, y).noneMatch((c) -> level.wireManager.isWireActiveAny(c.tileX, c.tileY));
    }
}
