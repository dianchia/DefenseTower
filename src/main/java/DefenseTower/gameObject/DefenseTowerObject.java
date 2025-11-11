package DefenseTower.gameObject;

import DefenseTower.objectEntity.DefenseTowerEntity;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.input.Control;
import necesse.engine.localization.Localization;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.window.GameWindow;
import necesse.engine.window.WindowManager;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.level.gameObject.ObjectDamagedTextureArray;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;
import necesse.level.maps.multiTile.MultiTile;
import necesse.level.maps.multiTile.StaticMultiTile;

import java.awt.*;
import java.util.List;

public class DefenseTowerObject extends DefenseTowerExtraObject {
    public ObjectDamagedTextureArray texture;
    protected int counterIDRight;

    private final String towerType;
    private final float damage;
    private final long cooldown;
    private final boolean targetBoss;

    protected DefenseTowerObject(String towerType, float damage, long cooldown, boolean targetBoss) {
        this.towerType = towerType;
        this.damage = damage;
        this.cooldown = cooldown;
        this.targetBoss = targetBoss;
    }

    @Override
    protected Rectangle getCollision(Level level, int x, int y, int rotation) {
        return new Rectangle(x * 32 + 6, y * 32, 22, 32);
    }

    @Override
    protected void setCounterIDs(int id1, int id2) {
        this.counterIDRight = id2;
    }

    @Override
    public MultiTile getMultiTile(int rotation) {
        return new StaticMultiTile(0, 0, 2, 1, 0, true, new int[]{this.getID(), this.counterIDRight});
    }

    @Override
    public void loadTextures() {
        super.loadTextures();
        this.texture = ObjectDamagedTextureArray.loadAndApplyOverlay(this, "objects/defensetower_" + this.towerType);
//        this.texture = GameTexture.fromFile("objects/defensetower_" + this.towerType);
    }

    @Override
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        if (!this.isMultiTileMaster()) return null;

        String projectileStringID = "dtprojectile_" + this.towerType;
        if (!projectileStringID.contains("cannonball")) {
            projectileStringID = this.targetBoss ? projectileStringID : projectileStringID + "piercing";
        }
        return new DefenseTowerEntity(level, "defensetowerentity_" + this.towerType, x, y, projectileStringID, 512, this.damage, this.cooldown, this.targetBoss);
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList, Level level, int tileX, int tileY, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        GameLight light = level.getLightLevel(tileX, tileY);
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        boolean active = this.isActive(level, tileX, tileY);

