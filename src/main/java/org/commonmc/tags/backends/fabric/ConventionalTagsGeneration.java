package org.commonmc.tags.backends.fabric;

import org.commonmc.tags.Tags;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Generates a ConventionalXxxTags.java source file.
 */
class ConventionalTagsGeneration {
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
                                
                package net.fabricmc.fabric.api.tag.convention.v2;
                
                import net.minecraft.block.Block;
                import net.minecraft.enchantment.Enchantment;
                import net.minecraft.registry.RegistryKeys;
                import net.minecraft.registry.tag.TagKey;
                import net.minecraft.util.Identifier;
                
                /**
                 * This file is auto-generated, do not edit by hand!
                 * Send a pull request to CommonMC/tags instead.
                 */
                public final class Conventional%sTags {
                	private Conventional%sTags() {
                	}
                
                """.formatted(registry.entryType(), registry.entryType()));

        // Generate each tag
        for (var tag : registry) {
            var comment = tag.getComment();
            if (comment != null) {
                sb.append("\t/**\n");
                comment.lines().forEach(line -> {
                    sb.append("\t *");
                    if (!line.isEmpty()) {
                        sb.append(" ");
                        sb.append(line);
                    }
                    sb.append("\n");
                });
                sb.append("\t */\n");
            }
            sb.append("\tpublic static final TagKey<%s> %s = register(\"%s\");\n".formatted(registry.entryType(), tag.fieldName(), tag.path()));
        }

        // Add tag() method to produce the tag keys
        sb.append("\n");
        sb.append("\tprivate static TagKey<%s> register(String name) {\n".formatted(registry.entryType()));
        sb.append("\t\treturn TagKey.of(RegistryKeys.%s, new Identifier(\"c\", name));\n".formatted(registry.fieldName()));
        sb.append("\t}\n");

        sb.append("}\n");

        Files.writeString(filePath, sb);
    }
}
