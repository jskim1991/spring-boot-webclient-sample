package io.jay.springbootwebclientsample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonFileStubReader {

    public static String fileToJson(String fileName) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/" + fileName)));
        return json;
    }

}
