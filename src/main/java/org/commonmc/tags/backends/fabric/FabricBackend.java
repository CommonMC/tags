package org.commonmc.tags.backends.fabric;

import org.commonmc.tags.Tags;
import org.commonmc.tags.backends.Backend;

import java.io.IOException;
import java.nio.file.Path;

public class FabricBackend implements Backend {
    @Override
    public String name() {
        return "fabric";
    }

    @Override
    public void modifyTags(Tags tags) {

    }

    @Override
    public void generate(Path basePath, Tags tags) throws IOException {
        var tagV2Api = basePath.resolve("fabric-convention-tags-v2/src/main/java/net/fabricmc/fabric/api/tag/convention/v2");
        var tagV2Datagen = basePath.resolve("fabric-convention-tags-v2/src/datagen/java/net/fabricmc/fabric/impl/tag/convention/datagen/generators");

        for (var registry : tags) {
            ConventionalTagsGeneration.generate(tagV2Api.resolve("Conventional%sTags.java".formatted(registry.entryType())), registry);
            TagGeneratorGeneration.generate(tagV2Datagen.resolve("%sTagGenerator.java".formatted(registry.entryType())), registry);
        }
    }
}
