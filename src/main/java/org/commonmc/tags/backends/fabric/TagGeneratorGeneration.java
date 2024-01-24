package org.commonmc.tags.backends.fabric;

import org.commonmc.tags.Tags;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class TagGeneratorGeneration {
    public static void generate(Path filePath, Tags.TagSet registry) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                /*
                 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
                 *
                 * Licensed under the Apache License, Version 2.0 (the "License");
                 * you may not use this file except in compliance with the License.
                 * You may obtain a copy of the License at
                 *
                 *     http://www.apache.org/licenses/LICENSE-2.0
                 *
                 * Unless required by applicable law or agreed to in writing, software
                 * distributed under the License is distributed on an "AS IS" BASIS,
                 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                 * See the License for the specific language governing permissions and
                 * limitations under the License.
                 */
                                
                package net.fabricmc.fabric.impl.tag.convention.datagen.generators;
                                
                import java.util.concurrent.CompletableFuture;
                
                import net.minecraft.block.Blocks;
                import net.minecraft.enchantment.Enchantments;
                import net.minecraft.registry.RegistryWrapper;
                import net.minecraft.registry.tag.BlockTags;
                import net.minecraft.util.Identifier;
                
                import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
                import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
                import net.fabricmc.fabric.api.tag.convention.v2.Conventional%sTags;
                                
                /**
                 * This file is auto-generated, do not edit by hand!
                 * Send a pull request to CommonMC/tags instead.
                 */
                public class %sTagGenerator extends FabricTagProvider.%sTagProvider {
                    public %sTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
                        super(output, registriesFuture);
                    }
                
                    @Override
                    public void configure(RegistryWrapper.WrapperLookup registries) {
                """.formatted(registry.entryType(), registry.entryType(), registry.entryType(), registry.entryType()));

        for (var tag : registry) {
            sb.append("        getOrCreateTagBuilder(Conventional%sTags.%s)".formatted(registry.entryType(), tag.fieldName()));

            var entriesString = tag.getEntries()
                    .stream()
                    .map(entry -> "%s.%s".formatted(registry.entriesType(), getYarnFieldName(registry, entry)))
                    .collect(joiningOrEmpty(", ", ".add(", ")"));

            if (!entriesString.isEmpty()) {
                sb.append(entriesString);
            }

            tag.getTagEntries()
                    .stream()
                    .map(tagEntry -> {
                        String[] parts = tagEntry.split(":", 2);
                        var fieldName = parts[1].replace("/", "_").toUpperCase(Locale.ROOT);

                        String tagReference;
                        if (parts[0].equals("c")) {
                            return "Conventional%sTags.%s".formatted(registry.entryType(), fieldName);
                        } else {
                            return "%sTags.%s".formatted(registry.entryType(), fieldName);
                        }
                    })
                    .forEach(fieldReference -> {
                        sb.append(".addTag(");
                        sb.append(fieldReference);
                        sb.append(")");
                    });

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

    /**
     * Convert entry from mojmap name to yarn name.
     */
    public static String getYarnFieldName(Tags.TagSet registry, String entry) {
        var adjustedEntry = switch (registry.pathNoWorldgen()) {
            case "enchantment" -> switch (entry) {
                case "all_damage_protection" -> "protection";
                case "block_fortune" -> "fortune";
                case "fall_protection" -> "feather_falling";
                case "mob_looting" -> "looting";
                case "power_arrows" -> "power";
                default -> entry;
            };
            default -> entry;
        };
        return adjustedEntry.toUpperCase(Locale.ROOT);
    }
}
