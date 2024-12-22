public class Task {
    private String title;
    private String description;
    private int priority; // 1 = High, 2 = Medium, 3 = Low

    public Task(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return title + " (Priority: " + priority + ") - " + description;
    }

    public String toFileFormat() {
        return title + "||" + description + "||" + priority;
    }

    public static Task fromFileFormat(String line) {
        String[] parts = line.split("\\|\\|");
        String title = parts[0];
        String description = parts[1];
        int priority = Integer.parseInt(parts[2]);
        return new Task(title, description, priority);
    }
}
