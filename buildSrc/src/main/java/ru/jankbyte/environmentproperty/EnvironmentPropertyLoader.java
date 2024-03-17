package ru.jankbyte.environmentproperty;

import java.util.Map;

public interface EnvironmentPropertyLoader {
    Map<String, String> getProperties();
}
