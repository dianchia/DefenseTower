package DefenseTower;

import DefenseTower.gameObject.DefenseTowerObject;
import DefenseTower.projectiles.DefenseTowerArrowProjectile;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.ProjectileRegistry;
import necesse.engine.registries.RecipeTechRegistry;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;

@ModEntry
public class DefenseTower {
    public void init() {
        DefenseTowerObject.registerDefenseTower();
        ProjectileRegistry.registerProjectile("defensetowerarrow", DefenseTowerArrowProjectile.class, "stonearrow", "arrow_shadow");
    }

    public void postInit() {
        Recipes.registerModRecipe(new Recipe(
                "defensetower",
                1,
                RecipeTechRegistry.WORKSTATION,
                new Ingredient[] {
                        new Ingredient("anystone", 100),
                        new Ingredient("anylog", 30),
                        new Ingredient("stonearrow", 100)
                }
        ));
    }
}
