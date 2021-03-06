/**
 * Created by andrew on 11/4/15.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Signature
{
    private String path1;
    private String path2;
    private String password;

    /**
     *
     * @param dir1Path
     * @param dir2Path
     * @param passwd
     */
    public Signature(String dir1Path, String dir2Path, String passwd)
    {
        this.path1 = dir1Path;
        this.path2 = dir2Path;
        this.password = passwd;
    }

    /**
     *
     * @throws NullPointerException
     */
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

    /**
     *
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     */
    private byte[] messageDigest(File file) throws NoSuchAlgorithmException
    {
        Mac hasher = Mac.getInstance("HmacSHA512");
        SecretKeySpec hmacKey = new SecretKeySpec(this.password.getBytes(), "HmacSHA512");
        try{
            hasher.init(hmacKey);
        } catch(InvalidKeyException exception){
            System.out.println(exception.getMessage());
        }
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
                hashedBytes = hasher.doFinal(fileBytes);
                hasher.reset();
            } catch(IOException exception){
                System.out.println(exception.getMessage());
            }
        } catch(FileNotFoundException exception){
            System.out.println(exception.getMessage());
        }
        return hashedBytes;
    }

    /**
     *
     * @param file
     * @param byteArray
     */
    private void writeToOutFile(File file, byte[] byteArray)
    {
        //Fix the file extension of all hash files to be .txt
        String extension = file.getName().substring(file.getName().lastIndexOf("."));
        String properFileName = file.getName().replace(extension, ".txt");
        //Create output file as concatenation of directory and filename
        File outFile = new File(this.path2+"/"+properFileName);
        //Convert byte array to hexadecimal String
        String hexHash = DatatypeConverter.printHexBinary(byteArray);
        try{
            //Use a PrintWriter to write a String to outFile
            PrintWriter printer = new PrintWriter(outFile);
            printer.write(hexHash);
            printer.close();
        } catch(FileNotFoundException exception){
            System.out.println(exception.toString());
        }
    }
}
