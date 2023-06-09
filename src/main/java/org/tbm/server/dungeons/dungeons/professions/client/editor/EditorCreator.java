package org.tbm.server.dungeons.dungeons.professions.client.editor;

import java.util.function.BiFunction;

public interface EditorCreator<T> extends BiFunction<Integer, Integer, T> {

}
