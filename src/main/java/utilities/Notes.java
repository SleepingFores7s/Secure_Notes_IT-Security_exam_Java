package utilities;

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
        return id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteData() {
        return noteData;
    }
    public void setNoteData(String noteData) {
        this.noteData = noteData;
    }

    public String getNoteOwner() {
        return noteOwner;
    }

    //TODO - Create method/methods for printing notes,
    // NOT from a List, only single objects, loop can be in LoggedInPanel
}
