import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        }
    }

    public Task getTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            return tasks.get(index);
        }
        throw new IndexOutOfBoundsException("Invalid task index: " + index);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void sortTasksByPriority() {
        tasks.sort((t1, t2) -> Integer.compare(t1.getPriority(), t2.getPriority()));
    }
}
