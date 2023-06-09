package org.tbm.server.dungeons.dungeons.professions.datapack;

import org.tbm.server.dungeons.dungeons.professions.RegistryConstants;
import org.tbm.server.dungeons.dungeons.professions.profession.Profession;
import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionBuilder;
import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionEditorSerializer;
import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionSerializer;
import org.tbm.server.dungeons.dungeons.professions.profession.action.Action;
import org.tbm.server.dungeons.dungeons.professions.profession.action.Actions;
import org.tbm.server.dungeons.dungeons.professions.profession.conditions.ActionCondition;
import org.tbm.server.dungeons.dungeons.professions.profession.conditions.ActionConditions;
import org.tbm.server.dungeons.dungeons.professions.profession.editor.Append;
import org.tbm.server.dungeons.dungeons.professions.profession.editor.Editor;
import org.tbm.server.dungeons.dungeons.professions.profession.modifiers.BasicModifiers;
import org.tbm.server.dungeons.dungeons.professions.profession.modifiers.milestones.Milestone;
import org.tbm.server.dungeons.dungeons.professions.profession.modifiers.milestones.Milestones;
import org.tbm.server.dungeons.dungeons.professions.profession.modifiers.perks.Perk;
import org.tbm.server.dungeons.dungeons.professions.profession.modifiers.perks.Perks;
import org.tbm.server.dungeons.dungeons.professions.profession.rewards.Reward;
import org.tbm.server.dungeons.dungeons.professions.profession.rewards.Rewards;
import org.tbm.server.dungeons.dungeons.professions.profession.unlock.Unlock;
import org.tbm.server.dungeons.dungeons.professions.profession.unlock.Unlocks;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.gson.*;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ProfessionLoader extends SimpleJsonResourceReloadListener {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected static final Gson GSON = createProfessionSerializer()
            .setPrettyPrinting()
            .create();
    protected Map<ResourceLocation, Profession> professionMap = ImmutableMap.of();

    public ProfessionLoader() {
        super(GSON, "professions/occupations");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        professionMap = null;

        Map<ResourceLocation, ProfessionBuilder> temp = Maps.newHashMap();
        Multimap<ResourceLocation, Editor> editsMade = HashMultimap.create();

        object.forEach((location, jsonElement) -> {
            try {
                JsonObject jsonObject = GsonHelper.convertToJsonObject(jsonElement, "profession");
                if (jsonObject.has("type")) {
                    ResourceLocation type = new ResourceLocation(GsonHelper.getAsString(jsonObject, "type"));
                    ProfessionSerializer<? extends Profession, ? extends ProfessionBuilder> nullable = RegistryConstants.PROFESSION_SERIALIZER.get(type);
                    nullable = nullable != null ? nullable : ProfessionSerializer.DEFAULT_PROFESSION;
                    ProfessionBuilder profession = GSON.fromJson(jsonElement, nullable.getBuilderType());
                    profession.setKey(location);
                    temp.put(location, profession);
                } else if (jsonObject.has("function_type")) {
                    ResourceLocation type = new ResourceLocation(GsonHelper.getAsString(jsonObject, "function_type"));
                    Editor editor = RegistryConstants.PROFESSION_EDITOR_SERIALIZER.getOptional(type)
                            .orElseThrow(() -> new JsonSyntaxException("Invalid or unsupported editor type '" + type + "'"))
                            .deserialize(jsonObject, GSON);
                    editor.setLocation(location);
                    editsMade.put(editor.getProfessionKey(), editor);
                }
            } catch (Exception e) {
                LOGGER.warn("Could not finish deserializing profession {}, reason(s) for failing: {}", location.toString(), e.getMessage());
                throw e;
            }
        });
        // TODO: there should probably be some sort of editing order.
        for (Map.Entry<ResourceLocation, ProfessionBuilder> entry : temp.entrySet()) {
            // we will simply ignore any professions that don't exist.
            ResourceLocation key = entry.getKey();
            if (key != null) {
                for (Editor editor : editsMade.get(entry.getKey())) {
                    editor.applyEdit(entry.getValue());
                }
            }
        }
        Map<ResourceLocation, Profession> result = temp.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().build()));
        this.professionMap = ImmutableMap.copyOf(result);
    }

    private static GsonBuilder createProfessionSerializer() {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeHierarchyAdapter(Reward.class, Rewards.createGsonAdapter())
                .registerTypeHierarchyAdapter(ActionCondition.class, ActionConditions.createGsonAdapter())
                .registerTypeHierarchyAdapter(Action.class, Actions.createGsonAdapter())
                .registerTypeHierarchyAdapter(Unlock.class, Unlocks.createGsonAdapter())
                .registerTypeHierarchyAdapter(Milestone.class, Milestones.createGsonAdapter())
                .registerTypeHierarchyAdapter(Perk.class, Perks.createGsonAdapter())
                .registerTypeAdapter(BasicModifiers.class, new BasicModifiers.ModifierSerializer())
                .registerTypeAdapter(Append.class, ProfessionEditorSerializer.APPEND_EDITOR)
                .registerTypeAdapter(Profession.class, ProfessionSerializer.DEFAULT_PROFESSION)
                .registerTypeAdapter(ProfessionBuilder.class, ProfessionSerializer.DEFAULT_PROFESSION);
        // todo; event
        //ProfessionUtilityEvents.SERIALIZER_CALLBACK.invoker().addProfessionSerializer(builder);
        return builder;
    }

    @Nullable
    public Profession getProfession(ResourceLocation location) {
        return professionMap.get(location);
    }

    public Collection<Profession> getProfessions() {
        return professionMap.values();
    }

    public Set<ResourceLocation> getProfessionKeys() {
        return professionMap.keySet();
    }


    @Nullable
    public ResourceLocation getIDFromProfession(Profession profession) {
        for (Map.Entry<ResourceLocation, Profession> entry : professionMap.entrySet()) {
            if (entry.getValue().getKey().equals(profession.getKey())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void clearProfessions() {
        professionMap = ImmutableMap.of();
    }

    public static String serialize(JsonObject object) {
        return GSON.toJson(object);
    }

    public static JsonElement serialize(Profession profession) {
        return GSON.toJsonTree(profession);
    }

    public static JsonElement serialize(Editor unlock) {
        return GSON.toJsonTree(unlock);
    }

}
