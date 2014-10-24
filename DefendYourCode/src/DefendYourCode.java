// The Incredible Inchworms
// Tyler Herrin, Chris Purta, Casey Schadewitz

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

public class DefendYourCode 
{

	public static void main(String[] args) 
	{
		Scanner in = new Scanner(System.in);
		
		FileWriter fw = null;
		BufferedWriter log = null;
		try 
		{
			fw = new FileWriter("log.txt");
			log = new BufferedWriter(fw);
		} 
		catch (IOException e) 
		{
			System.out.println("Could not create log file.");
		}
		
		String fname = null;
		String lname = null;
		
		fname = getName("first", in, log);
		lname = getName("last", in, log);
		
		boolean safeadd = false, safemult = false;
		
		int intOne = 0;
		int intTwo = 0;
		while(!(safeadd && safemult)) //If either safeadd or safemult are false keep looping.s
		{
			intOne = getInts("an", in, log);
			intTwo = getInts("another", in, log);
			
			safeadd = checkAddition(intOne, intTwo);
			safemult = checkMultiplication(intOne, intTwo);
			
			if((!safemult) || (!safeadd))
				System.out.println("Invalid integers entered.");
		}
		
		File inputFile = getInFile(in, log);
		
		File out = new File (makeOutFile(log)); //No reason to let user specify output file.
		
		passwordCheck(in, log);
		
		writeToOut(out, fname, lname, intOne, intTwo, inputFile, log);
		
		in.close();
	}
	

	private static void passwordCheck(Scanner in, BufferedWriter log) 
	{
		System.out.print("Enter a password: ");
		
		try 
		{
			String pass = in.nextLine();
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashOne = digest.digest(pass.getBytes("UTF-8"));
			
			System.out.print("Enter the password again: ");
			String auth = in.nextLine();
			
			byte[] hashTwo = digest.digest(auth.getBytes("UTF-8"));
			
			if(Arrays.equals(hashOne, hashTwo))
				System.out.println("Password accepted.");
			else
				System.out.println("Passwords did not match.");
		} 
		catch (NoSuchAlgorithmException e) 
		{
			System.out.println("Encryption error occured.");
			try 
			{
				log.write("Encryption error occured.");
				log.newLine();
			}
			catch(IOException f)
			{
				System.out.println("Could not write error to log file.");
			}
		}
		catch (UnsupportedEncodingException e) 
		{
			System.out.println("Encoding error occured.");
			try 
			{
				log.write("Encoding error occured.");
				log.newLine();
			}
			catch(IOException f)
			{
				System.out.println("Could not write error to log file.");
			}
		}
	}


	private static boolean checkMultiplication(int intOne, int intTwo) 
	{
		int smaller, larger;
		
		if(intOne == 0 || intTwo == 0)
			return true;
		
		if(Math.abs(intOne) < Math.abs(intTwo))
		{
			smaller = Math.abs(intOne);
			larger = Math.abs(intTwo);
		}
		else
		{	
			smaller = Math.abs(intTwo);	
			larger = Math.abs(intOne);
		}
		
		/* If the max integer size divided by the smaller integer is greater than the 
		 * larger integer divided by the smaller integer then larger multiplied by smaller
		 * must be less than the max int size.
		 */
		return ((Integer.MAX_VALUE / larger) > smaller);
			
	}


	private static boolean checkAddition(int intOne, int intTwo) 
	{
		if(intOne == 0 || intTwo == 0)
			return true;
		else if(intOne > 0 && intTwo > 0 && intOne+intTwo > 0)
			return true;
		else if(intOne < 0 && intTwo < 0 && intOne+intTwo < 0)
			return true;
		else
			return false;
	}


	private static File getInFile(Scanner in, BufferedWriter log) 
	{
		while(true)
		{
			System.out.print("Enter an input file in the working directory: ");
			try
			{
				String filename = in.nextLine();
				File file = new File(filename);
				
				if(!filename.matches("^[a-zA-Z0-9][\\w]*\\.?[\\w]*[a-zA-Z0-9]+$"))
				{
					System.out.println("Invalid file name. Make sure the file is in the working directory.");
				}
				else if(file.exists() && file.isFile())
					return file;
				else
					System.out.println("File does not exist.");
			}
			catch(NullPointerException e)
			{
				System.out.println("Null pathname error occurred.");
				try 
				{
					log.write("Null pathname error occured.");
					log.newLine();
				}
				catch(IOException f)
				{
					System.out.println("Could not write error to log file.");
				}
			}
		}
	}


