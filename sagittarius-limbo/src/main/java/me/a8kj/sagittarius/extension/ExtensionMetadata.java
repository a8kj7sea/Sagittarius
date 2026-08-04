package me.a8kj.sagittarius.extension;

import lombok.Value;

/**
 * Represents metadata for an extension.
 *
 * @author a8kj7sea
 * @version 1.2.3
 */
@Value
public class ExtensionMetadata {
    String name;
    String version;
    String author;
}