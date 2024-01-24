package org.commonmc.tags;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Tags implements Iterable<Tags.TagSet> {
    /**
     * Map of registry path to tag set.
     */
    private final Map<String, TagSet> registries = new TreeMap<>();

    @Override
    public Iterator<TagSet> iterator() {
        return registries.values().iterator();
    }

    /**
     * Retrieve or create the tag set for a registry path.
     * @param path Registry path, for example "entity_type".
     */
    public TagSet forRegistry(String path) {
        return registries.computeIfAbsent(path, TagSet::new);
    }

    public static class TagSet implements Iterable<TagContents> {
        private final String path;
        private final Map<String, TagContents> tags = new LinkedHashMap<>();

        public TagSet(String path) {
            this.path = path;
        }

        public String pathNoWorldgen() {
            if (path.startsWith("worldgen/")) {
                return path.substring("worldgen/".length());
            } else {
                return path;
            }
        }

        public String fieldName() {
            return pathNoWorldgen().replace("/", "_").toUpperCase(Locale.ROOT);
        }

        public String entryType() {
            String[] words = pathNoWorldgen().split("_");
            // Capitalize each word
            StringBuilder entryType = new StringBuilder();
            for (String word : words) {
                entryType.append(word.substring(0, 1).toUpperCase(Locale.ROOT));
                entryType.append(word.substring(1));
            }
            return entryType.toString();
        }

        public String entriesType() {
            return entryType() + "s";
        }

        public TagContents tag(String path) {
            if (path.contains(":")) {
                throw new IllegalArgumentException("Tag path cannot contain a namespace, the c namespace is implicit: " + path);
            }

            return tags.computeIfAbsent(path, TagContents::new);
        }

        @Override
        public Iterator<TagContents> iterator() {
            return tags.values().iterator();
        }
    }

    public static class TagContents {
        private final String path;
        @Nullable
        private String comment;
        private final SortedSet<String> entries;
        private final SortedSet<String> tagEntries;

        public TagContents(String path) {
            this.path = path;
            this.comment = null;
            this.entries = new TreeSet<>();
            this.tagEntries = new TreeSet<>();
        }

        public String path() {
            return path;
        }

        public String fieldName() {
            return path.replace("/", "_").toUpperCase(Locale.ROOT);
        }

        @Nullable
        public String getComment() {
            return comment;
        }

        public TagContents setComment(String comment) {
            if (!comment.endsWith("\n")) {
                throw new IllegalArgumentException("Comment must end with newline, ends with: " + comment.charAt(comment.length() - 1));
            }

            this.comment = comment;
            return this;
        }

        public SortedSet<String> getEntries() {
            return Collections.unmodifiableSortedSet(entries);
        }

        public TagContents addEntry(String entry) {
            if (entry.contains(":")) {
                throw new IllegalArgumentException("Entry cannot contain a namespace, the minecraft namespace is implicit: " + entry);
            }

            entries.add(entry);
            return this;
        }

        public SortedSet<String> getTagEntries() {
            return Collections.unmodifiableSortedSet(tagEntries);
        }

        public TagContents addTagEntry(String entry) {
            if (!entry.contains(":")) {
                throw new IllegalArgumentException("Tag entry must contain a namespace:" + entry);
            }

            String[] parts = entry.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Tag entry must contain a namespace and path only:" + entry);
            }

            if (!parts[0].equals("c") && !parts[0].equals("minecraft")) {
                throw new IllegalArgumentException("Namespace may only be c or minecraft, found: " + parts[0]);
            }

            tagEntries.add(entry);
            return this;
        }
    }
}
