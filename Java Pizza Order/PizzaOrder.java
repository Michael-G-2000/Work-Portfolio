import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PizzaOrder extends JFrame implements ActionListener, DocumentListener, ItemListener, ListSelectionListener {
	
	private JPanel mainPanel, pizzaCreation, baseSauce, toppings, pCount, /* Declarations of JPanels used */
				   price, tPrice, gPrice, orders, pad1, pad2, pad3, pad4;
	private JButton order, clearOrder; /* Declarations of JButtons used */
	private JList pizza, base; /* Declarations of JLists used */
	private JCheckBox pe, sa, ha, ba, ch, on, de; /* Declarations of JPanels used */
	private ButtonGroup sauce; /* Declarations of ButtonGroup used */
	private JRadioButton bbq, tom; /* Declarations of RadioButtons used */
	private JTextField total, grandTotal, count; /* Declarations of JTextField used */
	private JLabel tPriceL, gPriceL, countL; /* Declarations of JLabel used */
	private double overallTotal = 0, totalCost = 0, previousTopping = 0, previousPizza = 0; /* Double variables to hold costs */
	private boolean wasOnPan = false, wasDelivAdded = false; /* booleans to track additional costs */
		
	public PizzaOrder() {
		setTitle("Pizza Order Application"); /* Set the title of the JFrame */
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); /*Make it so closing the frame exits the application*/
		setSize(600, 800); /* Set size to 600x800 pixels */

		order = new JButton("Order"); /* Create a new button */
		order.setToolTipText("Submit the order and get the invoice"); /* Set tool tip text */
		order.setActionCommand("Order"); /* Set the action command so the actionLisenter know what action to perform */
		order.addActionListener(this); /* Add an action listener for the button */
		
		clearOrder = new JButton("Clear Order"); /* Create a new button */
		clearOrder.setToolTipText("Clear all orders"); /* Set tool tip text */
		clearOrder.setActionCommand("Clear"); /* Set the action command so the actionLisenter know what action to perform */
		clearOrder.addActionListener(this);/* Add an action listener for the button */
		
		/* Store the options for the pizza list */
		String pizzas[] = {"Supremo Supreme","Supreme","Chicken","Aussie","Vegie","Hawaiian"};
		pizza = new JList(pizzas); /* Create the pizza list */
		pizza.addListSelectionListener(this); /* Add an action listener for the list */
		
		String bases[] = {"Thin & Crispy","Pan","Cheese Filled Crust"}; /* Store the options for the base list */
		base = new JList(bases); /* Create the base list */
		base.addListSelectionListener(this); /* Add an action listener for the list */
		
		pe = new JCheckBox("Pepperoni"); /* Create a check box for the pepperoni topping */
		pe.addItemListener(this); /* Add an item listener for the check box */
		
		sa = new JCheckBox("Salami"); /* Create a check box for the salami topping */
		sa.addItemListener(this); /* Add an item listener for the check box */
		
		ha = new JCheckBox("Ham"); /* Create a check box for the ham topping */
		ha.addItemListener(this); /* Add an item listener for the check box */
		
		ba = new JCheckBox("Bacon"); /* Create a check box for the bacon topping */
		ba.addItemListener(this); /* Add an item listener for the check box */
		
		ch = new JCheckBox("Chicken"); /* Create a check box for the chicken topping */
		ch.addItemListener(this); /* Add an item listener for the check box */
		
		on = new JCheckBox("Onion"); /* Create a check box for the onion topping */
		on.addItemListener(this); /* Add an item listener for the check box */
		
		de = new JCheckBox("Delivery"); /* Create a check box for delivery */
		de.addItemListener(this); /* Add an item listener for the check box */
		
		bbq = new JRadioButton("Barbeque", true); /* Create a radio button for bbq sauce and set it as selected by default */		
		tom = new JRadioButton("Tomato", false); /* Create a radio button for tomato sauce */
		
		sauce = new ButtonGroup(); /* Create a button group to hold the bbq and tom radio buttons */
		sauce.add(bbq); /* Add the bbq button to the radio group */
		sauce.add(tom); /* Add the tom button to the radio group */
		
		tPriceL = new JLabel("Total:"); /* Create a label for the total text field */
		
		gPriceL = new JLabel("Grand Total:"); /* Create a label for the grand total text field */
		
		countL = new JLabel("Count:"); /* Create a label for the count text field */
		
		count = new JTextField(); /* Create text field to enter the number of pizza a person wants */
		count.setText("1"); /* Set the default number of pizzas to 1 */
		
		/*
		 * The following code for checking if the value entered into the
		 * count JTextField is an integer was found on the website:
		 * https://stackoverflow.com/questions/20541230/allow-only-numbers-in-jtextfield
		 * 
		 * The variable regEx was renamed regEx1 and the patter changed from \\d* to [1-9].
		 * This pattern would check the chracter entered to make sure that it was a number between 1 and 9.
		 * 
		 * The variable regEx2 was added to check every subsequent character to make sure it was a digit.
		 * 
		 * The replace function was changed to include the if (count.getDocument().getLength() < 1) statement
		 * to determine which regEx pattern to check the input against.
		 */
		((AbstractDocument)count.getDocument()).setDocumentFilter(new DocumentFilter(){
	        Pattern regEx1 = Pattern.compile("[1-9]");
	        Pattern regEx2 = Pattern.compile("[0-9]*");
	        
	        @Override
	        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {          
	            if (count.getDocument().getLength() < 1) {
	        	Matcher matcher = regEx1.matcher(text);
	            	if(!matcher.matches()){
	            		JOptionPane.showMessageDialog(mainPanel,"Only non-zero numbers are accepted for the first digit of Count.",
	            				"Error", JOptionPane.ERROR_MESSAGE);
	            		return;
	            	}
	            } else {
	            	Matcher matcher = regEx2.matcher(text);
		            if(!matcher.matches()){
		            	JOptionPane.showMessageDialog(mainPanel,"Only numbers are accepted in Count.","Error",
		            			JOptionPane.ERROR_MESSAGE);
		            	return;
		            } 
	            }
	            super.replace(fb, offset, length, text, attrs);
	        }
	    });
		/*
		 * End code from:
		 * https://stackoverflow.com/questions/20541230/allow-only-numbers-in-jtextfield
		 */
		
		/* Assign an owner field to determine who called the document listener */
		count.getDocument().putProperty("owner", count); 
		count.getDocument().addDocumentListener(this); /* Add a document listener to the field */
		
		total = new JTextField(); /* Create text field to show the total cost of one pizza */
		total.setEditable(false); /* Make it so user's can't edit the field */
		total.setText("00.00"); /* Set the default cost to 00.00 */
		/* Assign an owner field to determine who called the document listener */
		total.getDocument().putProperty("owner", total); 
		total.getDocument().addDocumentListener(this); /* Add a document listener to the field */
		
		grandTotal = new JTextField(7); /* Create text field to show the total cost the pizza times the amount of pizza ordered */
		grandTotal.setEditable(false); /* Make it so user's can't edit the field */
		grandTotal.setText("00.00");  /* Set the default cost to 00.00 */
		
		mainPanel = new JPanel(); /* Create a JPanel to hold all content */
		/* Set the components placed inside to stack vertically */
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		GridLayout options = new GridLayout(2,5,2,35); /* Create a new grid layout with 2 rows and 5 columns*/
		pad1 = new JPanel(); /* Create a JPanel to pad the sides of the content all content */
		pad2 = new JPanel(); /* Create a JPanel to pad the sides of the content all content */
		pad3 = new JPanel(); /* Create a JPanel to pad the sides of the content all content */
		pad4 = new JPanel(); /* Create a JPanel to pad the sides of the content all content */
		pizzaCreation = new JPanel(); /* Create a JPanel hold all ordering content */
		pizzaCreation.setLayout(options); /* Apply the grid layout to the JPanel */
		
		baseSauce = new JPanel(); /* Create a JPanel to hold the base list and sauce button group*/
		/* Set the components placed inside to stack vertically */
		baseSauce.setLayout(new BoxLayout(baseSauce, BoxLayout.Y_AXIS));
		baseSauce.add(base); /* Add the base selection list the JPanel */
		baseSauce.add(bbq); /* Add the bbq button the JPanel */
		baseSauce.add(tom); /* Add the tom button the JPanel */
		
		bbq.setAlignmentX(LEFT_ALIGNMENT); /* Set the alignment of the component to the left */
		tom.setAlignmentX(LEFT_ALIGNMENT); /* Set the alignment of the component to the left */
		base.setAlignmentX(LEFT_ALIGNMENT); /* Set the alignment of the component to the left */
		
		toppings = new JPanel(); /* Create a new JPanel to hold the topping check boxes */
		/* Set the components placed inside to stack vertically */
		toppings.setLayout(new BoxLayout(toppings, BoxLayout.Y_AXIS));
		/* Add the check boxes to the JPanel */
		toppings.add(pe); 
		toppings.add(sa);
		toppings.add(ha);
		toppings.add(ba);
		toppings.add(ch);
		toppings.add(on);
		
		/* Construct the first row of the grid layout */
		pizzaCreation.add(pad1);
		pizzaCreation.add(pizza);
		pizzaCreation.add(baseSauce);
		pizzaCreation.add(toppings);
		pizzaCreation.add(pad2);
		
		pCount = new JPanel(); /* Create a new JPanel to hold the label and text field for the number of pizzas ordered */
		/* Set the components placed inside to line up horizontally */
		pCount.setLayout(new BoxLayout(pCount, BoxLayout.X_AXIS));
		pCount.add(countL); /* Add the count label to the JPanel */
		pCount.add(count);	/* Add the count text field to the JPanel */
		
		price = new JPanel(); /* Create a new JPanel to hold the total values for the order */
		/* Set the components placed inside to stack vertically */
		price.setLayout(new BoxLayout(price, BoxLayout.Y_AXIS));
		
		tPrice = new JPanel(); /* Create a new JPanel to hold the label and text field for total for one pizza */
		/* Set the components placed inside to line up horizontally */
		tPrice.setLayout(new BoxLayout(tPrice, BoxLayout.X_AXIS));
		tPrice.add(tPriceL); /* Add the total label to the JPanel */
		tPrice.add(total); /* Add the total text field to the JPanel */
		
		gPrice = new JPanel(); /* Create a new JPanel to hold the label and text field for grand total for all pizzas */
		/* Set the components placed inside to line up horizontally */
		gPrice.setLayout(new BoxLayout(gPrice, BoxLayout.X_AXIS));
		gPrice.add(gPriceL); /* Add the grand total label to the JPanel */
		gPrice.add(grandTotal); /* Add the grand total text field to the JPanel */
		
		price.add(tPrice); /* Add the total JPanel to the price JPanel */
		price.add(gPrice); /* Add the grand total JPanel to the price JPanel  */
		
		/* Construct the second row of the grid layout */
		pizzaCreation.add(pad3);
		pizzaCreation.add(pCount);
		pizzaCreation.add(de);
		pizzaCreation.add(price);
		pizzaCreation.add(pad4);
		
		orders = new JPanel(); /* Create a new JPanel to hold the order and clear order buttons */
		orders.add(order); /* Add the order button to the JPanel */
		orders.add(clearOrder); /* Add the clear order button to the JPanel */
		
		FlowLayout flow = new FlowLayout(); /* Create a new flow layout for the orders JPanel */		
		flow.setHgap(40); /* Set the horizontal gap to 40 pixels*/
				
		orders.setLayout(flow); /* Apply the flow layout to the buttons*/
		
		mainPanel.add(Box.createRigidArea(new Dimension(0, 100))); /* Insert padding at the top of the JPanel */
		mainPanel.add(pizzaCreation); /* Insert the pizza creation options to the JPanel */
		mainPanel.add(Box.createRigidArea(new Dimension(0, 250))); /* Insert padding between the pizza creation and buttons */
		mainPanel.add(BorderLayout.SOUTH, orders); /* Insert the orders JPanel on the south edge of the JPanel */
		
		add(mainPanel); /* Add the mainPanel to the JFrame*/
		paintBackground(); /* Paint the background of all components */
		setVisible(true); /* Set the JFrame to be visible */
	}
	
	/* private void paintBackground()
	 * 
	 * = Description
	 * Paint the background of all components 
	 * 
	 * = Inputs
	 * No inputs taken.
	 * 
	 * = Outputs
	 * Background of all components set.
	 */
	private void paintBackground() {
		Color bgColour = new Color(245, 245, 230); /* Define colour for background */
		
		/* Set the background colour for the following components */
		mainPanel.setBackground(bgColour);
		pizzaCreation.setBackground(bgColour);
		orders.setBackground(bgColour);
		baseSauce.setBackground(bgColour);
		toppings.setBackground(bgColour);
		pCount.setBackground(bgColour);
		bbq.setBackground(bgColour);
		tom.setBackground(bgColour);
		pe.setBackground(bgColour);
		sa.setBackground(bgColour);
		ha.setBackground(bgColour);
		ba.setBackground(bgColour);
		ch.setBackground(bgColour);
		on.setBackground(bgColour);
		de.setBackground(bgColour);
		tPrice.setBackground(bgColour);
		gPrice.setBackground(bgColour);
		pad1.setBackground(bgColour);
		pad2.setBackground(bgColour);
		pad3.setBackground(bgColour);
		pad4.setBackground(bgColour);
	}
	
	public void paint(Graphics g) {
		/* Text of headings and instructions to be displayed */
		String title = "Mario's Pizzeria";
		String heading1 = "Pizza";
		String heading2 = "Base";
		String heading3 = "Toppings";
		String heading4 = "How many?";
		String heading5 = "Delivery?";
		String heading6 = "Totals";
		String heading7 = "Instructions";
		String instruct1 = "-Pan base costs $1.50 extra";
		String instruct2 = "-Each extra topping costs $1.10";
		String instruct3 = "-Delivery costs $5.00 for orders less than $26.00";
		
		super.paint(g);
		
		/* Load and display the pizza image as well as the corner leaf images*/
		Image image=new ImageIcon("Images/Pizza2.jpg").getImage();
		g.drawImage(image,225,575,150,150, this);
		
		image=new ImageIcon("Images/vinesBL.png").getImage();
		g.drawImage(image,8,596,200,200, this);
		
		image=new ImageIcon("Images/vinesBR.png").getImage();
		g.drawImage(image,392,596,200,200, this);
		
		image=new ImageIcon("Images/vinesTL.png").getImage();
		g.drawImage(image,8,30,200,200, this);
		
		image=new ImageIcon("Images/vinesTR.png").getImage();
		g.drawImage(image,392,30,200,200, this);
		
		/* Set the colour to a light red and paint three rectangles */
		g.setColor(new Color(255, 176, 176));
		g.fillRect(127, 105, 110, 23);
		g.fillRect(244, 105, 110, 23);
		g.fillRect(361, 105, 110, 23);
		
		/* Set the colour to a light green and paint three rectangles */
		g.setColor(new Color(180, 255, 166));
		g.fillRect(127, 294, 110, 23);
		g.fillRect(244, 294, 110, 23);
		g.fillRect(361, 294, 110, 23);
		
		/* Set the colour to white and paint a rectangle */
		g.setColor(Color.white);
		g.fillRect(127, 484, 348, 23);
		
		g.setColor(Color.black); /* Set the colour to black */ 
		g.setFont(new Font("Lucida Fax", Font.BOLD, 20)); /* Set the font and size of the font */
		int stringLength = g.getFontMetrics().stringWidth(title); /* Get the size of the title text*/
		g.drawString(title, ((600-stringLength)/2), 60); /* Display the title in the centre of the JFrame */
		
		g.setFont(new Font("Lucida Sans", Font.BOLD, 18)); /* Change the font size */
		/* Display the different headings on the JFrame */
		g.drawString(heading1, 156, 123);
		g.drawString(heading2, 275, 123);
		g.drawString(heading3, 373, 123);
		g.drawString(heading4, 127, 312);
		g.drawString(heading5, 260, 312);
		g.drawString(heading6, 385, 312);
		g.drawString(heading7, 245, 502);
		
		g.setFont(new Font("Lucida Sans", Font.BOLD, 12)); /* Change the font size */
		/* Display the ordering instructions on the JFrame */
		g.drawString(instruct1, 130, 525);
		g.drawString(instruct2, 130, 540);
		g.drawString(instruct3, 130, 555);
	}
	
	/* Main Function */
	public static void main(String[] args) {
		new PizzaOrder(); /* Create a new Pizza ordering JFrame */
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String src = e.getActionCommand(); /* Get the action command associated with the button that called the action */
		
		/* If the order button was pressed display the invoice otherwise clear the selections */
		if (src.equals("Order")) {
			printInvoice();
		} else if (src.equals("Clear")) {
			clearSelections();
		}
	}
	
	/* private void clearSelections()
	 * 
	 * = Description
	 * Reset the options to the default settings 
	 * 
	 * = Inputs
	 * No inputs taken.
	 * 
	 * = Outputs
	 * All selections made by the user will be erased
	 */
	private void clearSelections() {
		pizza.clearSelection(); /* Clear the selection made in the pizza list */
		previousPizza = 0.0; /* Clear the variable tracking the previous pizza selection */
		
		base.clearSelection(); /* Clear the selection made in the base type list */
		wasOnPan = false; /* Reset the base selection tracker */
		bbq.setSelected(true); /* Set the bbq radio button to selected */
		
		/* Deselect all the toppings check boxes*/
		pe.setSelected(false);
		sa.setSelected(false);
		ha.setSelected(false);
		ba.setSelected(false);
		ch.setSelected(false);
		on.setSelected(false);
		de.setSelected(false);
		previousTopping = 0.0; /* Clear the variable tracking previous topping costs*/
		
		count.setText("1"); /* Set the number of pizza's ordered back to 1 */
		total.setText("00.00"); /* Reset the total field */
		grandTotal.setText("00.00"); /* Reset the grand total field */
	}
	
	/* private void printInvoice()
	 * 
	 * = Description
	 * Print the invoice for the current selected options 
	 * 
	 * = Inputs
	 * No inputs taken.
	 * 
	 * = Outputs
	 * A JOptionPane will display an invoice
	 */
	private void printInvoice() {
		String pizzaType, baseType, sauceType, delivery; /* Variables to hold string representations of selections */
		
		/* If there is no pizza or base selected show an error and return */
		if (pizza.getSelectedIndex() < 0 || base.getSelectedIndex() < 0) {
			JOptionPane.showMessageDialog(mainPanel,"Please select a pizza and base.","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		/* Get the selected values of the pizza and base list */
		pizzaType = (String) pizza.getSelectedValue();
		baseType = (String) base.getSelectedValue();
		
		/* Set the sauceType variable to the select sauce */
		if (bbq.isSelected()) {
			sauceType = "Barbeque";
		} else {
			sauceType = "Tomatoe";
		}
		
		/* Store if delivery was requested or not */
		if (de.isSelected()) {
			delivery = "Yes";
		} else {
			delivery = "No";
		}
		
		/* Create a JOptionPane showing all the options selected by the user */
		JOptionPane.showMessageDialog(mainPanel, 
				"------------- Invoice -------------\n" +
				"Pizza: " + pizzaType + "\n" +
				"Base: " + baseType + "\n" +
				"Sauce: " + sauceType + "\n" +
				"Toppings:\n" + getToppingChoices() + "\n" +
				"Pizza price: " + total.getText() + "\n\n" +
				"Number of Pizzas: " + count.getText() + "\n" +
				"Delivery: " + delivery + "\n" +
				"Grand Total: " + grandTotal.getText() + "\n\n" +
				"------------------------------------\n"
				, "Invoice", JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	/* private String getToppingChoices()
	 * 
	 * = Description
	 * Return a string with all the select toppings contained in it. 
	 * 
	 * = Inputs
	 * No inputs taken.
	 * 
	 * = Outputs
	 * Returns a string with the selected toppings recorded.
	 */
	private String getToppingChoices() {
		String toppings = ""; /* Create the string variable to be returned*/
		
		/*
		 * For each topping, if it is selected concatenate the topping name and a end of line character with
		 * what is already stored in the variable. 
		 */
		if (pe.isSelected())
			toppings = toppings + "   Peperoni\n";
		
		if (sa.isSelected())
			toppings = toppings + "   Salami\n";
		
		if (ha.isSelected())
			toppings = toppings + "   Ham\n";
		
		if (ba.isSelected())
			toppings = toppings + "   Bacon\n";
		
		if (ch.isSelected())
			toppings = toppings + "   Chicken\n";
		
		if (on.isSelected())
			toppings = toppings + "   Onion\n";
		
		return toppings; /* Return the string */
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		/* If the mouse button hasn't been released do nothing */
		if (e.getValueIsAdjusting())
			return;
		
		Object src = e.getSource(); /* Store who triggered the event */
		String value = ""; /* A variable to store tha value selected */
		
		if (src.equals(pizza)) {
			/* If the selection was cleared do nothing */
			if (pizza.getSelectedIndex() < 0)
				return;
			
			adjustPizzaCost(value); /* Update the total text field with new cost */
		
		} else if (src.equals(base)) {
			if (base.getSelectedIndex() < 0)
				return;
			
			adjustBaseCost(value); /* Update the total text field with new cost */
		}
	}
	
	/* private void adjustPizzaCost(String value)
	 * 
	 * = Description
	 * Update the total cost with the new cost of the new pizza selection 
	 * 
	 * = Inputs
	 * Once input taken:
	 * $ A string to store which pizza was selected.
	 * 
	 * = Outputs
	 * Sets the total text field to the new value using the new selection.
	 */
	private void adjustPizzaCost(String value) {
		value = (String)pizza.getSelectedValue(); /* Get the selection made as a string */
		double pizzaCost = 0; /* A variable to hold the price of the selected pizza */
		
		/* Assign the cost of the pizza selected */
		switch (value) {
			case "Supremo Supreme":
				pizzaCost = 13.00;
				break;
			
			case "Supreme":
				pizzaCost = 12.00;
				break;
			
			case "Chicken":
				pizzaCost = 11.50;
				break;
			
			case "Aussie":
				pizzaCost = 12.50;
				break;
			
			case "Vegie":
				pizzaCost = 10.50;
				break;
			
			case "Hawaiian":
				pizzaCost = 11.00;
				break;
		}
		
		overallTotal = Double.parseDouble(total.getText()); /* Get the total currently displayed on screen */
		
		/* 
		 * If a selection was made before this one subtract the cost of the previously 
		 * selected pizza from the current cost
		 */
		if (previousPizza != 0)
			overallTotal -= previousPizza;
		
		overallTotal = overallTotal + pizzaCost; /* Add the cost of the new selection to the total */
		previousPizza = pizzaCost; /* Store the cost of the new selection in the previousPizza variable */
		
		total.setText(String.format("%.2f", overallTotal)); /* Display the new cost formatted to 2 decimal places*/
	}
	
	/* private String getToppingChoices()
	 * 
	 * = Description
	 * Update the total cost with the new cost of the new base selection 
	 * 
	 * = Inputs
	 * Once input taken:
	 * $ A string to store which base was selected.
	 * 
	 * = Outputs
	 * Sets the total text field to the new value using the new selection.
	 */
	private void adjustBaseCost(String value) {
		value = (String)base.getSelectedValue(); /* Get the value selected */
		
		/* 
		 * If the selected option was Pan add $1.50 to the total.  Otherwise if changing from Pan subtract
		 * $1.50 from the total
		 */
		if (value.equals("Pan")) {
			overallTotal = Double.parseDouble(total.getText());
			overallTotal += 1.50;
			wasOnPan = true;
			
			total.setText(String.format("%.2f", overallTotal));
		} else if (wasOnPan) {
			overallTotal = Double.parseDouble(total.getText());
			overallTotal -= 1.50;
			wasOnPan = false;
			
			total.setText(String.format("%.2f", overallTotal));
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object src = e.getSource(); /* Get the source of the event*/
		
		if (src.equals(de)) {
			totalCost = Double.parseDouble(grandTotal.getText()); /* Get the current grand total */
			
			/* 
			 * If the delivery option was selected and the cost is below or equal to $26.00 add a $5.00 delivery fee.
			 * Otherwise if the option was deselected and a fee was applied remove the fee from the total
			 */
			if (de.isSelected()) {
				if(totalCost <= 26.00) {
					totalCost += 5.0;
					wasDelivAdded = true;
				}
			} else if (wasDelivAdded){
				totalCost -= 5.0;
				wasDelivAdded = false;
			}
			
			grandTotal.setText(String.format("%.2f", totalCost)); /* Update the grand total text field */
		} else {
			int numSelected = 0;
			double toppingTotal = 0;
			overallTotal = Double.parseDouble(total.getText()); /* Get the current pizza total */
			
			/* 
			 * Since each topping costs $1.10 extra we can count the number of check boxes 
			 * selected and multiply it by 1.10 to get the total topping cost.
			 * 
			 * The below if statements count the number of toppings selected.
			 */
			if (pe.isSelected())
				numSelected++;
			
			if (sa.isSelected())
				numSelected++;
			
			if (ha.isSelected()) 
				numSelected++;
			
			if (ba.isSelected()) 
				numSelected++;
			
			if (ch.isSelected()) 
				numSelected++;
			
			if (on.isSelected())
				numSelected++;
			
			/* If a toppings value was calculated previously take that value away from the total */
			if (previousTopping != 0)
				overallTotal -= previousTopping;
			
			toppingTotal = numSelected * 1.10; /* Calculate the total for the toppings selected */
			overallTotal += toppingTotal; /* Calculate the new total */
			previousTopping = toppingTotal; /* Store the new total for changes in the future */
			
			total.setText(String.format("%.2f", overallTotal)); /* Display the new pizza total */
		}
	}

	@Override
	public void insertUpdate(DocumentEvent event) {
		updateTotals();
	}

	@Override
	public void removeUpdate(DocumentEvent event) {
		Object owner = event.getDocument().getProperty("owner"); /* Get the id of the text field that caused the event */
		
		/* 
		 * If the count text field caused the event and is not empty then update the grand total 
		 * text field accordingly otherwise do nothing.
		 */
		if(!count.getText().equals("") && owner == count)
			updateTotals();
	}
	
	@Override
	public void changedUpdate(DocumentEvent event) {
	}
	
	/* private void updateTotals()
	 * 
	 * = Description
	 * Update the grand total based on the new total or count text fields 
	 * 
	 * = Inputs
	 * No inputs taken.
	 * 
	 * = Outputs
	 * Sets the grand total text field to the new value.
	 */
	private void updateTotals() {
		/* Calculate the new grand total */
		totalCost = (Double.parseDouble(count.getText())*Double.parseDouble(total.getText()));
		
		/* 
		 * If the new total is more than $26.00 and a delivery fee was added previously, remove the fee.
		 * Otherwise if the new cost is less than or equal to $26.00 and the delivery fee was added, previously
		 * re-add the fee
		 */
		if (totalCost > 26.00 && de.isSelected()) {
			wasDelivAdded = false;
		} else if (totalCost <= 26.00 && de.isSelected()) {
			totalCost += 5.00;
			wasDelivAdded = true;
		}
		
		grandTotal.setText(String.format("%.2f", totalCost)); /* Display the new grand total */
	}
}
