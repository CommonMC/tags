package org.commonmc.tags.definitions;

import org.commonmc.tags.DyeColor;
import org.commonmc.tags.Tags;

public class BlockTags {
    public static void register(Tags.TagSet blockTags) {
        blockTags.tag("barrels")
                .addTagEntry("c:barrels/wooden");
        blockTags.tag("barrels/wooden")
                .addEntry("barrel");
        blockTags.tag("bookshelves")
                .addEntry("bookshelf");
        blockTags.tag("budding_blocks")
                .setComment("""
                        For blocks that are similar to amethyst where their budding block produces buds and cluster blocks.
                        """)
                .addEntry("budding_amethyst");
        blockTags.tag("buds")
                .setComment("""
                        For blocks that are similar to amethyst where they have buds forming from budding blocks.
                        """)
                .addEntry("small_amethyst_bud")
                .addEntry("medium_amethyst_bud")
                .addEntry("large_amethyst_bud");
        blockTags.tag("chains")
                .addEntry("chain");
        blockTags.tag("chests")
                .addTagEntry("c:chests/ender")
                .addTagEntry("c:chests/trapped")
                .addTagEntry("c:chests/wooden");
        blockTags.tag("chests/ender")
                .addEntry("ender_chest");
        blockTags.tag("chests/trapped")
                .addEntry("trapped_chest");
        blockTags.tag("chests/wooden")
                .addEntry("chest")
                .addEntry("trapped_chest");
        blockTags.tag("clusters")
                .setComment("""
                        For blocks that are similar to amethyst where they have clusters forming from budding blocks.
                        """)
                .addEntry("amethyst_cluster");
        blockTags.tag("cobblestones")
                .addTagEntry("c:cobblestones/normal")
                .addTagEntry("c:cobblestones/infested")
                .addTagEntry("c:cobblestones/mossy")
                .addTagEntry("c:cobblestones/deepslate");
        blockTags.tag("cobblestones/normal")
                .addEntry("cobblestone");
        blockTags.tag("cobblestones/infested")
                .addEntry("infested_cobblestone");
        blockTags.tag("cobblestones/mossy")
                .addEntry("mossy_cobblestone");
        blockTags.tag("cobblestones/deepslate")
                .addEntry("cobbled_deepslate");
        blockTags.tag("dyed")
                .setComment("""
                        Tag that holds all blocks that can be dyed of a specific color.
                        
                        <p>Does not include color blending blocks that would behave similar to leather armor item.
                        """);

        for (var color : DyeColor.values()) {
            blockTags.tag("dyed")
                    .addTagEntry("c:dyed/" + color.lowercaseName());
        }
        addColored(blockTags, "{color}_banner");
        addColored(blockTags, "{color}_bed");
        addColored(blockTags, "{color}_candle");
        addColored(blockTags, "{color}_carpet");
        addColored(blockTags, "{color}_concrete");
        addColored(blockTags, "{color}_concrete_powder");
        addColored(blockTags, "{color}_glazed_terracotta");
        addColored(blockTags, "{color}_shulker_box");
        addColored(blockTags, "{color}_stained_glass");
        addColored(blockTags, "{color}_stained_glass_pane");
        addColored(blockTags, "{color}_terracotta");
        addColored(blockTags, "{color}_wall_banner");
        addColored(blockTags, "{color}_wool");

        blockTags.tag("end_stones")
                .addEntry("end_stone");
        blockTags.tag("fence_gates")
                .addTagEntry("c:fence_gates/wooden");
        blockTags.tag("fence_gates/wooden")
                .addEntry("acacia_fence_gate")
                .addEntry("bamboo_fence_gate")
                .addEntry("birch_fence_gate")
                .addEntry("cherry_fence_gate")
                .addEntry("crimson_fence_gate")
                .addEntry("dark_oak_fence_gate")
                .addEntry("jungle_fence_gate")
                .addEntry("mangrove_fence_gate")
                .addEntry("oak_fence_gate")
                .addEntry("spruce_fence_gate")
                .addEntry("warped_fence_gate");
        blockTags.tag("fences")
                .addTagEntry("c:fences/nether_brick")
                .addTagEntry("c:fences/wooden");
        blockTags.tag("fences/nether_brick")
                .addEntry("nether_brick_fence");
        blockTags.tag("fences/wooden")
                .addTagEntry("minecraft:wooden_fences");
        blockTags.tag("glass/blocks")
                .addTagEntry("c:glass/blocks/colorless")
                .addTagEntry("c:glass/blocks/cheap")
                .addTagEntry("c:glass/blocks/tinted");
        for (var color : DyeColor.values()) {
            blockTags.tag("glass/blocks")
                    .addEntry(color.formatEntry("{color}_stained_glass"));
        }
        blockTags.tag("glass/blocks/colorless")
                .addEntry("glass");
        blockTags.tag("glass/blocks/cheap")
                .addEntry("glass");
        for (var color : DyeColor.values()) {
            blockTags.tag("glass/blocks/cheap")
                    .addEntry(color.formatEntry("{color}_stained_glass"));
        }
        blockTags.tag("glass/blocks/tinted")
                .addEntry("tinted_glass");

        // TODO: continue with glass panes
    }

    private static void addColored(Tags.TagSet tags, String pattern) {
        for (var color : DyeColor.values()) {
            tags.tag("dyed/" + color.lowercaseName())
                    .addEntry(color.formatEntry(pattern));
        }
    }
}
