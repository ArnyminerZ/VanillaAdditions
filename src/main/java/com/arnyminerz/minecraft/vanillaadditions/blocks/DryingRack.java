package com.arnyminerz.minecraft.vanillaadditions.blocks;

import com.arnyminerz.minecraft.vanillaadditions.recipes.DryingRecipe;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DryingRack extends HorizontalFacingBlock implements BlockEntityProvider {
    public static final BooleanProperty DRYING = BooleanProperty.of("drying");

    public DryingRack(Settings settings) {
        super(settings);
        setDefaultState(
                getStateManager()
                        .getDefaultState()
                        .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                        .with(Properties.STAGE, 0)
                        .with(DRYING, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
        builder.add(Properties.STAGE);
        builder.add(DRYING);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DryingRackEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case NORTH -> VoxelShapes.cuboid(0f, 0.8125f, 0.8125f, 1f, 1f, 1f); // done
            case SOUTH -> VoxelShapes.cuboid(0f, 0.8125f, 0f, 1f, 1f, 0.1875f);
            case EAST -> VoxelShapes.cuboid(0f, 0.8125f, 0f, 0.1875f, 1f, 1f);
            case WEST -> VoxelShapes.cuboid(0.8125, 0.8125f, 0f, 1f, 1f, 1f);
            default -> VoxelShapes.fullCube();
        };
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction dir = ctx.getSide();
        if (dir == Direction.UP || dir == Direction.DOWN)
            return null;
        return getDefaultState().with(Properties.HORIZONTAL_FACING, dir);
    }

    /*@Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        DryingRackEntity entity = (DryingRackEntity) world.getBlockEntity(pos);
        if (entity == null)
            return ActionResult.FAIL;

        String entityItemIdString = entity.getItem();
        Identifier entityItemId = entityItemIdString != null && !entityItemIdString.isBlank() ? Identifier.tryParse(entityItemIdString) : null;
        Item entityItem = entityItemId != null ? Registry.ITEM.get(entityItemId) : null;

        ItemStack handItem = player.getStackInHand(hand);
        int stage = state.get(Properties.STAGE);

        // Get recipe input and output
        CraftingInventory inventory = new CraftingInventory(player.playerScreenHandler, 1, 1);
        inventory.setStack(0, entityItem != null ? entityItem.getDefaultStack() : handItem);
        Optional<DryingRecipe> recipeOpt = world.getRecipeManager().getFirstMatch(DryingRecipe.Type.INSTANCE, inventory, world);
        if (recipeOpt.isEmpty()) {
            System.out.println("Could not find a valid recipe. Entity item null: " + (entityItem != null) + " (" + entityItemId + ")");
            return ActionResult.FAIL;
        }
        DryingRecipe recipe = recipeOpt.get();
        ItemStack input = recipe.getInput().getMatchingStacks()[0];
        ItemStack output = recipe.getOutput();

        if (input == null || output == null) {
            System.out.println("Input and output null");
            return ActionResult.FAIL;
        }

        if (entityItemId != null) {
            entity.clearItem();
            world.setBlockState(pos, state.with(DRYING, false));
            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);

            player.sendMessage(Text.of("Took item!"));
            player.getInventory().offerOrDrop(
                    stage < 10 ? input : output
            );

            return ActionResult.SUCCESS;
        }

        if (!handItem.isItemEqual(input)) {
            player.sendMessage(Text.of("Item not valid."));
            return ActionResult.PASS;
        }

        // player.playSound(SoundEvents.UI_LOOM_TAKE_RESULT, SoundCategory.BLOCKS, 1, 1);
        entity.setItem(input);
        world.setBlockState(pos, state.with(DRYING, true));
        world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);

        // Take item
        handItem.decrement(1);

        player.sendMessage(Text.of("Item is correct!"));

        return ActionResult.SUCCESS;
    }*/

    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (world.isClient) return ActionResult.SUCCESS;
        Inventory blockEntity = (Inventory) world.getBlockEntity(blockPos);
        if (blockEntity == null) return ActionResult.FAIL;
        ItemStack inventoryItem = blockEntity.getStack(0);
        ItemStack handItem = player.getStackInHand(hand);

        if (!inventoryItem.isEmpty()) {
            player.getInventory().offerOrDrop(inventoryItem);
            blockEntity.removeStack(0);

            world.setBlockState(blockPos, blockState.with(DRYING, false));
            world.updateListeners(blockPos, blockState, blockState, Block.NOTIFY_LISTENERS);
        } else if (!handItem.isEmpty()) {
            // Check what is the first open slot and put an item from the player's hand there
            ItemStack item = handItem.copy();
            item.setCount(1);

            // Put the stack the player is holding into the inventory
            blockEntity.setStack(0, item);
            // Remove the stack from the player's hand
            handItem.decrement(1);

            world.setBlockState(blockPos, blockState.with(DRYING, true));
            world.updateListeners(blockPos, blockState, blockState, Block.NOTIFY_LISTENERS);
        }

        return ActionResult.SUCCESS;
    }
}
