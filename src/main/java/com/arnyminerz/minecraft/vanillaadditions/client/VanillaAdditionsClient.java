package com.arnyminerz.minecraft.vanillaadditions.client;

import com.arnyminerz.minecraft.vanillaadditions.blocks.DryingRackRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

import static com.arnyminerz.minecraft.vanillaadditions.VanillaAdditions.DRYING_RACK_ENTITY;

@Environment(EnvType.CLIENT)
public class VanillaAdditionsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(DRYING_RACK_ENTITY, DryingRackRenderer::new);
    }
}
