package org.tbm.server.dungeons.dungeons.professions.client.format;

public interface FormatBuilder<T> {

    default Format<T> buildDefaultFormat() {
        return deserializeToFormat(null);
    }

    Format<T> deserializeToFormat(T t);

}
