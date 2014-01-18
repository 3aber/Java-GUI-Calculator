/*
 * Filename: Calculator.java
 * Author: Arka Ganguli/ 2s03 assignment
 */
/*NOTE: This was an assignment for my programming course (a lot of the code was provided)
 * However i had to modify the code to add functionality to the buttons and perform operations
 * using the other files (divide/plus/times/minus/expr.java etc) 
 * These files were modified as well to actually perform each operation. 
 */
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static java.util.Locale.US;
import static java.lang.String.format;


/*
 * We're going to be using the Swing package.
 * It provides a set of "lightweight" (all-Java language) components that
 * (as best as possible) work the same on all platforms.
 *
 * The general hierarchy is: text fields, labels, buttons etc. are ADDED to panels
 * and panels are ADDED to frames.
 *
 * We are going to use panels, frames, text fields and button so we import them.
 */
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

class CalculatorFrame extends JFrame {

	private static final int NUMBER_PAD_WIDTH = 4;
	private static final int NUMBER_PAD_HEIGHT = 5;
//test
	private static final int CALCULATOR_WIDTH = 1;
	private static final int CALCULATOR_HEIGHT = 2;

	private static boolean mode;
	/* In the end this is what we're looking for
	 *    -------------------
	 *    |               0 |
	 *    -------------------
	 *    | ( | ) | % | AC  |
	 *    -------------------
	 *    | 7 | 8 | 9 |  /  |
	 *    -------------------
	 *    | 4 | 5 | 6 |  *  |
	 *    -------------------
	 *    | 1 | 2 | 3 |  -  |
	 *    -------------------
	 *    | 0 | . | = |  +  |
	 *    -------------------
	 */

	private static final long serialVersionUID = 1L;

	private JPanel mainPanel, resultPanel, numberPanel;

	private static Set<String> operators = new HashSet<String>();
	private static Set<String> digits = new HashSet<String>();
	JButton integerMode, floatMode;

	// Static blocks work like a constructor, but are outside out constructor
	// and typically only used for variable initialization. Lets create a set
	// for our symbols and operators!
	static {

		for(String x : new String[]{"(", ")", "+", "-", "*", "/", "B", "=", " ", "AC"})
			operators.add(x);

		for(int i = 0; i < 10; i++)
			digits.add(Integer.toString(i));
	}

	// The indicates whether the next digit-press should clear the screen or not.
	private boolean clearResultField = true;

	// The first number entered and stored into our calculator.
	private Integer firstNumber = null;

	// The action/operation entered and stored into our calculator.
	private String action = null;

	/* and finally we need a JTextField to hold our results */
	private JTextField resultField;

	public CalculatorFrame() {

		/* Set the title of the window. */
		super("The coolest calculator ever");

		/* result panel */
		resultPanel = new JPanel();

		/* FlowLayout positions components row wise from left to right */
		resultPanel.setLayout(new FlowLayout());

		/* We want it to initially say 0 and have a width of about 20 columns */
		resultField = new JTextField("0", 20);

		/* Add the text field to its panel */
		resultPanel.add(resultField);

		/* Set the alignment */
		resultField.setHorizontalAlignment(JTextField.RIGHT);

		/* We don't want it to be editable we just want to display results */
		resultField.setEditable(false);

		/* number panel*/
		numberPanel = new JPanel();

		numberPanel.setLayout(new GridLayout(NUMBER_PAD_HEIGHT, NUMBER_PAD_WIDTH));

		Map<String, JButton> buttons = new HashMap<String, JButton>();

		// Lets build the buttons for digits.
		integerMode = new JButton("Integer");
		floatMode = new JButton("Float");

		for(String x : digits)
			buttons.put(x, new JButton(x));

		// Lets build the buttons for operators.
		for(String x : operators)
			buttons.put(x, new JButton(x));

		// The numbers will appear organized as we have them here. This does not have
		// to be a 2-dimensional array, but it helps visualize things better.
		String[][] buttonOrder = new String[][]{

				{ "(", ")", "B", "AC" },
				{ "7", "8", "9", "/" },
				{ "4", "5", "6", "*" },
				{ "1", "2", "3", "-" },
				{ "0", " ", "=", "+" }
		};

		numberPanel.add(integerMode);
		numberPanel.add(floatMode);

		// Lets add our rows to the number panel!
		for(int i = 0; i < NUMBER_PAD_HEIGHT; i++)
			for(int j = 0; j < NUMBER_PAD_WIDTH; j++)
				numberPanel.add(buttons.get(buttonOrder[i][j]));


		// Create and add a digit listener to each digit button. Check the implementation
		// of buildDigitListener() below!

		ActionListener digitListener = buildDigitListener();

		for(String x : digits)
			buttons.get(x).addActionListener(digitListener);

		// Create and add an operator listener to each operator button. Check the implementation
		// of buildOperatorListener() below!

		ActionListener operatorListener = buildOperatorListener();

		for(String x : operators)
			buttons.get(x).addActionListener(operatorListener);

		//action listener for integer/float mode
		ActionListener a = buildModeListener();
		integerMode.addActionListener(a);
		floatMode.addActionListener(a);

		/* we then create our mainPanel which we're going to add everything else to */
		mainPanel = new JPanel();

		/* we make it have 2 rows and 1 column so we can stack our panels */
		mainPanel.setLayout(new GridLayout(CALCULATOR_HEIGHT, CALCULATOR_WIDTH));

		/* and we add both in the order we want them to be displayed */
		mainPanel.add(resultPanel);
		mainPanel.add(numberPanel);

		/* We add our mainPanel to the JFrame */
		add(mainPanel);

		/* Specify that the window should close when the exit button provided by the OS is clicked. */
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* We then call pack() which causes the window to be sized
		 * to fit the preferred size and layouts of its subcomponents
		 */
		pack();

		/* and finally set it's visibility to true */
		setVisible(true);
	}

