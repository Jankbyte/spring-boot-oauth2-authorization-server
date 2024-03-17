package ru.jankbyte.spring.oauth2.authorizationserver.utils;

import java.util.Map;
import java.util.HashMap;
import java.util.stream.Stream;
import java.net.URI;

public final class UrlUtils {
    private UrlUtils() {}

    public static Map<String, String> getQueryParameters(String url) {
        URI uri = URI.create(url);
        String query = uri.getQuery();
        String[] keyValueParams = query.split("&");
        Map<String, String> queryParams = new HashMap<>();
        Stream.of(keyValueParams).forEach(keyValueParam -> {
            String[] keyValue = keyValueParam.split("=");
            String key = keyValue[0];
            String value = keyValue[1];
            queryParams.put(key, value);
        });
        return queryParams;
    }
}
