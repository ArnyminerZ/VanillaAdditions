package com.arnyminerz.minecraft.vanillaadditions.recipes;

import com.arnyminerz.minecraft.vanillaadditions.recipes.model.drying.DryingRecipeSerializer;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DryingRecipe implements Recipe<CraftingInventory> {
    private final Ingredient input;
    private final ItemStack output;
    private final Identifier id;

    public static class Type implements RecipeType<DryingRecipe> {
        private Type() {}

        public static final Type INSTANCE = new Type();
        public static final String ID = "drying_recipe";
    }

    public DryingRecipe(Identifier id, ItemStack result, Ingredient input) {
        this.id = id;
        this.output = result;
        this.input = input;
    }

    public Ingredient getInput() {
        return input;
    }

    public Identifier getId() {
        return this.id;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        return this.output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        if (inventory.size() < 1) return false;
        return input.test(inventory.getStack(0));
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DryingRecipeSerializer.INSTANCE;
    }
}
