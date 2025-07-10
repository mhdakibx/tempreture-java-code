import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TemperatureConverter extends JFrame implements ActionListener {

  private JComboBox<String> fromUnit, toUnit;
  private JTextField inputField;
  private JLabel resultLabel, formulaLabel;
  private JButton convertButton, clearButton, exitButton;

  public TemperatureConverter() {
    setTitle("🌡 Temperature Converter");
    setSize(700, 500);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new GridBagLayout());
    getContentPane().setBackground(Color.WHITE);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(12, 12, 12, 12);

    Font labelFont = new Font("Arial", Font.BOLD, 20);
    Font fieldFont = new Font("Arial", Font.PLAIN, 20);

    String[] units = { "Celsius", "Fahrenheit", "Kelvin" };

    // Input Field
    JLabel valueLabel = new JLabel("Value:");
    valueLabel.setFont(labelFont);
    inputField = new JTextField("Enter Your Value:", 15);
    inputField.setFont(fieldFont);
    inputField.setForeground(Color.GRAY);
    inputField.setBorder(BorderFactory.createCompoundBorder(
        inputField.getBorder(),
        BorderFactory.createEmptyBorder(8, 10, 8, 10)));

    // Placeholder behavior
    inputField.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (inputField.getText().equals("Enter Your Value:")) {
          inputField.setText("");
          inputField.setForeground(Color.BLACK);
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        if (inputField.getText().isEmpty()) {
          inputField.setText("Enter Your Value:");
          inputField.setForeground(Color.GRAY);
        }
      }
    });

    gbc.gridx = 0;
    gbc.gridy = 0;
    add(valueLabel, gbc);
    gbc.gridx = 1;
    add(inputField, gbc);

    // From Unit
    JLabel fromLabel = new JLabel("From:");
    fromLabel.setFont(labelFont);
    fromUnit = new JComboBox<>(units);
    fromUnit.setFont(fieldFont);

    gbc.gridx = 0;
    gbc.gridy = 1;
    add(fromLabel, gbc);
    gbc.gridx = 1;
    add(fromUnit, gbc);

    // To Unit
    JLabel toLabel = new JLabel("To:");
    toLabel.setFont(labelFont);
    toUnit = new JComboBox<>(units);
    toUnit.setFont(fieldFont);

    gbc.gridx = 0;
    gbc.gridy = 2;
    add(toLabel, gbc);
    gbc.gridx = 1;
    add(toUnit, gbc);

    // Result Label
    resultLabel = new JLabel("Result: ");
    resultLabel.setFont(new Font("Arial", Font.BOLD, 22));
    resultLabel.setForeground(Color.BLUE);
    resultLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
    resultLabel.setOpaque(true);
    resultLabel.setBackground(new Color(230, 240, 255));
    resultLabel.setPreferredSize(new Dimension(300, 50));
    resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    add(resultLabel, gbc);

    // Formula Label
    formulaLabel = new JLabel("Formula: ");
    formulaLabel.setFont(new Font("Arial", Font.ITALIC, 18));
    formulaLabel.setForeground(Color.DARK_GRAY);
    formulaLabel.setPreferredSize(new Dimension(400, 30));
    formulaLabel.setHorizontalAlignment(SwingConstants.CENTER);

    gbc.gridy = 4;
    add(formulaLabel, gbc);

    // Buttons
    convertButton = new JButton("Convert");
    clearButton = new JButton("Clear");
    exitButton = new JButton("Exit");

    JButton[] buttons = { convertButton, clearButton, exitButton };
    for (JButton btn : buttons) {
      btn.setFont(fieldFont);
      btn.setForeground(Color.BLUE);
      btn.setBackground(Color.BLACK);
      btn.setFocusPainted(false);
      btn.setPreferredSize(new Dimension(140, 40));
      btn.addActionListener(this);
    }

    gbc.gridy = 5;
    JPanel btnPanel = new JPanel();
    btnPanel.setBackground(Color.WHITE);
    btnPanel.add(convertButton);
    btnPanel.add(clearButton);
    btnPanel.add(exitButton);
    add(btnPanel, gbc);

    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == convertButton) {
      try {
        String inputText = inputField.getText().trim();
        if (inputText.equals("Enter Your Value:") || inputText.isEmpty()) {
          JOptionPane.showMessageDialog(this, "Please enter a value!", "Warning", JOptionPane.WARNING_MESSAGE);
          return;
        }

        double input = Double.parseDouble(inputText);
        String from = (String) fromUnit.getSelectedItem();
        String to = (String) toUnit.getSelectedItem();

        double result = convertTemperature(input, from, to);
        resultLabel.setText("Result: " + String.format("%.2f", result) + " " + to);
        formulaLabel.setText("Formula: " + getFormula(from, to));
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } else if (e.getSource() == clearButton) {
      inputField.setText("Enter Your Value:");
      inputField.setForeground(Color.GRAY);
      resultLabel.setText("Result: ");
      formulaLabel.setText("Formula: ");
    } else if (e.getSource() == exitButton) {
      System.exit(0);
    }
  }

  private double convertTemperature(double value, String from, String to) {
    if (from.equals(to))
      return value;

    // Convert to Celsius first
    if (from.equals("Fahrenheit"))
      value = (value - 32) * 5 / 9;
    else if (from.equals("Kelvin"))
      value = value - 273.15;

    // Then convert from Celsius
    if (to.equals("Fahrenheit"))
      return value * 9 / 5 + 32;
    else if (to.equals("Kelvin"))
      return value + 273.15;
    else
      return value;
  }

  private String getFormula(String from, String to) {
    if (from.equals(to))
      return "No conversion needed.";

    if (from.equals("Celsius") && to.equals("Fahrenheit"))
      return "(°C × 9/5) + 32 = °F";
    else if (from.equals("Celsius") && to.equals("Kelvin"))
      return "°C + 273.15 = K";
    else if (from.equals("Fahrenheit") && to.equals("Celsius"))
      return "(°F − 32) × 5/9 = °C";
    else if (from.equals("Fahrenheit") && to.equals("Kelvin"))
      return "((°F − 32) × 5/9) + 273.15 = K";
    else if (from.equals("Kelvin") && to.equals("Celsius"))
      return "K − 273.15 = °C";
    else if (from.equals("Kelvin") && to.equals("Fahrenheit"))
      return "((K − 273.15) × 9/5) + 32 = °F";

    return "";
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(TemperatureConverter::new);
  }
}