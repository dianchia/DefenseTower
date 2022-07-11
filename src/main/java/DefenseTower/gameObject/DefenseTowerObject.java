package DefenseTower.gameObject;

import DefenseTower.objectEntity.DefenseTowerEntity;
import necesse.engine.Screen;
import necesse.engine.control.Control;
import necesse.engine.localization.Localization;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.tickManager.TickManager;
import necesse.engine.util.Zoning;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.level.maps.Level;
import necesse.level.maps.levelData.settlementData.SettlementLevelData;
import necesse.level.maps.light.GameLight;
import necesse.level.maps.multiTile.MultiTile;
import necesse.level.maps.multiTile.StaticMultiTile;

import java.awt.*;
import java.util.List;

public class DefenseTowerObject extends DefenseTowerExtraObject {
    private final String towerType;
    private final float damage;
    private final long cooldown;
    protected int counterIDRight;
    private PlayerMob owner;

    public DefenseTowerObject(String towerType, float damage, long cooldown) {
        this.towerType = towerType;
        this.damage = damage;
        this.cooldown = cooldown;
        this.owner = null;
    }

    public static void registerDefenseTower() {
        DefenseTowerObject stoneTower1 = new DefenseTowerObject("stone", 35.0F, 700);
        DefenseTowerObject2 stoneTower2 = new DefenseTowerObject2();
        DefenseTowerObject fireTower1 = new DefenseTowerObject("fire", 50.0F, 1000);
        DefenseTowerObject2 fireTower2 = new DefenseTowerObject2();
        DefenseTowerObject poisonTower1 = new DefenseTowerObject("poison", 50.0F, 1000);
        DefenseTowerObject2 poisonTower2 = new DefenseTowerObject2();
        DefenseTowerObject cannonballTower1 = new DefenseTowerObject("cannonball", 100.0F, 2000);
        DefenseTowerObject2 cannonballTower2 = new DefenseTowerObject2();

        int stoneID1 = ObjectRegistry.registerObject("defensetowerstone", stoneTower1, 10.0F, true);
        int stoneID2 = ObjectRegistry.registerObject("defensetowerstone2", stoneTower2, 10.0F, false);
        int fireID1 = ObjectRegistry.registerObject("defensetowerfire", fireTower1, 10.0F, true);
        int fireID2 = ObjectRegistry.registerObject("defensetowerfire2", fireTower2, 10.0F, false);
        int poisonID1 = ObjectRegistry.registerObject("defensetowerpoison", poisonTower1, 10.0F, true);
        int poisonID2 = ObjectRegistry.registerObject("defensetowerpoison2", poisonTower2, 10.0F, false);
        int cannonballID1 = ObjectRegistry.registerObject("defensetowercannonball", cannonballTower1, 10.0F, true);
        int cannonballID2 = ObjectRegistry.registerObject("defensetowercannonball2", cannonballTower2, 10.0F, false);

        stoneTower1.setCounterIDs(stoneID1, stoneID2);
        stoneTower2.setCounterIDs(stoneID1, stoneID2);
        fireTower1.setCounterIDs(fireID1, fireID2);
        fireTower2.setCounterIDs(fireID1, fireID2);
        poisonTower1.setCounterIDs(poisonID1, poisonID2);
        poisonTower2.setCounterIDs(poisonID1, poisonID2);
        cannonballTower1.setCounterIDs(cannonballID1, cannonballID2);
        cannonballTower2.setCounterIDs(cannonballID1, cannonballID2);
    }

    @Override
    public void loadTextures() {
        super.loadTextures();
        this.texture_day = GameTexture.fromFile("objects/defensetower_" + this.towerType + "_morning");
        this.texture_night = GameTexture.fromFile("objects/defensetower_" + this.towerType + "_night");
    }

    @Override
    protected void setCounterIDs(int var1, int var2) {
        this.counterIDRight = var2;
    }

    @Override
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return new DefenseTowerEntity(level, "defensetowerentity_" + this.towerType, x, y,
                "defensetowerarrow_" + this.towerType, 512, this.damage, this.cooldown, this.owner);
    }

    @Override
    protected Rectangle getCollision(Level level, int x, int y, int rotation) {
        return new Rectangle(x * 32 + 6, y * 32, 22, 32);
    }

    @Override
    public ListGameTooltips getItemTooltips(InventoryItem item, PlayerMob perspective) {
        float attackSpeed = 1000.0F / this.cooldown;

        ListGameTooltips tooltips = super.getItemTooltips(item, perspective);
        if (Screen.isKeyDown(Control.getControl("invquickmove").getKey())) {
            tooltips.add(Localization.translate("defensetower", "attackstats", "damage", this.damage));
            tooltips.add(Localization.translate("defensetower", "attacktype", "type", this.towerType));
            tooltips.add(Localization.translate("defensetower", "attackspeed", "speed", String.format("%.2f", attackSpeed)));
            tooltips.add(Localization.translate("defensetower", "rangestats", "range", (512 / 32) - 2));
        } else {
            tooltips.add(Localization.translate("defensetower", "pressshift"));
        }
        return tooltips;
    }

    @Override
    public MultiTile getMultiTile(int rotation) {
        return new StaticMultiTile(0, 0, 2, 1, 0, true, this.getID(), this.counterIDRight);
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList, Level level, int tileX, int tileY, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        GameLight light = level.getLightLevel(tileX, tileY);
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        GameTexture texture = level.getWorldEntity().isNight() ? this.texture_night : this.texture_day;

        final TextureDrawOptions options = texture.initDraw().light(light).pos(drawX, drawY - this.texture_day.getHeight() + 32);
        list.add(new LevelSortedDrawable(this, tileX, tileY) {
            @Override
            public int getSortY() {
                return 16;
            }

            @Override
            public void draw(TickManager tickManager) {
                options.draw();
            }
        });
    }

    @Override
    public void drawPreview(Level level, int tileX, int tileY, int rotation, float alpha, PlayerMob player, GameCamera camera) {
        if (this.owner == null) this.owner = player;
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        this.texture_day.initDraw().alpha(alpha).draw(drawX, drawY - this.texture_day.getHeight() + 32);
    }

    @Override
    public GameTexture generateItemTexture() {
        return this.texture_day;
    }

    //@Override
    //public String canPlace(Level level, int x, int y, int rotation) {
    //    if (this.owner != null) {
    //        SettlementLevelData levelData = SettlementLevelData.getSettlementData(this.owner.getLevel());
    //        if (levelData != null) {
    //            Zoning defendZone = levelData.getDefendZone();
    //            System.out.printf("Defend Zone at: (%d, %d)", defendZone.getTileBounds().x, defendZone.getTileBounds().y);
    //        } else {
    //            System.out.println("SettlementLevelData is null");
    //        }
    //    } else {
    //        System.out.println("Owner is null");
    //    }
    //    return super.canPlace(level, x, y, rotation);
    //}
}
