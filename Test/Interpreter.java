/**
 * Interpreter.java - parses string input representing an infix arithmetic
 *                 expression into tokens, and builds an expression tree.
 *                 The expression can use the operators =, +, -, *, /, %.
 *                 and can contain arbitrarily nested parentheses.
 *                 The = operator is assignment and must be absolutely lowest
 *                 precedence.
 * March 2013
 * rdb
 */
import javax.swing.*;
import java.util.*;
import java.io.*;

public class Interpreter extends JFrame
{
   //----------------------  class variables  ------------------------

   //---------------------- instance variables ----------------------
   private boolean      _printTree = true;  // if true print tree after each
                                           //    expression tree is built 
   ArrayList<String> _Operator;
   SymbolTable _SymbolTable;
   Scanner _Scanner;
   
   //----------- constants
   
   //--------------------------- constructor -----------------------
   /**
    * If there is a command line argument use it as an input file.
    * otherwise invoke an interactive dialog.
    */
   public Interpreter( String[] args ) 
   {      
      if ( args.length > 0 )
         processFile( args[ 0 ] );
      else
         interactive();      
   }
   //--------------------- processFile -----------------------------
   /**
    * Given a String containing a file name, open the file and read it.
    * Each line should represent an expression to be parsed.
    */
   public void processFile( String fileName )
   {
       File _File = new File(fileName);
    
       try 
       {
           _Scanner = new Scanner(_File);
     
      while (_Scanner.hasNextLine()) 
      {
        String scanner = _Scanner.nextLine();
        
        if (scanner.startsWith("#"))
          System.out.println(scanner);
                 
        else if (scanner.startsWith("@")) 
        {
          if (scanner.startsWith("@print")) 
            print(scanner);
                     
          else if (scanner.startsWith("@lookup"))           
            lookUp(scanner);
          
        } 
        
        else 
        {
          System.out.println( processLine(scanner) );
        }
      }
    }    
    
    catch (FileNotFoundException e) 
    {
      e.printStackTrace();
    }
    
  }
  
  private void print(String line) 
  {
    System.out.println(line);
  }
  
  private void lookUp(String line) 
  {
    System.out.println(line);
  }
       
   //--------------------- processLine -----------------------------
   /**
    * Parse each input line -- it shouldn't matter whether it comes from
    * the file or the popup dialog box. It might be convenient to return
    * return something to the caller in the form of a String that can
    * be printed or displayed.
    */
   public String processLine( String line )
   {
      
    String news = "";
    Scanner scn = new Scanner(line);
    String[] operatorar = new String[] { "=", "(", ")", "+", "-", "*", "/",
      "%" };
    List<String> operator = Arrays.asList(operatorar);
    
    String[] alphanumericar = new String[] { "a", "b", "c", "d", "e", "f",
      "g", "h", "i", "j", "k", "l", "m", "o", "p", "q", "r", "s",
      "t", "u", "v", "w", "x", "y", "z", ".", "_", "$" };
    List<String> alphanumeric = Arrays.asList(alphanumericar);
    
    String[] number = new String[] { "0", "1", "2", "3", "4", "5", "6",
      "7", "8", "9" };
    List<String> numbers = Arrays.asList(number);
    
    while (scn.hasNext()) 
    {
      
      String str = scn.next();
      
      if (alphanumeric.contains(str.toLowerCase()) || 
          operator.contains(str) || 
          numbers.contains(str))
      {
        if (operator.contains(str)) 
        {
          news += "<" + str + ">";
          if (scn.hasNext()) 
             news += ",";
        } 
        
        else 
        {
          if (!Character.isJavaIdentifierStart(str.charAt(0)))
          {
            news += "@" + str;
            if ( scn.hasNext() ) 
               news += ",";
            
            
          } 
          
          else if( numbers.contains(str) )
          {
            news += "-Error-";
            System.out.println("In");
            
            if( scn.hasNext())
               news += ",";
            
          }
         
          else
          {
            System.out.println( "get in the else" );
            boolean isBad = false;
            
            for (int i = 1; i < str.length(); i++) 
            {              
                if (Character.isJavaIdentifierPart(str.charAt(i))) 
                {
                    System.out.println( "get in the isJava" );
                    news += "Error";
                    isBad = true;
                    break;
                }
            }
            
            if (!isBad) 
            {
              System.out.println( "get in the if" );
              news += "@" + str;
            }
            
            else 
              System.out.println( "get in the else" );
              news += "Error";
            
            if (scn.hasNext()) 
              news += ",";
            
          }       
        }
      }
    }
    return news;
       
   }
   //--------------------- interactive -----------------------------
   /**
    * Use a file chooser to get a file name interactively, then 
    * go into a loop prompting for expressions to be entered one
    * at a time.
    */
   public void interactive()
   {
      JFileChooser fChooser = new JFileChooser( "." );
      fChooser.setFileFilter( new TextFilter() );
      int choice = fChooser.showDialog( null, "Pick a file of expressions" );
      if ( choice == JFileChooser.APPROVE_OPTION )
      {
         File file = fChooser.getSelectedFile();
         if ( file != null )
            processFile( file.getName() );
      }
      
      String prompt = "Enter an arithmetic expression: ";
      String expr = JOptionPane.showInputDialog( null, prompt );
      while ( expr != null && expr.length() != 0 )
      {
         String result = processLine( expr );
         JOptionPane.showMessageDialog( null, expr + "\n" + result );
         expr = JOptionPane.showInputDialog( null, prompt );
      }
   }

   //+++++++++++++++++++++++++ inner class +++++++++++++++++++++++++++++++
   //---------------------------- TextFilter -----------------------------
   /**
    * This class is used with FileChooser to limit the choice of files
    * to those that end in *.txt
    */
   public class TextFilter extends javax.swing.filechooser.FileFilter
   {
      public boolean accept( File f ) 
      {
         if ( f.isDirectory() || f.getName().matches( ".*txt" ) )
            return true;
         return false;
      }
      public String getDescription()
      {
         return "*.txt files";
      }
   }
   //--------------------- main -----------------------------------------
   public static void main( String[] args )
   {
      Interpreter app = new Interpreter( args );
   }
}