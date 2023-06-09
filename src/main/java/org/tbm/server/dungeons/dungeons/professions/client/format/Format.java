package org.tbm.server.dungeons.dungeons.professions.client.format;

import org.apache.commons.lang3.function.TriFunction;

public interface Format<T> {

    TriFunction<Integer, Integer, Integer, FormatEntryBuilder<T>> entries();
}
