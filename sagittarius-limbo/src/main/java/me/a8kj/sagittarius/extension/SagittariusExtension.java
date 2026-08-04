package me.a8kj.sagittarius.extension;

/**
 * Interface for all extensions.
 *
 * @author a8kj7sea
 * @version 1.2.3
 */
public interface SagittariusExtension {
    /**
     * Gets the metadata for this extension.
     *
     * @return the metadata
     */
    ExtensionMetadata getMetadata();

    /**
     * Called when the extension is loaded.
     *
     * @param context the context provided to the extension
     */
    default void onLoad(ExtensionContext context) {
    }

    /**
     * Called when the extension is enabled.
     */
    default void onEnable() {
    }

    /**
     * Called when the extension is disabled.
     */
    default void onDisable() {
    }
}