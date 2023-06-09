package org.tbm.server.dungeons.dungeons.professions.profession;

import org.tbm.server.dungeons.dungeons.professions.Constants;
import org.tbm.server.dungeons.dungeons.professions.RegistryConstants;
import org.tbm.server.dungeons.dungeons.professions.profession.editor.Append;
import org.tbm.server.dungeons.dungeons.professions.profession.editor.Editor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public interface ProfessionEditorSerializer<T extends Editor> extends JsonSerializer<T> {
    ProfessionEditorSerializer<Append> APPEND_EDITOR = registerSerializer(Constants.modID("append"), new Append.Serializer());

    T deserialize(JsonObject json, Gson gson);

    Class<T> getType();

    static <S extends ProfessionEditorSerializer<T>, T extends Editor> S registerSerializer(ResourceLocation id, S serializer) {
        return Registry.register(RegistryConstants.PROFESSION_EDITOR_SERIALIZER, id, serializer);
    }
}
