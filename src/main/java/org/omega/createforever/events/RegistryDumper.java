package org.omega.createforever.events;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.omega.createforever.CreateForever;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@net.neoforged.fml.common.Mod(CreateForever.MODID)
public class RegistryDumper {

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        JsonObject root = new JsonObject();

        // Dump blocks
        JsonArray blocksArray = new JsonArray();
        for (Block block : BuiltInRegistries.BLOCK) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            if (id != null) {
                JsonObject obj = new JsonObject();
                obj.addProperty("id", id.toString());
                obj.addProperty("class", block.getClass().getName());
                blocksArray.add(obj);
            }
        }

        root.add("blocks", blocksArray);

        // Dump items
        JsonArray itemsArray = new JsonArray();
        for (Item item : BuiltInRegistries.ITEM) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            if (id != null) {
                JsonObject obj = new JsonObject();
                obj.addProperty("id", id.toString());
                obj.addProperty("class", item.getClass().getName());
                itemsArray.add(obj);
            }
        }
        root.add("items", itemsArray);

        // Write JSON file
        try {
            File file = new File("registry_dump.json");
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(root, writer);
            writer.close();
            System.out.println("Registry dump saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void init(){}
}