/**
 * Created by andrew on 11/5/15.
 */
public class SignatureLauncher
{
    public static void main(String[] args)
    {
        Signature sig = new Signature("./directory1", "./directory2");
        Validator val = new Validator("./directory1", "./directory2");
        sig.signatureAlgorithm();
        val.validationAlgorithm();
    }
}
