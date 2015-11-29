/**
 * Created by andrew on 11/4/15.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

public class Validator
{
    private String path1;
    private String path2;

    /**
     *
     * @param dir1Path
     * @param dir2Path
     */
    public Validator(String dir1Path, String dir2Path)
    {
        this.path1 = dir1Path;
        this.path2 = dir2Path;
    }

    /**
     *
     */
    public void validationAlgorithm()
    {
        //Get directory1 as a File
        File dir1 = new File(this.path1);
        //Get directory2 as a File
        File dir2 = new File(this.path2);
        try {
            //Get all files from directory1 and 2
            File[] dir1Files = dir1.listFiles();
            File[] dir2Files = dir2.listFiles();
            //Loop through all files in directory1
            for(File file1 : dir1Files){
                //For each file, get the file name
                //Remove the file extension
                String file1Name = file1.getName();
                int index1 = file1Name.indexOf(".");
                String fixedFile1Name = file1Name.substring(0, index1);
                //Loop through all files in directory2
                for(File file2: dir2Files){
                    //Remove the file extension
                    String file2Name = file2.getName();
                    int index2 = file2Name.indexOf(".");
                    String fixedFile2Name = file2Name.substring(0, index2);
                    //Check if directory2 file name matches directory1 file name
                    if(fixedFile2Name.equals(fixedFile1Name)){
                        this.validate(file1, file2);
                    }
                }
            }
        } catch(SecurityException exception){
            System.out.println(exception.getMessage());
        }
    }

    /**
     *
     * @param file1
     * @param file2
     */
    private void validate(File file1, File file2)
    {
        byte[] hashFile1 = null;
        try{
            //Has file from directory1 again
            hashFile1 = this.messageDigest(file1);
            //Convert the hash bytes to hex String
            String hexHash1 = DatatypeConverter.printHexBinary(hashFile1);
            try{
                //Read hex String from file in directory2
                FileReader fileReader = new FileReader(file2);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                try{
                    //The hex String is all one line
                    String file2Contents = bufferedReader.readLine();
                    //Check if the hex Strings of the two files are equal
                    if(hexHash1.equals(file2Contents)){
                        System.out.println(file1.getName() + " is valid");
                    } else{
                        System.out.println(file1.getName() + " is NOT valid");
                    }
                } catch(IOException exception3){
                    System.out.println(exception3.toString());
                }
            } catch(FileNotFoundException exception2){
                System.out.println(exception2.toString());
            }
        } catch(NoSuchAlgorithmException exception){
            System.out.println(exception.toString());
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
                hasher.reset();
            } catch(IOException exception){
                System.out.println(exception.getMessage());
            }
        } catch(FileNotFoundException exception){
            System.out.println(exception.getMessage());
        }
        return hashedBytes;
    }
}
