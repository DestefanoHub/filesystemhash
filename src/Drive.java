import java.util.Scanner;
import java.io.*;
import java.security.*;

/**
 * Driver asks the user for the location of the directory of the files
 * that need to be HASHED using SHA-256. It then generates the directory
 * in which these HASH files will be located. The Hash values are then 
 * generated and stored in files in the newly created directory. 
 *  
 * @author Victoria Johnson				Date: November 19, 2015
 * 
 */
public class Driver{

	public static void main(String[] args) throws Exception{
		
		//Variables
		String dir1 = "C://Users/johnson/Documents/Projects/Eclipse/FileSystemHash/directory1";
		File directory1 = new File(dir1);
		Scanner scan = new Scanner(System.in);
		String dir2 = "C://Users/johnson/Documents/Projects/Eclipse/FileSystemHash/directory2";
		String loc = "directory2/";
		String HASHvalue = "HexString";
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		
		byte[] dataBytes = new byte[1024];
		
		
		//Ask User for Directory to be Hashed
		System.out.println("Enter name of directory1: ");
		dir1 = scan.nextLine();
	
		//Put Files in Director in Array
		File[] listFile = directory1.listFiles();
		
		
		//Call create directory
		createDir2(dir2);
		
		
		//Generate HASH Values for each File and Place that in a new HASH.txt
		//	in the directory2.
		for(int i = 0; i < listFile.length; i++)
		{
			System.out.println(listFile[i].getName());
			FileInputStream fis = new FileInputStream(listFile[i]);
			
			//Generate Hash value
			int nread = 0;
			while((nread = fis.read(dataBytes)) != -1)
			{
				md.update(dataBytes, 0, nread);
			}
			byte[] mdbytes = md.digest();
			
			//convert the byte to hex format method 1
			StringBuffer hexString = new StringBuffer();
			for(int j =0; j <mdbytes.length; j++)
			{
				hexString.append(Integer.toHexString(0xFF & mdbytes[j]));
			}
			
			HASHvalue = hexString.toString();
			
			//Call create file
			createFile((loc +"HASH" + i + ".txt"), HASHvalue);
		}

	}
	
	//Create second directory
	public static void createDir2(String dir)
	{
		//Variable
		boolean dirFlag = false;
		
		File directory2 = new File(dir);
		
		try{
			dirFlag = directory2.mkdir();
		} catch (SecurityException Se) {
			System.out.println("Error while creating directory2 in Java: " + Se);
		}
		
		if(dirFlag)
		{
			System.out.println("Directory2 created successfully");
		}
		else
		{
			System.out.println("Directory2 was not created successfully");
		}
	}
	
	//Create a file for the HASH value
	public static void createFile(String loc, String content)
	{
		try{
			File file = new File(loc);
			
			if(file.createNewFile())
			{
				System.out.println("File is Created!");
			}
			else
			{
				System.out.println("File already exists.");
			}
			
			//Write Content to file
			FileWriter writer = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(writer);
			
			bw.write(content);
			bw.close();
			
			System.out.println("Done");
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
