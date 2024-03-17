package ru.jankbyte.environmentproperty;

import java.util.List;

public abstract class AbstractEnvironmentPropertyLoader
        implements EnvironmentPropertyLoader {
    protected final List<String> profiles;

    public AbstractEnvironmentPropertyLoader(List<String> profiles) {
        this.profiles = profiles;
    }
}
