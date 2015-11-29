/**
 * Created by andrew on 11/5/15.
 */
import javax.xml.bind.DatatypeConverter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.security.MessageDigest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class SignatureLauncher
{
    /**
     *
     * @param args
     */
    public static void main(String[] args)
    {
        System.out.println("Please enter your password to use the HMAC file verification program.");
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        password = password.toLowerCase();
        byte[] passwordBytes = password.getBytes();
        byte[] hashedPasswordBytes = getHashedPassword(passwordBytes);
        boolean passwordMatch = passwordCompare(hashedPasswordBytes);
        if(passwordMatch == true){
            System.out.println("Enter 'generate' to generate a hash value for a new file, or enter 'validate' to validate the existing files.");
            String input = scanner.nextLine();
            input = input.toLowerCase();
            if(input.equals("generate")){
                Signature sig = new Signature("./directory1", "./directory2");
                sig.signatureAlgorithm();
            } else if(input.equals("validate")){
                Validator val = new Validator("./directory1", "./directory2");
                val.validationAlgorithm();
            } else{
                System.out.println("Invalid option, please try again.");
            }
        } else{
            System.out.println("Incorrect password, please try again.");
        }
    }

    /**
     *
     * @param password
     * @return
     */
    private static byte[] getHashedPassword(byte[] password)
    {
        byte[] hashedPasswordBytes = null;
        //Storage for digest bytes
        byte[] passwordFileBytes = new byte[password.length];
        try{
            //Use SHA-512
            MessageDigest hasher = MessageDigest.getInstance("SHA-512");
            //File passwordFile = new File("./password.txt");
            //Hash the file bytes
            hasher.update(passwordFileBytes);
            //Finish and cleanup
            hashedPasswordBytes = hasher.digest();
            hasher.reset();
        } catch(NoSuchAlgorithmException exception){
            System.out.println(exception.getMessage());
        }
        return hashedPasswordBytes;
    }

    /**
     *
     * @param hashedPasswordBytes
     * @return
     */
    private static boolean passwordCompare(byte[] hashedPasswordBytes)
    {
        boolean match = false;
        //Convert the hash bytes to hex String
        String hexHash1 = DatatypeConverter.printHexBinary(hashedPasswordBytes);
        try{
            //Read password hex String from file
            FileReader fileReader = new FileReader("./password.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            try{
                //The hex String is all one line
                String passwordFileContents = bufferedReader.readLine();
                //Check if the hex Strings of the two files are equal
                if(hexHash1.equals(passwordFileContents)){
                    match = true;
                }
            } catch(IOException exception3){
                System.out.println(exception3.toString());
            }
        } catch(FileNotFoundException exception2){
            System.out.println(exception2.toString());
        }
        return match;
    }
}
