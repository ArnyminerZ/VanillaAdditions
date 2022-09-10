package com.arnyminerz.minecraft.vanillaadditions.recipes.model.drying;

import com.arnyminerz.minecraft.vanillaadditions.recipes.DryingRecipe;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DryingRecipeSerializer implements RecipeSerializer<DryingRecipe> {
    public static final DryingRecipeSerializer INSTANCE = new DryingRecipeSerializer();

    public static final Identifier ID = new Identifier("vanilla_additions:drying_recipe");

    private DryingRecipeSerializer() {}

    @Override
    public DryingRecipe read(Identifier id, JsonObject json) {
        DryingRecipeJsonFormat recipeJson = new Gson().fromJson(json, DryingRecipeJsonFormat.class);

        Ingredient input = Ingredient.fromJson(recipeJson.input);
        Item outputItem = Registry.ITEM
                .getOrEmpty(new Identifier(recipeJson.output))
                .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.output));
        ItemStack output = new ItemStack(outputItem, 1);

        return new DryingRecipe(id, output, input);
    }

    @Override
    public void write(PacketByteBuf buf, DryingRecipe recipe) {
        recipe.getInput().write(buf);
        buf.writeItemStack(recipe.getOutput());
    }

    @Override
    public DryingRecipe read(Identifier id, PacketByteBuf buf) {
        Ingredient input = Ingredient.fromPacket(buf);
        ItemStack output = buf.readItemStack();
        return new DryingRecipe(id, output, input);
    }
}
