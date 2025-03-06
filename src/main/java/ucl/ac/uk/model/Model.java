package ucl.ac.uk.model;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

import ucl.ac.uk.classes.Note;
import ucl.ac.uk.classes.NoteStorage;

public class Model
{
    final String jsonFilePath = "data/notes.json";
    private NoteStorage notes;

    public Model()
    {
        notes = new NoteStorage(jsonFilePath);
    }

    // This also returns dummy data. The real version should use the keyword parameter to search
    // the data and return a list of matching items.
    public List<String> searchFor(String keyword)
    {
        return List.of("Search keyword is: "+ keyword, "result1", "result2", "result3");
    }
}