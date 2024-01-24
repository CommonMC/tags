package org.commonmc.tags.backends.neoforge;

import org.commonmc.tags.Tags;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Generates the {@code CommonTags.java} source file.
 */
class CommonTagsGeneration {
    public static void generate(Path filePath, Tags tags) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                /*
                 * Copyright (c) NeoForged and contributors
                 * SPDX-License-Identifier: LGPL-2.1-only
                 */
                                
                package net.neoforged.neoforge.common;
                
                import net.minecraft.client.renderer.GameRenderer;
                import net.minecraft.core.registries.Registries;
                import net.minecraft.resources.ResourceLocation;
                import net.minecraft.tags.BlockTags;
                import net.minecraft.tags.FluidTags;
                import net.minecraft.tags.ItemTags;
                import net.minecraft.tags.TagKey;
                import net.minecraft.world.item.enchantment.Enchantment;
                import net.minecraft.world.level.block.Block;
                
                /**
                 * Definitions for common tags (in the {@code c} namespace).
                 *
                 * <p>Do not reference directly, instead reference via {@link Tags}!
                 *
                 * <p>This file is auto-generated, do not edit by hand!
                 * Send a pull request to CommonMC/tags instead.
                 */
                public class CommonTags {
                """);

        boolean first = true;
        for (var registry : tags) {
            if (!first) {
                sb.append("\n");
            }
            first = false;

            // Open nested class
            sb.append("    public static class %s {\n".formatted(registry.entriesType()));

            // Generate each tag
            for (var tag : registry) {
                var comment = tag.getComment();
                if (comment != null) {
                    sb.append("        /**\n");
                    comment.lines().forEach(line -> {
                        sb.append("         *");
                        if (!line.isEmpty()) {
                            sb.append(" ");
                            sb.append(line);
                        }
                        sb.append("\n");
                    });
                    sb.append("         */\n");
                }
                sb.append("        public static final TagKey<%s> %s = tag(\"%s\");\n".formatted(registry.entryType(), tag.fieldName(), tag.path()));
            }

            // Add tag() method to produce the tag keys
            sb.append("\n");
            sb.append("        private static TagKey<%s> tag(String name) {\n".formatted(registry.entryType()));
            sb.append("            return TagKey.create(Registries.%s, new ResourceLocation(\"c\", name));\n".formatted(registry.fieldName()));
            sb.append("        }\n");

            // Close nested class
            sb.append("    }\n");
        }

        sb.append("}\n");

        Files.writeString(filePath, sb);
    }
}
