/**
 * Created by andrew on 11/4/15.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;

public class Signature
{
    private String path1;
    private String path2;

    public Signature(String dir1Path, String dir2Path)
    {
        this.path1 = dir1Path;
        this.path2 = dir2Path;
    }

    public void signatureAlgorithm() throws NullPointerException
    {
        //Get directory1 as a File
        File dir1 = new File(this.path1);
        try {
            //Get all files from directory1
            File[] dir1Files = dir1.listFiles();
            //Loop through all files
            for(File file : dir1Files){
                try{
                    //Compute the digest of the file
                    byte[] bytes = this.messageDigest(file);
                    //Write the digest to directory2
                    this.writeToOutFile(file, bytes);
                } catch(NoSuchAlgorithmException exception){
                    System.out.println(exception.getMessage());
                }
            }
        } catch(SecurityException exception){
            System.out.println(exception.getMessage());
        }
    }

    private byte[] messageDigest(File file) throws NoSuchAlgorithmException
    {
        //Use SHA-512
        MessageDigest hasher = MessageDigest.getInstance("SHA-512");
        byte[] hashedBytes = null;
        //Storage for digest bytes
        byte[] fileBytes = new byte[(int) file.length()];
        try{
            FileInputStream inputStream = new FileInputStream(file);
            try{
                //Read fileBytes.length bytes from file
                int bytesRead = inputStream.read(fileBytes);
                inputStream.close();
                //Hash the file bytes
                hasher.update(fileBytes);
                hashedBytes = hasher.digest();
            } catch(IOException exception){
                System.out.println(exception.getMessage());
            }
        } catch(FileNotFoundException exception){
            System.out.println(exception.getMessage());
        }
        return hashedBytes;
    }

    private void writeToOutFile(File file, byte[] byteArray)
    {

    }
}
