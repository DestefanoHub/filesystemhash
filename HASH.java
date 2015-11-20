import java.io.*;
import java.security.MessageDigest;
import java.util.*;


/**
 * HASH takes in two input arguments, directory1 and directory2
 * It then generates the HASH values for the directory1 and compares
 * them to directory2. 
 *  
 * @author Victoria Johnson				Date: November 19, 2015
 * 
 */
public class HASH {

	public static void main(String[] args) throws Exception
	{
		//Variables
		String dir1 = "C://Users/johnson/Documents/Projects/Eclipse/FileSystemHash/directory1";
		File directory1 = new File(dir1);
		String dir2 = "C://Users/johnson/Documents/Projects/Eclipse/FileSystemHash/directory2";
		File directory2 = new File(dir2);
		String HASHvalue = "HexString";
		String savedHASH = "";
		
		Scanner scan = new Scanner(System.in);
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		
		byte[] dataBytes = new byte[1024];
		
		//Put Files in Director in Array
		File[] dir1ListFile = directory1.listFiles();
		File[] dir2ListFile = directory2.listFiles();
		
		//Generate HASHS for Directory1 again
		for(int i = 0; i < dir1ListFile.length; i++)
		{
			System.out.println(dir1ListFile[i].getName());
			FileInputStream fis = new FileInputStream(dir1ListFile[i]);
			FileInputStream fis2 = new FileInputStream(dir2ListFile[i]);
			
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
			
			//Read Saved HASH value
			BufferedReader br = new BufferedReader(new InputStreamReader(fis2));
			
			savedHASH = br.readLine();
			
			System.out.println("Saved HASH Value: " + savedHASH);
			System.out.println("New HASH Value: " + HASHvalue);
			
			System.out.println("\n" + dir1ListFile[i].getName());
			if(savedHASH.compareTo(HASHvalue) == 0)
			{
				System.out.println("\t yes");
			}
			else
			{
				System.out.println("\t no");
			}
		}
	}
}
