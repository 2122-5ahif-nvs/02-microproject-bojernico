package at.htl.kochrezepte.control;

import at.htl.kochrezepte.entity.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ImportController {
    private static final String filePath = "/data/inputs.json";

    private static String path = "..";


    public static void changePathForTesting() {
        path = ".";
    }

    public static List<Recipe> ReadRecipesFromJson() {
        File f = new File(path);
        String path;
        try {
            path = f.getCanonicalPath();
        } catch (IOException e) {
            throw new IllegalArgumentException("You have some big errors in your program, download Windows 10 Home");
        }
        try {
            var text = Files.readString(Path.of(path + filePath));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
            return gson.fromJson(text, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