	/**
	 * Creates an action listener for digits. This should only be called once!
	 * 
	 * @return An action listener for digits.
	 */
	private ActionListener buildDigitListener(){

		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				/*
				 * We know only JButtons will trigger this event, so it is safe to type case
				 * the 'source' of the event to a JButton type.
				 */
				JButton j = (JButton) e.getSource();

				// If the result field should be cleared, do so while adding this digit. Lets also
				// prevent leading zeroes.
				if(clearResultField || resultField.getText().equals("0")){
					resultField.setText(j.getText());
					clearResultField = false;
				}
				// We probably should not be relying on the text in the digit as our 'identifier'
				// for that button, but it is sufficient for our purposes for now.

				else {

					resultField.setText(resultField.getText() + j.getText());
				}
			}
		};
	}

	/**
	 * Creates an action listener for operators. This should only be called once!
	 * 
	 * @return An action listener for operators.
	 */
//added new action listener for integer/float mode
	private ActionListener buildModeListener(){
		return new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JButton j = (JButton) e.getSource();

				if(j.getText() == "Integer"){
					mode = true;
				}
				if(j.getText() == "Float"){
					mode = false;
				}
			}
		};
	}
	private ActionListener buildOperatorListener(){

		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				/*
				 * We are only going to support +, -, *, / and % operations.
				 * (, ) and . are beyond the needs of our basic calculator.
				 */

				// Upon entering an operation, the next digits a user enters should clear the result field.
				clearResultField = false;

				JButton j = (JButton) e.getSource();

				String operator = j.getText();

				/*
				 * This is not a very good way for identifying which operator was pressed (relying on the caption
				 * text of the button), but it is good enough for demonstration purposes! We are 'switching' on the
				 * first character of the operator button caption, because that's enough to identify the operation.
				 * 
				 * If this were used under Java 7, we would be able to switch on the String itself.
				 */
				switch(operator.charAt(0)){
				case 'A': // The clear operation.

					resultField.setText("0");
					action = null;
					firstNumber = null;

					break; // If you are missing 'break', the next case will execute too!

				case '(':
					resultField.setText(resultField.getText()+ "(");
					break;

				case ')':
					resultField.setText(resultField.getText()+ ")");
					break;

				case '=':
					//using substring to access last element in string
					String result = resultField.getText().substring(resultField.getText().length()-1);
					char error = result.charAt(0);

					if (error == ')' || error == '(' || error == '+' || error == '-' || error == '*' || error == '/'){
						resultField.setText(resultField.getText() + " = syntax error");
					}

					String num1 = firstNumber.toString();
					String num2 = resultField.getText().substring(resultField.getText().lastIndexOf(action)+1);
					//using parser to format inputs
					Parser p = new Parser(num1 + action + num2);
					Expr new1;

					try{
						Expr e1 = p.parse();
						int left = Integer.parseInt(num1);
						int right = Integer.parseInt(num2);

						if (action == "+"){
							new1 = new Plus(new Integer2(left), new Integer2(right));
						}
						else if (action == "-"){
							new1 = new Minus(new Integer2(left), new Integer2(right));
						}
						else if (action == "*"){
							new1 = new Times(new Integer2(left), new Integer2(right));
						}
						else{
							new1 = new Divide(new Integer2(left), new Integer2(right));

						}

						if(mode == true){
							if ((left%right) != 0 && action == "/"){
								resultField.setText(e1.toString()+ j.getText() + "fraction");

							}
							else if (right == 0 && action == "/"){
								resultField.setText(e1.toString()+ j.getText() + "fraction");
							}
							else{
								resultField.setText(e1.toString()+j.getText()+Integer.toString(new1.IntNum()));
							}
						}
						else{
							if (right == 0 && action == "/"){
								resultField.setText(e1.toString()+ j.getText() + " NaN");
							}else{			
								//the formatting to 6 decimals from: http://stackoverflow.com/questions/1451149/rounding-a-double-to-5-decimal-places-in-java-me
								resultField.setText(e1.toString()+j.getText()+String.valueOf(format(US, "%1$.6f", new1.FloatNum())));
							}
						}
					}
					catch(ParseError et){
						System.out.println(et);
					}

					break;

				case 'B':
					resultField.setText(resultField.getText().substring(0, resultField.getText().length()-1));
					break;

					// This case 'falls through'. If +, -, %, / or * are entered, they all execute the same case!
				case '+':
				case '-':
				case '/':
				case '*':

					firstNumber = Integer.parseInt(resultField.getText());
					resultField.setText(resultField.getText()+operator);
					action = operator;
					break;

				default:

				}
			}
		};
	}


	public static void main(String[] args) {
		/* We simple create a new object of CalculatorFrame */
		CalculatorFrame cf = new CalculatorFrame();
	}
}