import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.util.List;

public class TaskListGUI extends JFrame {
    private TaskManager taskManager;
    private DefaultTableModel tableModel;

    public TaskListGUI() {
        taskManager = new TaskManager();

        setTitle("TODO List");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Custom panel for background image
        BackgroundPanel backgroundPanel = new BackgroundPanel("background.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Load tasks from file
        loadTasksFromFile();

        // Task Table
        String[] columnNames = {"Title", "Description", "Priority"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable taskTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setBackground(row % 2 == 0 ? new Color(230, 230, 230, 150) : new Color(255, 255, 255, 150)); // Semi-transparent rows
                return c;
            }
        };
        taskTable.setOpaque(false); // Make table transparent
        taskTable.setFont(new Font("Arial", Font.PLAIN, 14));
        taskTable.setRowHeight(25);

        // Configure the header
        JTableHeader header = taskTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent black background
        header.setForeground(Color.WHITE); // White text color for contrast
        header.setOpaque(true); // Ensure the background is drawn

        // JScrollPane for the table
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setOpaque(false); // Make scroll pane transparent
        scrollPane.getViewport().setOpaque(false); // Make viewport transparent
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        // Input Section
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false); // Make panel transparent
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField titleField = new JTextField(15);
        JTextField descriptionField = new JTextField(15);
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"1 - High", "2 - Medium", "3 - Low"});
        JButton addButton = new JButton("Add Task");
        addButton.setBackground(new Color(144, 238, 144)); // Light green
        addButton.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
        addButton.setPreferredSize(new Dimension(150, 40)); // Set a larger button size

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Labels with larger font and solid background
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font size
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 0, 0, 200)); // Solid black background
        titleLabel.setForeground(Color.WHITE);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font size
        descriptionLabel.setOpaque(true);
        descriptionLabel.setBackground(new Color(0, 0, 0, 200)); // Solid black background
        descriptionLabel.setForeground(Color.WHITE);

        JLabel priorityLabel = new JLabel("Priority:");
        priorityLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font size
        priorityLabel.setOpaque(true);
        priorityLabel.setBackground(new Color(0, 0, 0, 200)); // Solid black background
        priorityLabel.setForeground(Color.WHITE);

        inputPanel.add(titleLabel, gbc);
        gbc.gridy++;
        inputPanel.add(titleField, gbc);
        gbc.gridy++;
        inputPanel.add(descriptionLabel, gbc);
        gbc.gridy++;
        inputPanel.add(descriptionField, gbc);
        gbc.gridy++;
        inputPanel.add(priorityLabel, gbc);
        gbc.gridy++;
        inputPanel.add(priorityBox, gbc);
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(addButton, gbc);

        JPanel inputWrapper = new JPanel(new BorderLayout());
        inputWrapper.setOpaque(false); // Transparent wrapper
        inputWrapper.add(inputPanel, BorderLayout.CENTER);
        inputWrapper.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);

        backgroundPanel.add(inputWrapper, BorderLayout.NORTH);

        // Button Section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Transparent panel

        JButton deleteButton = new JButton("Delete Task");
        deleteButton.setBackground(new Color(255, 182, 193)); // Light red
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
        deleteButton.setPreferredSize(new Dimension(150, 40)); // Set a larger button size

        JButton sortButton = new JButton("Sort by Priority");
        sortButton.setBackground(new Color(144, 238, 144)); // Light green
        sortButton.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
        sortButton.setPreferredSize(new Dimension(150, 40)); // Set a larger button size

        JButton saveButton = new JButton("Save Tasks");
        saveButton.setBackground(new Color(144, 238, 144)); // Light green
        saveButton.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
        saveButton.setPreferredSize(new Dimension(150, 40)); // Set a larger button size

        buttonPanel.add(deleteButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(saveButton);

        JPanel buttonWrapper = new JPanel(new BorderLayout());
        buttonWrapper.setOpaque(false); // Transparent wrapper
        buttonWrapper.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.NORTH);
        buttonWrapper.add(buttonPanel, BorderLayout.CENTER);

        backgroundPanel.add(buttonWrapper, BorderLayout.SOUTH);

        // Add Task Action
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();
            if (title.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and Description cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int priority = priorityBox.getSelectedIndex() + 1;

            Task newTask = new Task(title, description, priority);
            taskManager.addTask(newTask);
            tableModel.addRow(new Object[]{title, description, priority});

            titleField.setText("");
            descriptionField.setText("");
        });

        // Delete Task Action
        deleteButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                taskManager.removeTask(selectedRow);
                updateTaskTable(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to delete.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Sort Tasks by Priority Action
        sortButton.addActionListener(e -> {
            taskManager.sortTasksByPriority(); // Sort tasks by priority
            updateTaskTable(); // Update the table to reflect sorted tasks
        });

        // Save Tasks Action
        saveButton.addActionListener(e -> saveTasksToFile());

        // Populate task table with loaded tasks
        for (Task task : taskManager.getTasks()) {
            tableModel.addRow(new Object[]{task.getTitle(), task.getDescription(), task.getPriority()});
        }

        setVisible(true);
    }

    private void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
            for (Task task : taskManager.getTasks()) {
                writer.write(task.toFileFormat());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Tasks saved successfully!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving tasks: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTasksFromFile() {
        File file = new File("tasks.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = Task.fromFileFormat(line);
                taskManager.addTask(task);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading tasks: " + ex.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTaskTable() {
        // Clear the existing rows in the table
        tableModel.setRowCount(0);

        // Add the sorted tasks back to the table
        for (Task task : taskManager.getTasks()) {
            tableModel.addRow(new Object[]{task.getTitle(), task.getDescription(), task.getPriority()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskListGUI::new);
    }

    // Custom JPanel for background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading background image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
