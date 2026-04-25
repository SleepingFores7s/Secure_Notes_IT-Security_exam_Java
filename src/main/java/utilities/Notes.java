package utilities;

import java.util.List;

public class Notes {

    private int id;
    private String title;
    private String noteData;
    private String noteOwner;

    public Notes(int id, String title, String noteData, String noteOwner) {
        this.id = id;
        this.title = title;
        this.noteData = noteData;
        this.noteOwner = noteOwner;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteData() {
        return this.noteData;
    }
    public void setNoteData(String noteData) {
        this.noteData = noteData;
    }
    public void printNoteData() {
        System.out.println(this.noteData);
    }

    public String getNoteOwner() {
        return this.noteOwner;
    }

}