	private static void writeToOut(File out, String fname, String lname, int intOne, int intTwo, File inputFile, BufferedWriter log) 
	{
		try
		{
			int add = intOne + intTwo;
			int multiply = intOne * intTwo; 
			
			FileWriter fw = new FileWriter(out.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(fname + " " + lname);
			bw.newLine();
			
			bw.write("Addition: " + add);
			bw.newLine();
			
			bw.write("Multiplication: " + multiply);
			bw.newLine();
			bw.newLine();
			
			//Write input file to output file
			FileReader fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);
			
			for(String line = br.readLine(); line != null; line = br.readLine())
			{
				bw.write(line);
				bw.newLine();
			}
			
			br.close();
			bw.close();
			System.out.println("Done.");
			
		}
		catch(IOException e)
		{
			System.out.println("Could not write to output.txt");
			try 
			{
				log.write("Could not write to output.txt.");
				log.newLine();
			}
			catch(IOException f)
			{
				System.out.println("Could not write error to log file.");
			}
		}
	}


	private static String makeOutFile(BufferedWriter log) 
	{
		Writer writer = null;

		try 
		{
		    writer = new PrintWriter("output.txt", "utf-8");
		} 
		catch (IOException e)
		{
			System.out.println("Could not create output.txt");
			try 
			{
				log.write("Could not create output.txt");
				log.newLine();
			}
			catch(IOException f)
			{
				System.out.println("Could not write error to log file.");
			}
		} 
		finally 
		{
			try 
			{
				writer.close();
			} 
			catch (IOException e) 
			{
				System.out.println("Could not close output file.");
				try 
				{
					log.write("Could not close output file.");
					log.newLine();
				}
				catch(IOException f)
				{
					System.out.println("Could not write error to log file.");
				}
			}
		}
		return "output.txt";
	}


	private static int getInts(String s, Scanner in, BufferedWriter log) 
	{
		while(true)
		{
			System.out.printf("Enter %s integer: ", s);
			try
			{
				int i = in.nextInt();
				return i;
			}
			catch(InputMismatchException e)
			{
				System.out.println("Invalid integer entry.");
				try 
				{
					log.write("Invalid Integer entry.");
					log.newLine();
				}
				catch(IOException f)
				{
					System.out.println("Could not write error to log file.");
				}
			}
			catch(NoSuchElementException e)
			{
				System.out.println("Input error.");
				try 
				{
					log.write("Input exausted in getInts.");
					log.newLine();
				}
				catch(IOException f)
				{
					System.out.println("Could not write error to log file.");
				}
			}
			catch(IllegalStateException e)
			{
				System.out.println("Scanner error occured.");
				try 
				{
					log.write("Scanner input was exausted somehow in getInts.");
					log.newLine();
				}
				catch(IOException f)
				{
					System.out.println("Could not write error to log file.");
				}
			}
			finally
			{
				in.nextLine();
			}
		}
	}

	private static String getName(String s, Scanner in, BufferedWriter log) 
	{
		while(true)
		{
			System.out.printf("Enter your %s name: ", s);
			
			try
			{
				String name = in.nextLine();
				name.replaceAll("\\r|\\n", "");
				
				if(name.matches("[a-zA-z]{1,50}+"))
				{
					return name;
				}
				else
				{
					System.out.println("Invalid name entry.");
				}
			}
			catch(PatternSyntaxException e)
			{
				System.out.println("Regex pattern error occured.");
				try 
				{
					log.write("Regex pattern was changed somehow in getName");
					log.newLine();
				}
				catch(IOException f)
				{
					System.out.println("Could not write error to log file.");
				}
			}
			catch(NoSuchElementException e)
			{
				System.out.println("No line was found.");
				try 
				{
					log.write("No line found in getName.");
					log.newLine();
				}
				catch(IOException f)
				{
					System.out.println("Could not write error to log file.");
				}
			}
			catch(IllegalStateException e)
			{
				System.out.println("Scanner error has occured.");
				try 
				{
					log.write("Scanner has been closed somehow.");
					log.newLine();
				}
				catch(IOException f)
				{
					System.out.println("Could not write error to log file.");
				}
			}
			
		}
	}

}
