package ucl.ac.uk.model;

import java.util.List;

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