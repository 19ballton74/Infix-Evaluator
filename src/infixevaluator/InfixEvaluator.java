/*
 * Author: Brock A. Allton
 * Date: 19 March, 2016
 * Purpose: To set up a calculator to evaluate infix expressions using stacks
 */

package infixevaluator;

import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Set up a class to catch the unfortunate input attempt of division by zero
 */

class DivideByZeroException extends ArithmeticException{
    
    
    public DivideByZeroException (){
   
    }
    
    public DivideByZeroException(String message){
        super(message);
    }
}//End class DivideByZero Exception 

/**
 * Set up the Stacks that will be used to store operators and operands.
*/

class Stack<E>{
    private ArrayList<E> elements;
    private int numElements;
    private E topElement;
    
    public Stack(){
        elements = new ArrayList<>();
        numElements = 0;
    }
    //Check to see if stacks contains elements, return true if empty
    public boolean isEmpty(){
        if(numElements == 0){
            return true;
        }
        else
            return false;
    }
    //Get size of the elements
    public int getSize(){
        return this.numElements;
    }
    
    //Add new element to top of Stack
    public void push(E item){
        elements.add(item);
        numElements++;
    }
    
    //Remove top element of Stack
    public E pop(){
        if(numElements == 0){
            JOptionPane.showMessageDialog(null, "Stack is Empty!");
            return null;
        }
        else{
            topElement = elements.get(elements.size()-1);
            elements.remove(elements.size()-1);
            numElements--;
            return topElement;
        }
         
    }
    //Will look at the top of the element without removing
    public E peek(){
        if(numElements == 0){
            JOptionPane.showMessageDialog(null, "Stack is Empty!");
            return null;
        }
        else{
            topElement = elements.get(elements.size()-1);
            
        }
        return topElement;
    }
}//End class Stack

/**
 *  This is the class that will perform the evaluation on the infix expression
 * passed from the GUI class
*/

class InfixEvaluation {
   
   private static Stack <String> numStack = new Stack <>();
   private static Stack <String> opStack = new Stack <>();
    
        
    public static int evaluate(String expression)throws DivideByZeroException{
      
       numStack = new Stack <>();
       opStack = new Stack <>();
        int evaluation = 0;
        
        //Remove white spaces
        String expressionRead = expression;
        expressionRead = expressionRead.replaceAll(" ","");
        
        //Tokenize the String that was read in, uses +,-,*, etc as delimiters
        StringTokenizer tokens = new StringTokenizer(expressionRead, 
                "+-*/()", true);
        
        //While there are more tokens to be read, get the next one
        while(tokens.hasMoreTokens()){
            String token = tokens.nextToken();
            
            //If it's an operand, push it onto the numStack, if the token is not
            //an operator (*-/+) then it will assume to be an operand
            if(!token.equals("+")&&!token.equals("-")&&!token.equals("/")
                    &&!token.equals("*")&&!token.equals("(")&&!token.equals(")")){
                numStack.push(token);
            }
            
            //If token is open brace, push it onto the opStack
            else if(token.equals("(")){
                opStack.push(token);
            }
            
            //If it is a close brace, and top of opStack is not an open brace,
            //pop an opStack and 2 numStacks, and do the calculation
            else if(token.equals(")")){
                while(!opStack.peek().equals("(")){//while top of opStack !=')'
                    //first and second numStack holders
                    int a = 0;
                    int b = 0;
                    String operator = "";//hold operator popped from opStack
                    
                    //have to parse a and b into int from String
                    a = Integer.parseInt(numStack.pop());
                    b = Integer.parseInt(numStack.pop());
                    operator = opStack.pop();
                    //perform calculation
                    int calculation = calcExpression(a, b, operator);
                    
                    //push the calculation back onto numStack, and convert
                    //back to String
                    numStack.push(Integer.toString(calculation));
                }//End while
                //Now pop the open brace off the opStack
                opStack.pop();
            }
            //If the token is an operator
            else if(token.equals("+")||token.equals("-")||token.equals("/")
                    ||token.equals("*")){
                //while opStack is not empty and operator at the top of numStack
                //has higher or same precedence than the curent operator
                while(opStack.isEmpty()==false && getPrecedence(opStack.peek(),token)==true){
                    //pop off two from numStack and one from opStack
                    //create holders for first and second number from numStack
                    int a = 0;
                    int b = 0;
                    String operator = "";//hold operator popped from opStack
                    
                    //parse a and b into int from String after popping from numStack
                    //then pop opertor from opStack
                    a = Integer.parseInt(numStack.pop());
                    b = Integer.parseInt(numStack.pop());
                    operator = opStack.pop();
                    //Do calculation and push it onto the numStack after converting
                    //back to String
                    int calculation = calcExpression(a, b, operator);
                    numStack.push(Integer.toString(calculation));        
                }//End while
                opStack.push(token);
            }//end else if           
        }//End while has more tokens
        
        //while opStack is not empty
        while(!opStack.isEmpty()){
            //pop off two operands and one opertor
            //create holders and do conversions as above
            int a = 0;
            int b = 0;
            String operator = "";
            
            a = Integer.parseInt(numStack.pop());
            b = Integer.parseInt(numStack.pop());
            operator = opStack.pop();
            
            int calculation = calcExpression(a, b, operator);
            numStack.push(Integer.toString(calculation));
        }
        evaluation = Integer.parseInt(numStack.pop());
        return evaluation;
           
    }//End public evaluate
    

    
    public static boolean getPrecedence(String topOp, String currentOp){
        if(topOp.equals("*")||topOp.equals("/")){
            return true;
            
        }
        else if(currentOp.equals("+")||currentOp.equals("-")){
            return false;
            
        }
        else if(topOp.equals("+")||topOp.equals("-")&&currentOp.equals("*")
                ||currentOp.equals("/")){
            return false;
            
        }
        return false;
       
    }//End public getPrecedence
    
