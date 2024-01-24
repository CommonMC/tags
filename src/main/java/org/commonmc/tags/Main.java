package org.commonmc.tags;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.commonmc.tags.backends.Backend;
import org.commonmc.tags.definitions.BlockTags;
import org.commonmc.tags.definitions.EnchantmentTags;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) throws IOException {
        // Parse options
        var optionParser = new OptionParser();
        OptionSpec<String> backendOption = optionParser.accepts("backend").withRequiredArg();
        OptionSpec<String> baseFolder = optionParser.accepts("folder").withOptionalArg().defaultsTo(".");
        OptionSet options = optionParser.parse(args);

        var backendName = backendOption.value(options);

        // Select backend
        var backendInstance = ServiceLoader.load(Backend.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(x -> x.name().equals(backendName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find backend: " + backendName));

        // Add common tags
        Tags tags = new Tags();
        BlockTags.register(tags.forRegistry("block"));
        EnchantmentTags.register(tags.forRegistry("enchantment"));

        // Add loader-specific extensions
        backendInstance.modifyTags(tags);

        // Generate!
        backendInstance.generate(Path.of(baseFolder.value(options)), tags);
    }
}
