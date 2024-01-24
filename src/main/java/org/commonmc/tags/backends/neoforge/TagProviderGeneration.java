package org.commonmc.tags.backends.neoforge;

import org.commonmc.tags.Tags;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class TagProviderGeneration {
    public static void generate(Path filePath, Tags.TagSet registry) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                /*
                 * Copyright (c) NeoForged and contributors
                 * SPDX-License-Identifier: LGPL-2.1-only
                 */
                
                package net.neoforged.neoforge.common.data.internal;
                
                import java.util.concurrent.CompletableFuture;
                import net.minecraft.core.HolderLookup;
                import net.minecraft.data.PackOutput;
                import net.minecraft.tags.BlockTags;
                import net.minecraft.world.item.enchantment.Enchantments;
                import net.minecraft.world.level.block.Blocks;
                import net.neoforged.neoforge.common.CommonTags;
                import net.neoforged.neoforge.common.data.BlockTagsProvider;
                import net.neoforged.neoforge.common.data.EnchantmentTagsProvider;
                import net.neoforged.neoforge.common.data.ExistingFileHelper;
                
                /**
                 * This file is auto-generated, do not edit by hand!
                 * Send a pull request to CommonMC/tags instead.
                 */
                public class Common%sTagsProvider extends %sTagsProvider {
                    public Common%sTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
                        super(output, lookupProvider, "c", existingFileHelper);
                    }
                
                    @SuppressWarnings("unchecked")
                    @Override
                    public void addTags(HolderLookup.Provider p_256380_) {
                """.formatted(registry.entryType(), registry.entryType(), registry.entryType()));

        for (var tag : registry) {
            sb.append("        tag(CommonTags.%s.%s)".formatted(registry.entriesType(), tag.fieldName()));

            var entriesString = tag.getEntries()
                    .stream()
                    .map(entry -> "%s.%s".formatted(registry.entriesType(), entry.toUpperCase(Locale.ROOT)))
                    .collect(joiningOrEmpty(", ", ".add(", ")"));

            if (!entriesString.isEmpty()) {
                sb.append(entriesString);
            }

            var tagEntriesString = tag.getTagEntries()
                    .stream()
                    .map(tagEntry -> {
                        String[] parts = tagEntry.split(":", 2);
                        var fieldName = parts[1].replace("/", "_").toUpperCase(Locale.ROOT);

                        if (parts[0].equals("c")) {
                            return "CommonTags.%s.%s".formatted(registry.entriesType(), fieldName);
                        } else {
                            return "%sTags.%s".formatted(registry.entryType(), fieldName);
                        }
                    })
                    .collect(joiningOrEmpty(", ", ".addTags(", ")"));

            if (!tagEntriesString.isEmpty()) {
                sb.append(tagEntriesString);
            }

            sb.append(";\n");
        }

        sb.append("    }\n");

        sb.append("}\n");

        Files.writeString(filePath, sb);
    }

    /**
     * Same as {@link Collectors#joining(CharSequence, CharSequence, CharSequence)},
     * but produces an empty string if the stream is empty.
     */
    public static Collector<CharSequence, ?, String> joiningOrEmpty(CharSequence delimiter,
            CharSequence prefix,
            CharSequence suffix) {
        return Collector.of(
                () -> {
                    var sj = new StringJoiner(delimiter, prefix, suffix);
                    sj.setEmptyValue("");
                    return sj;
                },
                StringJoiner::add, StringJoiner::merge,
                StringJoiner::toString);
    }
}
