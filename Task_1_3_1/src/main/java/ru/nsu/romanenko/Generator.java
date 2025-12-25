package ru.nsu.romanenko;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Generator {
    private static final long TARGET_SIZE_BYTES = 5L * 1024 * 1024 * 1024 * 2;
    private static final String FILENAME = "huge_utf8_test.txt";
    private static final String SUBSTRING_TO_EMBED = "\uD83E\uDD23\uD83E\uDD23";

    public static void generate(){
        long bytesWritten = 0;
        Random random = new Random();

        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(FILENAME), StandardCharsets.UTF_8))) {

            writer.print(SUBSTRING_TO_EMBED);
            bytesWritten += SUBSTRING_TO_EMBED.getBytes(StandardCharsets.UTF_8).length;

            String block = "Lorem ipsum dolor sit amet, " +  "\uD83D\uDE48";
            byte[] blockBytes = block.getBytes(StandardCharsets.UTF_8);

            while (bytesWritten < TARGET_SIZE_BYTES) {
                writer.print(block);
                bytesWritten += blockBytes.length;

                if (random.nextInt(1000000) == 0) {
                    writer.print(SUBSTRING_TO_EMBED);
                    bytesWritten += SUBSTRING_TO_EMBED.getBytes(StandardCharsets.UTF_8).length;
                }
            }

            System.out.println("Success generated");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}