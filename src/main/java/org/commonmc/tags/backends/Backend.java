package org.commonmc.tags.backends;

import org.commonmc.tags.Tags;

import java.io.IOException;
import java.nio.file.Path;

public interface Backend {
    String name();

    // TODO: do we want to keep this?
    void modifyTags(Tags tags);

    void generate(Path basePath, Tags tags) throws IOException;
}
