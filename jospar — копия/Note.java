package kz.yerzhan.jospar;

public class Note {
    private long id;
    private String description;

    public Note(){}

    public Note(String description){this.description =description;}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
