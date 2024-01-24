package org.commonmc.tags.backends.neoforge;

import org.commonmc.tags.Tags;
import org.commonmc.tags.backends.Backend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class NeoForgeBackend implements Backend {
    @Override
    public String name() {
        return "neoforge";
    }

    @Override
    public void modifyTags(Tags tags) {
        // TODO legacy tags
    }

    @Override
    public void generate(Path basePath, Tags tags) throws IOException {
        var neoforgeCommon = basePath.resolve("src/main/java/net/neoforged/neoforge/common");

        CommonTagsGeneration.generate(neoforgeCommon.resolve("CommonTags.java"), tags);

        for (var registry : tags) {
            TagProviderGeneration.generate(neoforgeCommon.resolve("data/internal/Common%sTagsProvider.java".formatted(registry.entryType())), registry);
        }
    }
}
