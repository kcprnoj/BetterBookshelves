package com.bawnorton.betterbookshelves.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public abstract class PlayerLookHelper {

    public static Pair<Book, ItemStack> getLookingAtBook(ChiseledBookshelfBlockEntity blockEntity) {
        Pair<Book, ItemStack> emptyBook = new Pair<>(Book.NONE, ItemStack.EMPTY);
        MinecraftClient client = MinecraftClient.getInstance();
        if (!(client.crosshairTarget instanceof BlockHitResult hit)) return emptyBook;

        // Get block entity from world if null
        assert client.world != null;
        if (blockEntity == null) {
            Optional<ChiseledBookshelfBlockEntity> blockEntityOptional = client.world.getBlockEntity(hit.getBlockPos(), BlockEntityType.CHISELED_BOOKSHELF);
            if (blockEntityOptional.isEmpty()) return emptyBook;
            blockEntity = blockEntityOptional.get();
        } else if (!hit.getBlockPos().equals(blockEntity.getPos())) return emptyBook;

        // Get hit position on the block and the slot
        OptionalInt slot = getSlotForHitPos(hit, blockEntity.getCachedState());
        
        if (slot.isEmpty())
            return emptyBook;
        else
            return new Pair<>(Book.getBook(slot.getAsInt()), blockEntity.getStack(slot.getAsInt()));
    }

    public static List<Text> getBookText(ItemStack book) {
        List<Text> displayText = new ArrayList<>();
        displayText.add(book.getName());
        if (book.getItem() == Items.ENCHANTED_BOOK) {
            ItemEnchantmentsComponent tag = book.get(DataComponentTypes.STORED_ENCHANTMENTS);

            if (tag != null) {
                Set<RegistryEntry<Enchantment>> enchantments = tag.getEnchantments();
                for (RegistryEntry<Enchantment> entry : enchantments) {
                    displayText.add(entry.value().getName(tag.getLevel(entry.value())));
                }
            }
        }
        return displayText;
    }


    private static OptionalInt getSlotForHitPos(BlockHitResult hit, BlockState state) {
        return getHitPos(hit, state.get(HorizontalFacingBlock.FACING)).map((hitPos) -> {
            int i = hitPos.y >= 0.5F ? 0 : 1;
            int j = getColumn(hitPos.x);
            return OptionalInt.of(j + i * 3);
        }).orElseGet(OptionalInt::empty);
    }

    private static Optional<Vec2f> getHitPos(BlockHitResult hit, Direction facing) {
        Direction direction = hit.getSide();
        if (facing != direction) {
            return Optional.empty();
        } else {
            BlockPos blockPos = hit.getBlockPos().offset(direction);
            Vec3d vec3d = hit.getPos().subtract((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ());
            double d = vec3d.getX();
            double e = vec3d.getY();
            double f = vec3d.getZ();
            Optional var10000;
            switch (direction) {
                case NORTH:
                    var10000 = Optional.of(new Vec2f((float)(1.0 - d), (float)e));
                    break;
                case SOUTH:
                    var10000 = Optional.of(new Vec2f((float)d, (float)e));
                    break;
                case WEST:
                    var10000 = Optional.of(new Vec2f((float)f, (float)e));
                    break;
                case EAST:
                    var10000 = Optional.of(new Vec2f((float)(1.0 - f), (float)e));
                    break;
                case DOWN:
                case UP:
                    var10000 = Optional.empty();
                    break;
                default:
                    throw new MatchException((String)null, (Throwable)null);
            }

            return var10000;
        }
    }

    private static int getColumn(float x) {
        float f = 0.0625F;
        float g = 0.375F;
        if (x < 0.375F) {
            return 0;
        } else {
            float h = 0.6875F;
            return x < 0.6875F ? 1 : 2;
        }
    }
}
