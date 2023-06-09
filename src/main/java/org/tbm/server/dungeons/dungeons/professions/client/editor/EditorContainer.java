package org.tbm.server.dungeons.dungeons.professions.client.editor;

import org.tbm.server.dungeons.dungeons.professions.client.editors.DatapackEditor;

/**
 * A class to store extra data about an {@link DatapackEditor editor} so we can display extra information to the user.
 */
public class EditorContainer<T> {

    private final boolean modded;
    private final String displayName;
    private final EditorCreator<T> creator;

    public EditorContainer(String displayName, boolean modded, EditorCreator<T> creator) {
        this.displayName = displayName;
        this.modded = modded;
        this.creator = creator;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isModded() {
        return modded;
    }

    public boolean isNotModded() {
        return !modded;
    }


    public EditorCreator<T> getCreator() {
        return creator;
    }
}