        GameTexture texture = this.texture.getDamagedTexture(this, level, tileX, tileY);
        final DrawOptions options = texture.initDraw().sprite(active ? 0 : 1, 0, 64, texture.getHeight()).light(light).pos(drawX, drawY - texture.getHeight() + 32);

//        final TextureDrawOptions options = this.texture.initDraw().sprite(active ? 0 : 1, 0, 64, this.texture.getHeight()).light(light).pos(drawX, drawY - this.texture.getHeight() + 32);
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
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        GameTexture texture = this.texture.getDamagedTexture(0.0F);
        texture.initDraw().sprite(0, 0, 64, texture.getHeight()).alpha(alpha).draw(drawX, drawY - texture.getHeight() + 32);
//        this.texture.initDraw().sprite(0, 0, 64, this.texture.getHeight()).alpha(alpha).draw(drawX, drawY - this.texture.getHeight() + 32);
    }

    @Override
    public ListGameTooltips getItemTooltips(InventoryItem item, PlayerMob perspective) {
        float attackSpeed = 1000.0F / this.cooldown;

        ListGameTooltips tooltips = super.getItemTooltips(item, perspective);
        if (this.targetBoss) tooltips.add(Localization.translate("itemtooltip", "defensetowertargetboss"));

        GameWindow gameWindow = WindowManager.getWindow();
        if (gameWindow.isKeyDown(Control.getControl("invquickmove").getKey())) {
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
    public GameTexture generateItemTexture() {
        return GameTexture.fromFile("items/defensetower_" + this.towerType);
    }

    public static int[] registerDefenseTower() {
        DefenseTowerObject stoneTower1 = new DefenseTowerObject("stone", 28.0F, 700, false);
        DefenseTowerObject2 stoneTower2 = new DefenseTowerObject2();
        DefenseTowerObject fireTower1 = new DefenseTowerObject("fire", 40.0F, 1000, false);
        DefenseTowerObject2 fireTower2 = new DefenseTowerObject2();
        DefenseTowerObject poisonTower1 = new DefenseTowerObject("poison", 40.0F, 1000, false);
        DefenseTowerObject2 poisonTower2 = new DefenseTowerObject2();
        DefenseTowerObject cannonballTower1 = new DefenseTowerObject("cannonball", 80.0F, 2000, false);
        DefenseTowerObject2 cannonballTower2 = new DefenseTowerObject2();

        DefenseTowerObject boss_stoneTower1 = new DefenseTowerObject("stone_boss", 70.0F, 700, true);
        DefenseTowerObject2 boss_stoneTower2 = new DefenseTowerObject2();
        DefenseTowerObject boss_fireTower1 = new DefenseTowerObject("fire_boss", 100.0F, 1000, true);
        DefenseTowerObject2 boss_fireTower2 = new DefenseTowerObject2();
        DefenseTowerObject boss_poisonTower1 = new DefenseTowerObject("poison_boss", 100.0F, 1000, true);
        DefenseTowerObject2 boss_poisonTower2 = new DefenseTowerObject2();

        int stoneID1 = ObjectRegistry.registerObject("defensetowerstone", stoneTower1, 100.0F, true);
        int stoneID2 = ObjectRegistry.registerObject("defensetowerstone2", stoneTower2, 0.0F, false);
        int fireID1 = ObjectRegistry.registerObject("defensetowerfire", fireTower1, 100.0F, true);
        int fireID2 = ObjectRegistry.registerObject("defensetowerfire2", fireTower2, 0.0F, false);
        int poisonID1 = ObjectRegistry.registerObject("defensetowerpoison", poisonTower1, 100.0F, true);
        int poisonID2 = ObjectRegistry.registerObject("defensetowerpoison2", poisonTower2, 0.0F, false);
        int cannonballID1 = ObjectRegistry.registerObject("defensetowercannonball", cannonballTower1, 100.0F, true);
        int cannonballID2 = ObjectRegistry.registerObject("defensetowercannonball2", cannonballTower2, 0.0F, false);

        int boss_stoneID1 = ObjectRegistry.registerObject("defensetowerstone_boss", boss_stoneTower1, 200.0F, true);
        int boss_stoneID2 = ObjectRegistry.registerObject("defensetowerstone2_boss", boss_stoneTower2, 0.0F, false);
        int boss_fireID1 = ObjectRegistry.registerObject("defensetowerfire_boss", boss_fireTower1, 200.0F, true);
        int boss_fireID2 = ObjectRegistry.registerObject("defensetowerfire2_boss", boss_fireTower2, 0.0F, false);
        int boss_poisonID1 = ObjectRegistry.registerObject("defensetowerpoison_boss", boss_poisonTower1, 200.0F, true);
        int boss_poisonID2 = ObjectRegistry.registerObject("defensetowerpoison2_boss", boss_poisonTower2, 0.0F, false);


        stoneTower1.setCounterIDs(stoneID1, stoneID2);
        stoneTower2.setCounterIDs(stoneID1, stoneID2);
        fireTower1.setCounterIDs(fireID1, fireID2);
        fireTower2.setCounterIDs(fireID1, fireID2);
        poisonTower1.setCounterIDs(poisonID1, poisonID2);
        poisonTower2.setCounterIDs(poisonID1, poisonID2);
        cannonballTower1.setCounterIDs(cannonballID1, cannonballID2);
        cannonballTower2.setCounterIDs(cannonballID1, cannonballID2);

        boss_stoneTower1.setCounterIDs(boss_stoneID1, boss_stoneID2);
        boss_stoneTower2.setCounterIDs(boss_stoneID1, boss_stoneID2);
        boss_fireTower1.setCounterIDs(boss_fireID1, boss_fireID2);
        boss_fireTower2.setCounterIDs(boss_fireID1, boss_fireID2);
        boss_poisonTower1.setCounterIDs(boss_poisonID1, boss_poisonID2);
        boss_poisonTower2.setCounterIDs(boss_poisonID1, boss_poisonID2);

        return new int[]{stoneID1, stoneID2, fireID1, fireID2, poisonID1, poisonID2, cannonballID1, cannonballID2, boss_stoneID1, boss_stoneID2, boss_fireID1, boss_fireID2, boss_poisonID1, boss_poisonID2};
    }
}
