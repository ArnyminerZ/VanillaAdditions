package com.arnyminerz.minecraft.vanillaadditions.blocks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class DryingRackRenderer implements BlockEntityRenderer<DryingRackEntity> {
    public DryingRackRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(DryingRackEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        // Translate the matrix
        Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
        if (direction == Direction.NORTH)
            matrices.translate(0.5, 0.5, 0.9);
        else if (direction == Direction.SOUTH)
            matrices.translate(0.5, 0.5, 0.1);
        else if (direction == Direction.WEST) {
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90f));
            matrices.translate(-0.5, 0.5, 0.9);
        } else if (direction == Direction.EAST) {
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90f));
            matrices.translate(-0.5, 0.5, 0.1);
        }
        matrices.scale(.8f, .8f, .8f);

        World world = entity.getWorld();
        assert world != null;

        MinecraftClient client = MinecraftClient.getInstance();
        ItemRenderer renderer = client.getItemRenderer();
        int lightAbove = WorldRenderer.getLightmapCoordinates(world, entity.getPos().up());

        ItemStack itemStack = entity.getStack(0);
        // System.out.println("Item: " + itemStack + " Is empty: " + itemStack.isEmpty());
        if (!itemStack.isEmpty()) {
            renderer.renderItem(
                    itemStack,
                    ModelTransformation.Mode.GUI,
                    lightAbove,
                    OverlayTexture.DEFAULT_UV,
                    matrices,
                    vertexConsumers,
                    1
            );
        } // else System.out.println("ITem is empty");

        matrices.pop();
    }
}
