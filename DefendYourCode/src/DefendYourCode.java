import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DefendYourCode 
{

	public static void main(String[] args) 
	{
		Scanner in = new Scanner(System.in);
		
		String fname = null;
		String lname = null;
		
		fname = getName("first", in);
		lname = getName("last", in);
		
		boolean safeadd = false, safemult = false;
		
		int intOne = 0;
		int intTwo = 0;
		while(!(safeadd && safemult))
		{
			intOne = getInts("a", in);
			intTwo = getInts("another", in);
			
			safeadd = checkAddition(intOne, intTwo);
			safemult = checkMultiplication(intOne, intTwo);
		}
		
		File inputFile = getInFile(in);
		
		File out = new File (makeOutFile()); //No reason to let user specify output file.
		
		//TODO Get password here.
		
		writeToOut(out, fname, lname, intOne, intTwo, inputFile);
		
		in.close();
	}
	

	private static boolean checkMultiplication(int intOne, int intTwo) {
		// TODO Auto-generated method stub
		return false;
	}


	private static boolean checkAddition(int intOne, int intTwo) 
	{
		if(intOne > 0 && intTwo > 0 && intOne+intTwo > 0)
			return true;
		else if(intOne < 0 && intTwo < 0 && intOne+intTwo < 0)
			return true;
		else
			return false;
	}


	private static File getInFile(Scanner in) 
	{
		while(true)
		{
			System.out.print("Enter an input file in the working directory: ");
			try
			{
				String filename = in.nextLine();
				File file = new File(filename);
				
				if(filename.contains("\\"))
				{
					System.out.println("Invalid file name. Do not give a path, Just a file name in the working directory.");
				}
				else if(file.exists() && file.isFile())
					return file;
				else
					System.out.println("Invalid input file.");
			}
			catch(NullPointerException e)
			{
				System.out.println("Invalid input file.");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}


	private static void writeToOut(File out, String fname, String lname, int intOne, int intTwo, File inputFile) 
	{
		try
		{
			int add = intOne + intTwo;
			int multiply = intOne * intTwo; 
			// Check values before and after.
			
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
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}


	private static String makeOutFile() 
	{
		Writer writer = null;

		try 
		{
		    writer = new PrintWriter("output.txt", "utf-8");
		} 
		catch (IOException e)
		{
			System.out.println("Could not create output.txt");
		} 
		finally 
		{
			try 
			{
				writer.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return "output.txt";
	}


	private static int getInts(String s, Scanner in) 
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
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				in.nextLine();
			}
		}
	}


	private static String getName(String s, Scanner in) 
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
			catch(Exception e) //See if we can find any errors
			{
				e.printStackTrace();
			}
			
		}
	}

}
