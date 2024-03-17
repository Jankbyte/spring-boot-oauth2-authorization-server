package ru.jankbyte.environmentproperty;

import java.util.*;
import java.io.UncheckedIOException;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Collectors;

public class FilePropertyLoader
        extends AbstractEnvironmentPropertyLoader {
    private final Map<String, String> allProperties = new HashMap<>();

    public FilePropertyLoader(String folderPath, List<String> profiles) {
        super(profiles);
        loadProperties(folderPath);
    }

    @Override
    public Map<String, String> getProperties() {
        return allProperties;
    }

    private void loadProperties(String folderPath) {
        String regex = profiles.stream()
            .collect(Collectors.joining("|", "^(", ").properties$"));
        File folder = new File(folderPath);
        File[] files = folder.listFiles(file -> {
            if (file.isDirectory()) {
                return false;
            }
            String fileName = file.getName();
            return fileName.matches(regex);
        });
        for (File file : files) {
            try(InputStream is = new FileInputStream(file)) {
                loadPropertiesFromStream(is);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
    }

    private void loadPropertiesFromStream(InputStream is) throws IOException {
        Properties props = new Properties();
        props.load(is);
        props.stringPropertyNames().stream().forEach(key -> {
            String value = props.getProperty(key);
            allProperties.put(key, value);
        });
    }
}
