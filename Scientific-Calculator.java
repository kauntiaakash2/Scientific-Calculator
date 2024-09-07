// ScientificCalculator.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ScientificCalculator {
    private JFrame frame;
    private CalculatorPanel calculatorPanel;

    public ScientificCalculator() {
        frame = new JFrame("Scientific Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250, 300);

        calculatorPanel = new CalculatorPanel();
        frame.add(calculatorPanel);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new ScientificCalculator();
    }
}

// CalculatorPanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CalculatorPanel extends JPanel {
    private CalculatorModel calculatorModel;
    private JTextField displayField;
    private List<CalculatorButton> buttons;

    public CalculatorPanel() {
        calculatorModel = new CalculatorModel();
        displayField = new JTextField(20);
        buttons = new ArrayList<>();

        setLayout(new BorderLayout());
        add(displayField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 4));
        add(buttonPanel, BorderLayout.CENTER);

        createButtons(buttonPanel);
    }

    private void createButtons(JPanel buttonPanel) {
        String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+"
        };

        for (String label : buttonLabels) {
            CalculatorButton button = new CalculatorButton(label);
            button.addActionListener(new ButtonListener());
            buttons.add(button);
            buttonPanel.add(button);
        }
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CalculatorButton button = (CalculatorButton) e.getSource();
            String label = button.getLabel();

            if (label.equals("=")) {
                calculatorModel.calculate(displayField.getText());
                displayField.setText(calculatorModel.getResult());
            } else {
                displayField.setText(displayField.getText() + label);
            }
        }
    }
}

// CalculatorButton.java
import javax.swing.*;
import java.awt.*;

public class CalculatorButton extends JButton {
    private String label;

    public CalculatorButton(String label) {
        super(label);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

// CalculatorModel.java
public class CalculatorModel {
    private String result;

    public void calculate(String expression) {
        // Implement calculation logic here
        // For simplicity, we'll just use the `eval` method
        result = String.valueOf(eval(expression));
    }

    public String getResult() {
        return result;
    }

    private double eval(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (pos < expression.length()) ? expression.charAt(pos++) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                return parseExpression();
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) {
                        x += parseTerm(); // addition
                    } else if (eat('-')) {
                        x -= parseTerm(); // subtraction
                    } else {
                        break;
                    }
                }
                return x;
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) {
                        x *= parseFactor(); // multiplication
                    } else if (eat('/')) {
                        x /= parseFactor(); // division
                    } else {
                        break;
                    }
                }
                return x;
            }

            double parseFactor() {
                double x;
                int startPos = this.pos;
                if (eat('+')) {
                    x = parseFactor(); // unary plus
                } else if (eat('-')) {
                    x = -parseFactor(); // unary minus
                } else if ((ch >= '0' && ch <=