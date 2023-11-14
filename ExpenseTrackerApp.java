import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ExpenseTrackerApp extends JFrame {
    private JTextField expenseField;
    private JTextField itemField;
    private JComboBox<String> categoryComboBox;
    private JTextField dateField;
    private DefaultListModel<String> expenseListModel;

    public ExpenseTrackerApp() {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

       
        getContentPane().setBackground(new Color(173, 216, 230));
        getContentPane().setBackground(new Color(173, 216, 230));
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(173, 216, 230));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        


        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel amountLabel = new JLabel("Enter Amount:");
        inputPanel.add(amountLabel, gbc);

        gbc.gridx = 1;
        expenseField = new JTextField(15);
        inputPanel.add(expenseField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descriptionLabel = new JLabel("Enter Description:");
        inputPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        itemField = new JTextField(15);
        inputPanel.add(itemField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel categoryLabel = new JLabel("Enter Category:");
        inputPanel.add(categoryLabel, gbc);

        gbc.gridx = 1;
        String[] categories = {"Food", "Transportation", "Utilities", "Entertainment", "Other"};
        categoryComboBox = new JComboBox<>(categories);
        inputPanel.add(categoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel dateLabel = new JLabel("Enter Date:");
        inputPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        dateField = new JTextField(15);
        inputPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton addButton = new JButton("Add Expense");
        inputPanel.add(addButton, gbc);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });
        contentPanel.add(inputPanel, BorderLayout.NORTH);

        JPanel expenseListPanel = new JPanel(new BorderLayout());
        expenseListModel = new DefaultListModel<>();
        JList<String> expenseList = new JList<>(expenseListModel);
        JScrollPane scrollPane = new JScrollPane(expenseList);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        expenseListPanel.add(scrollPane, BorderLayout.CENTER);

        JButton showChartButton = new JButton("Show Expenses Chart");
        showChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showExpensesChart();
            }
        });
        expenseListPanel.add(showChartButton, BorderLayout.SOUTH);

        contentPanel.add(expenseListPanel, BorderLayout.CENTER);

        setContentPane(contentPanel);
    }

    private void addExpense() {
        String expense = expenseField.getText();
        String item = itemField.getText();
        String category = (String) categoryComboBox.getSelectedItem();
        String date = dateField.getText();

        if (!expense.isEmpty() && !date.isEmpty()) {
            String newExpense = expense + " - " + item + " - " + category + " (" + date + ")";
            expenseListModel.addElement(newExpense);

            
            expenseField.setText("");
            itemField.setText("");
            dateField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Expense and Date cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showExpensesChart() {
        HashMap<String, Double> categoryTotals = new HashMap<>();
        for (int i = 0; i < expenseListModel.getSize(); i++) {
            String expense = expenseListModel.getElementAt(i);
            String category = expense.split(" - ")[2].split(" ")[0];
            String amountStr = expense.split(" - ")[0];
            double amount = Double.parseDouble(amountStr);
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (String category : categoryTotals.keySet()) {
            dataset.setValue(category, categoryTotals.get(category));
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Expense Categories", dataset, true, true, false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));
        JFrame chartFrame = new JFrame();
        chartFrame.setContentPane(chartPanel);
        chartFrame.setSize(600, 500);
        chartFrame.setLocationRelativeTo(null);
        chartFrame.setVisible(true);
        String highestCategory = "";
        double highestAmount = 0.0;
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            if (entry.getValue() > highestAmount) {
                highestAmount = entry.getValue();
                highestCategory = entry.getKey();
            }
        }

        // Display specific warnings for different expense categories
        switch (highestCategory) {
            case "Food":
                JOptionPane.showMessageDialog(this,
                        "You spent the most on food! Consider eating in the mess to save expenses.",
                        "Expense Warning", JOptionPane.WARNING_MESSAGE);
                break;
            case "Entertainment":
                JOptionPane.showMessageDialog(this,
                        "You spent the most on entertainment! Please control your entertainment expenses.",
                        "Expense Warning", JOptionPane.WARNING_MESSAGE);
                break;
            case "Transportation":
                JOptionPane.showMessageDialog(this,
                        "You spent the most on transportation! Consider walking or using public transport.",
                        "Expense Warning", JOptionPane.WARNING_MESSAGE);
                break;
            // Add more cases for other categories if needed

            default:
                // If the highestCategory is not one of the predefined categories, display a general warning
                JOptionPane.showMessageDialog(this,
                        "You spent the most on " + highestCategory + ". Control your expenses in this category.",
                        "Expense Warning", JOptionPane.WARNING_MESSAGE);
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ExpenseTrackerApp app = new ExpenseTrackerApp();
                app.setVisible(true);
            }
        });
    }
}
