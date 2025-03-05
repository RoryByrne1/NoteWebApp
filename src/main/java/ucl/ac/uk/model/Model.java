package ucl.ac.uk.model;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

public class Model
{
    final String jsonFilePath = "data/notes.json";

    // read the JSON file into a map, categories -> notes -> note details
    public Map<String, Map<String, Map<String, String>>> readFile(String filePath)
    {
        // category : notes
        Map<String, Map<String, Map<String, String>>> categoriesMap = new HashMap<>();

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(filePath));

            // iterate through categories
            for (Object categoryKey : jsonObject.keySet()) {
                String category = (String) categoryKey;
                JSONObject categoryNotes = (JSONObject) jsonObject.get(category);

                // note name : note details
                Map<String, Map<String, String>> notesMap = new HashMap<>();

                // iterate through notes
                for (Object noteKey : categoryNotes.keySet()) {
                    String noteTitle = (String) noteKey;
                    JSONObject noteDetails = (JSONObject) categoryNotes.get(noteTitle);

                    Map<String, String> note = new HashMap<>();
                    note.put("content", (String) noteDetails.get("content"));
                    note.put("timestamp", (String) noteDetails.get("timestamp"));

                    notesMap.put(noteTitle, note);
                }
                categoriesMap.put(category, notesMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return categoriesMap;
    }

    public Map<String, Map<String, String>> getNotes(String category)
    {
        return readFile(jsonFilePath).get(category);
    }

    public Map<String, String> getNote(String category, String name)
    {
        return getNotes(category).get(name);
    }

    // This also returns dummy data. The real version should use the keyword parameter to search
    // the data and return a list of matching items.
    public List<String> searchFor(String keyword)
    {
        return List.of("Search keyword is: "+ keyword, "result1", "result2", "result3");
    }
}