    public static int calcExpression (int a, int b, String operator)
            throws DivideByZeroException{
        int calcResult = 0;
        if(operator.equals("+")){
            calcResult = b+a;
        }
        else if(operator.equals("-")){
            calcResult = b-a;
        }
        else if(operator.equals("*")){
            calcResult = b*a;
        }
        else if(operator.equals("/")){
            if(a == 0){
                throw new DivideByZeroException ("Cannot Divide By Zero!"
                        + " Tear in Space/Time Resulting!");
            }
            else{
                calcResult = b/a;
            }
           }
        else{
            JOptionPane.showMessageDialog(null, "You Must Input a Valid Operator."
                    + "Examples: '*' ,'+', '-', '/'");
        }
        return calcResult;
    }//End public calcExpression
       
}//End class InfixEvaluation 

/**
 * This is the GUI class that will take the infix expression input from the user
 * and return the results of the evaluation on it
 */
     
public class InfixEvaluator extends JFrame {
    private final JLabel expressionLabel, resultLabel;
    private final JTextField expressionField, resultField;
    private final JButton evaluateButton;
    private static final int WIDTH = 350, HEIGHT = 150;
    
    public InfixEvaluator(){
        
        setTitle ("Inifx Exression Evaluator");
        setSize (WIDTH,HEIGHT);
        setResizable (false);
        
        //Set so window opens in the middle of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = getSize().width;
        int h = getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        setLocation(x,y);
        
         //Close window upon exit
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
         //Set up panel for button, text fields, and combo box to be placed on
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets (0,0,10,5);
        
        expressionLabel = new JLabel ("Enter Inifix Exression");
        constraints.gridx = 0;
        constraints.gridy = 0;
       
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        add (expressionLabel, constraints);
        
        expressionField = new JTextField (40);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.FIRST_LINE_END;
        add (expressionField, constraints);
        
        evaluateButton = new JButton ("Evaluate");
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        add (evaluateButton, constraints);
        evaluateButton.addActionListener (new ButtonListener());
        
        resultLabel = new JLabel ("Result");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LINE_START;
        add (resultLabel, constraints);
        
        resultField = new JTextField (20);
        resultField.setEditable(false);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LINE_END;
        add (resultField, constraints);
                
                
         setVisible(true);
    }//End public InfixEvaluator
    
     private class ButtonListener implements ActionListener{
           @Override
        public void actionPerformed (ActionEvent e){
            
            Object source = e.getSource();
            String expression = expressionField.getText();
            int result;
            if(source == evaluateButton){
                try{
                    result = InfixEvaluation.evaluate(expression);
                    resultField.setText(String.valueOf(result));
                }
                catch(DivideByZeroException e1){
                    JOptionPane.showMessageDialog(null, "Error!\nYou Cannot "
                            + "Divide By Zero!\nRip in Space/Time!");
                    e1.printStackTrace();
                     
                }
            }
        }// End actionPerformed
     }// End ButtonListener
    

    
    public static void main(String[] args) {
        new InfixEvaluator();
    }
    
}// End Class
   
                    