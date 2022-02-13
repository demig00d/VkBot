package ru.vkbot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;

public class Config {
    public final Integer groupId;
    public final String accessToken;

    public Config(@JsonProperty("group_id") Integer groupId,
                  @JsonProperty("access_token") String accessToken) {
        this.groupId = groupId;
        this.accessToken = accessToken;
    }

    public static Config read(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Paths.get(path).toFile(), Config.class);
    }
}
