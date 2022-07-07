package DefenseTower.gameObject;

import necesse.engine.localization.Localization;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.GameObject;
import necesse.level.gameObject.ObjectHoverHitbox;
import necesse.level.maps.Level;

import java.awt.*;
import java.util.Objects;

public abstract class DefenseTowerExtraObject extends GameObject {
    public String lastChangedTime = "morning";
    protected GameTexture texture_day;
    protected GameTexture texture_night;

    public DefenseTowerExtraObject() {
        super(new Rectangle(32, 32));
        this.mapColor = new Color(107, 107, 107);
        this.displayMapTooltip = true;
        this.toolType = ToolType.PICKAXE;
        this.objectHealth = 100;
        this.drawDmg = false;
        this.stackSize = 10;

        this.isLightTransparent = true;
        this.lightLevel = 200;
        this.lightSat = 0.2F;
        this.lightHue = 60F;
        this.roomProperties.add("light");
    }

    protected abstract void setCounterIDs(int var1, int var2);

    @Override
    public abstract ObjectEntity getNewObjectEntity(Level level, int x, int y);

    @Override
    public java.util.List<ObjectHoverHitbox> getHoverHitboxes(Level level, int tileX, int tileY) {
        java.util.List<ObjectHoverHitbox> list = super.getHoverHitboxes(level, tileX, tileY);
        list.add(new ObjectHoverHitbox(tileX, tileY, 0, -32, 32, 32));
        return list;
    }

    @Override
    public boolean canInteract(Level level, int x, int y, PlayerMob player) {
        return false;
    }

    @Override
    public ListGameTooltips getItemTooltips(InventoryItem item, PlayerMob perspective) {
        ListGameTooltips tooltips = super.getItemTooltips(item, perspective);
        tooltips.add(Localization.translate("itemtooltip", "defensetowertip"));
        return tooltips;
    }

    @Override
    public int getLightLevel(Level level, int x, int y) {
        return this.lightLevel;
//        return level.getWorldEntity().isNight() ? this.lightLevel : 0;
    }

    @Override
    public void tick(Level level, int x, int y) {
        super.tick(level, x, y);
        if (this.shouldUpdateLight(level)) {
            Rectangle rect = this.getMultiTile(0).getTileRectangle(x, y);
            level.lightManager.updateStaticLight(rect.x, rect.y, rect.x + rect.width - 1, rect.y + rect.height - 1, true);
        }
    }

    protected boolean shouldUpdateLight(Level level) {
        if (level.getWorldEntity().isNight() && Objects.equals(this.lastChangedTime, "morning")) {
            this.lastChangedTime = "night";
            return true;
        } else if (!level.getWorldEntity().isNight() && Objects.equals(this.lastChangedTime, "night")) {
            this.lastChangedTime = "morning";
            return true;
        }
        return false;
    }
}
