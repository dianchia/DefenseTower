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

public abstract class DefenseTowerExtraObject extends GameObject {
    public String lastChangedTime = "morning";
    public DefenseTowerExtraObject() {
        super(new Rectangle(32, 32));
        this.mapColor = new Color(86, 79, 79);
        this.displayMapTooltip = true;
        this.toolType = ToolType.PICKAXE;
        this.objectHealth = 100;
        this.drawDmg = false;

        this.isLightTransparent = true;
        this.lightLevel = 320;
        this.lightSat = 0.2F;
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
}
