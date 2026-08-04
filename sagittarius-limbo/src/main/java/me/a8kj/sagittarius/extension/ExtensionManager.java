package me.a8kj.sagittarius.extension;

import de.spacepotato.sagittarius.Sagittarius;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.a8kj.sagittarius.event.EventBus;
import me.a8kj.sagittarius.extension.actions.DefaultExtensionActions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the lifecycle of extensions, including loading, enabling, and disabling.
 *
 * @author a8kj7sea
 * @version 1.2.3
 */
@Slf4j
public class ExtensionManager {
    private final Sagittarius sagittarius;
    private final EventBus eventBus;
    private final ExtensionLoader loader;
    private final List<SagittariusExtension> extensions;
    private final File extensionsFolder;

    @Getter
    private SagittariusExtensionContext context;

    /**
     * Constructs a new ExtensionManager.
     *
     * @param sagittarius the server instance
     * @param eventBus the event bus
     */
    public ExtensionManager(Sagittarius sagittarius, EventBus eventBus) {
        this.sagittarius = sagittarius;
        this.eventBus = eventBus;
        this.loader = new JarExtensionLoader();
        this.extensions = new ArrayList<>();
        this.extensionsFolder = new File("extensions");

        this.context = new SagittariusExtensionContext(
                sagittarius,
                eventBus,
                log,
                new DefaultExtensionActions()
        );
    }

    /**
     * Loads all extensions from the extensions folder.
     */
    public void loadExtensions() {
        if (!extensionsFolder.exists()) {
            extensionsFolder.mkdir();
            return;
        }

        File[] files = extensionsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files == null) return;

        for (File file : files) {
            loader.load(file, context).ifPresent(ext -> {
                extensions.add(ext);
                ext.onEnable();
            });
        }
    }

    /**
     * Disables all loaded extensions.
     */
    public void disableExtensions() {
        for (SagittariusExtension extension : extensions) {
            try {
                log.info("Disabling extension: {}", extension.getClass().getSimpleName());
                extension.onDisable();
            } catch (Exception e) {
                log.error("Error while disabling extension", e);
            }
        }
        extensions.clear();
    }
}