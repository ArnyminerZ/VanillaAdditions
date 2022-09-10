package com.arnyminerz.minecraft.vanillaadditions;

import com.arnyminerz.minecraft.vanillaadditions.blocks.DryingRack;
import com.arnyminerz.minecraft.vanillaadditions.blocks.DryingRackEntity;
import com.arnyminerz.minecraft.vanillaadditions.recipes.DryingRecipe;
import com.arnyminerz.minecraft.vanillaadditions.recipes.model.drying.DryingRecipeSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class VanillaAdditions implements ModInitializer {
    public static final Block DRYING_RACK = new DryingRack(
            FabricBlockSettings
                    .of(Material.WOOD)
                    .sounds(BlockSoundGroup.WOOD)
    );

    public static final BlockEntityType<DryingRackEntity> DRYING_RACK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier("vanilla_additions", "drying_rack_entity"),
            FabricBlockEntityTypeBuilder.create(DryingRackEntity::new, DRYING_RACK).build()
    );

    @Override
    public void onInitialize() {
        Registry.register(
                Registry.BLOCK,
                new Identifier("vanilla_additions", "drying_rack"),
                DRYING_RACK
        );
        Registry.register(
                Registry.ITEM,
                new Identifier("vanilla_additions", "drying_rack"),
                new BlockItem(
                        DRYING_RACK,
                        new FabricItemSettings()
                                .group(ItemGroup.DECORATIONS)
                )
        );

        // Register recipes
        Registry.register(Registry.RECIPE_SERIALIZER, DryingRecipeSerializer.ID, DryingRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier("vanilla_additions", DryingRecipe.Type.ID), DryingRecipe.Type.INSTANCE);
    }
}
