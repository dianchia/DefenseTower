package DefenseTower;

import DefenseTower.events.DefenseTowerExplosionEvent;
import DefenseTower.gameObject.DefenseTowerObject;
import DefenseTower.mobs.DefenseTowerAttackerMob;
import DefenseTower.projectiles.DefenseTowerCannonBallProjectile;
import DefenseTower.projectiles.DefenseTowerFireArrowProjectile;
import DefenseTower.projectiles.DefenseTowerPoisonArrowProjectile;
import DefenseTower.projectiles.DefenseTowerStoneArrowProjectile;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.*;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;

@ModEntry
public class DefenseTower {
    public void init() {
        DefenseTowerObject.registerDefenseTower();
        MobRegistry.registerMob("defensetowerattacker", DefenseTowerAttackerMob.class, false);
        ProjectileRegistry.registerProjectile("defensetowerarrow_stone", DefenseTowerStoneArrowProjectile.class, "stonearrow", "arrow_shadow");
        ProjectileRegistry.registerProjectile("defensetowerarrow_fire", DefenseTowerFireArrowProjectile.class, "firearrow", "arrow_shadow");
        ProjectileRegistry.registerProjectile("defensetowerarrow_poison", DefenseTowerPoisonArrowProjectile.class, "poisonarrow", "arrow_shadow");
        ProjectileRegistry.registerProjectile("defensetowerarrow_cannonball", DefenseTowerCannonBallProjectile.class, "cannonball", "cannonball_shadow");
        LevelEventRegistry.registerEvent("defensetowerevent_explosion", DefenseTowerExplosionEvent.class);
    }

    public void postInit() {
        Recipes.registerModRecipe(new Recipe("defensetowerstone", 1, RecipeTechRegistry.WORKSTATION,
                new Ingredient[]{
                        new Ingredient("ironbar", 10),
                        new Ingredient("torch", 20),
                        new Ingredient("anystone", 30),
                        new Ingredient("anylog", 50),
                        new Ingredient("stonearrow", 100)
                }));
        Recipes.registerModRecipe(new Recipe("defensetowerfire", 1, RecipeTechRegistry.WORKSTATION,
                new Ingredient[]{
                        new Ingredient("ironbar", 10),
                        new Ingredient("torch", 20),
                        new Ingredient("anystone", 30),
                        new Ingredient("anylog", 50),
                        new Ingredient("firearrow", 100)
                }));
        Recipes.registerModRecipe(new Recipe("defensetowerpoison", 1, RecipeTechRegistry.WORKSTATION,
                new Ingredient[]{
                        new Ingredient("ironbar", 10),
                        new Ingredient("torch", 20),
                        new Ingredient("anystone", 30),
                        new Ingredient("anylog", 50),
                        new Ingredient("poisonarrow", 100)
                }));
        Recipes.registerModRecipe(new Recipe("defensetowercannonball", 1, RecipeTechRegistry.WORKSTATION,
                new Ingredient[]{
                        new Ingredient("ironbar", 10),
                        new Ingredient("torch", 20),
                        new Ingredient("anystone", 30),
                        new Ingredient("anylog", 50),
                        new Ingredient("handcannon", 1),
                        new Ingredient("cannonball", 10)
                }));
    }
}
