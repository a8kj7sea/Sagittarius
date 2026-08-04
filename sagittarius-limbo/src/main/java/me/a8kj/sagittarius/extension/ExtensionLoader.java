package me.a8kj.sagittarius.extension;

import java.io.File;
import java.util.Optional;

/**
 * Interface for loading extensions from files.
 *
 * @author a8kj7sea
 * @version 1.2.3
 */
public interface ExtensionLoader {
    /**
     * Loads an extension from the specified file.
     *
     * @param file the file to load the extension from
     * @param context the context to provide to the extension
     * @return an Optional containing the loaded extension, or empty if loading failed
     */
    Optional<SagittariusExtension> load(File file, ExtensionContext context);
}