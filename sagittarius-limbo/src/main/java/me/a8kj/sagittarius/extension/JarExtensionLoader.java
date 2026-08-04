package me.a8kj.sagittarius.extension;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Loader for extensions packaged as JAR files.
 *
 * @author a8kj7sea
 * @version 1.2.3
 */
@Slf4j
public class JarExtensionLoader implements ExtensionLoader {

    /**
     * Loads an extension from the specified JAR file.
     *
     * @param file the JAR file
     * @param context the context to provide to the extension
     * @return an Optional containing the loaded extension, or empty if loading failed
     */
    @Override
    public Optional<SagittariusExtension> load(File file, ExtensionContext context) {
        try (JarFile jarFile = new JarFile(file)) {
            JarEntry entry = jarFile.getJarEntry("extension.yml");
            if (entry == null) {
                log.warn("Cannot find extension.yml in {}. Skipping...", file.getName());
                return Optional.empty();
            }

            try (InputStream is = jarFile.getInputStream(entry)) {
                Yaml yaml = new Yaml();
                Map<String, Object> data = yaml.load(is);

                String mainClass = (String) data.get("main");
                String name = (String) data.getOrDefault("name", "Unknown");
                String version = (String) data.getOrDefault("version", "1.0");
                String author = (String) data.getOrDefault("author", "Unknown");

                URL url = file.toURI().toURL();
                URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, getClass().getClassLoader());
                Class<?> clazz = classLoader.loadClass(mainClass);

                if (!SagittariusExtension.class.isAssignableFrom(clazz)) {
                    log.warn("Class {} does not implement SagittariusExtension!", mainClass);
                    return Optional.empty();
                }

                SagittariusExtension extension = (SagittariusExtension) clazz.getDeclaredConstructor().newInstance();
                extension.onLoad(context);

                log.info("Loaded extension: {} v{} by {}", name, version, author);
                return Optional.of(extension);
            }
        } catch (Exception e) {
            log.error("Failed to load extension from file: {}", file.getName(), e);
            return Optional.empty();
        }
    }
}