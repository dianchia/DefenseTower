package DefenseTower;

import DefenseTower.events.DefenseTowerExplosionEvent;
import DefenseTower.gameObject.DefenseTowerObject;
import DefenseTower.mobs.DefenseTowerAttackerMob;
import DefenseTower.projectiles.*;
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

        ProjectileRegistry.registerProjectile("dtprojectile_stone", DTStoneArrowProjectile.class, "stonearrow", "arrow_shadow");
        ProjectileRegistry.registerProjectile("dtprojectile_fire", DTFireArrowProjectile.class, "firearrow", "arrow_shadow");
        ProjectileRegistry.registerProjectile("dtprojectile_poison", DTPoisonArrowProjectile.class, "poisonarrow", "arrow_shadow");
        ProjectileRegistry.registerProjectile("dtprojectile_cannonball", DTCannonBallProjectile.class, "cannonball", "cannonball_shadow");

        ProjectileRegistry.registerProjectile("dtprojectile_stonepiercing", DTStoneArrowPiercingProjectile.class, "stonearrow", "arrow_shadow");
        ProjectileRegistry.registerProjectile("dtprojectile_firepiercing", DTFireArrowPiercingProjectile.class, "firearrow", "arrow_shadow");
        ProjectileRegistry.registerProjectile("dtprojectile_poisonpiercing", DTPoisonArrowPiercingProjectile.class, "poisonarrow", "arrow_shadow");

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

        Recipes.registerModRecipe(new Recipe("defensetowerstone", 1, RecipeTechRegistry.WORKSTATION,
                new Ingredient[]{
                        new Ingredient("tungstenbar", 10),
                        new Ingredient("torch", 20),
                        new Ingredient("anystone", 30),
                        new Ingredient("anylog", 50),
                        new Ingredient("stonearrow", 100)
                }));
        Recipes.registerModRecipe(new Recipe("defensetowerfire", 1, RecipeTechRegistry.WORKSTATION,
                new Ingredient[]{
                        new Ingredient("tungstenbar", 10),
                        new Ingredient("torch", 20),
                        new Ingredient("anystone", 30),
                        new Ingredient("anylog", 50),
                        new Ingredient("firearrow", 100)
                }));
        Recipes.registerModRecipe(new Recipe("defensetowerpoison", 1, RecipeTechRegistry.WORKSTATION,
                new Ingredient[]{
                        new Ingredient("tungstenbar", 10),
                        new Ingredient("torch", 20),
                        new Ingredient("anystone", 30),
                        new Ingredient("anylog", 50),
                        new Ingredient("poisonarrow", 100)
                }));
    }
}
