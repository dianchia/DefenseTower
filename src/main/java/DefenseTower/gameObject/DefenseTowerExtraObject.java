package DefenseTower.gameObject;

import DefenseTower.objectEntity.DefenseTowerEntity;
import necesse.engine.localization.Localization;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;

import java.awt.*;
import java.util.Objects;

public abstract class DefenseTowerExtraObject extends GameObject {
    public String lastChangedTime = "morning";
    public DefenseTowerExtraObject() {
        super(new Rectangle(32, 32));
        this.mapColor = new Color(86, 79, 79);
        this.displayMapTooltip = true;
        this.toolType = ToolType.PICKAXE;
        this.objectHealth = 100;
        this.drawDmg = false;
        this.stackSize = 10;

        this.isLightTransparent = true;
        this.lightLevel = 320;
        this.lightSat = 0.05F;
        this.lightHue = 80F;
        this.roomProperties.add("light");
    }

    protected abstract void setCounterIDs(int var1, int var2);

    @Override
    public boolean canInteract(Level level, int x, int y, PlayerMob player) {
        return false;
    }

    @Override
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return this.isMultiTileMaster() ? new DefenseTowerEntity(level, "defensetowerentity", x, y) : null;
    }

    @Override
    public ListGameTooltips getItemTooltips(InventoryItem item, PlayerMob perspective) {
        ListGameTooltips tooltips = super.getItemTooltips(item, perspective);
        tooltips.add(Localization.translate("itemtooltip", "defensetowertip"));
        return tooltips;
    }

    @Override
    public int getLightLevel(Level level, int x, int y) {
        return level.getWorldEntity().isNight() ? this.lightLevel : 0;
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
        }
        else if (!level.getWorldEntity().isNight() && Objects.equals(this.lastChangedTime, "night")) {
            this.lastChangedTime = "morning";
            return true;
        }
        return false;
    }
}
