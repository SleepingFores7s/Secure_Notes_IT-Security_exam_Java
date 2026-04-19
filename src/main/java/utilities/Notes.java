package utilities;

public class Notes {

    private String title;
    private String note;
    private String noteOwner;

    public Notes(String title, String note, String noteOwner) {
        this.title = title;
        this.note = note;
        this.noteOwner = noteOwner;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteOwner() {
        return noteOwner;
    }
    public void setNoteOwner(String noteOwner) {
        this.noteOwner = noteOwner;
    }
}
