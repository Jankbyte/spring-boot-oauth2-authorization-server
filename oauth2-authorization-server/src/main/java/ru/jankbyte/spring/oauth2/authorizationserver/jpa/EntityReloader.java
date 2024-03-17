package ru.jankbyte.spring.oauth2.authorizationserver.jpa;

/**
 * Contains methods for implementing into services for updating
 * some data of client.
 */
public interface EntityReloader<T> {
    /**
     * Refresh some data of entity.
     * @param entity The target entity for reloading
     */
    void reload(T entity);
}
