
import java.io.*;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class Operator {
	private final String algo = "PBEWITHSHA256AND256BITAES-CBC-BC";

	public void encrypt(String src, String dest, String password)
    {
        try
        {
            int buffersize=8*1024;
            File input=new File(src);
            File output=new File(dest);
            FileInputStream fis=new FileInputStream(input);
            FileOutputStream fos=new FileOutputStream(output);
            byte buffer[]=new byte[buffersize];
            KeySpec ks=new PBEKeySpec(password.toCharArray());
            SecretKeyFactory skf=SecretKeyFactory.getInstance(algo);
            SecretKey key=skf.generateSecret(ks);
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest=md.digest();
            byte[] salt=Arrays.copyOf(digest, 16);
            AlgorithmParameterSpec aps=new PBEParameterSpec(salt, 20);
            Cipher cipher=Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, key, aps);
            int reader=0;
            while((reader=fis.read(buffer))!=-1)
            {
                byte[] out=cipher.update(buffer, 0, reader);
                fos.write(out, 0, reader);
            }
            fos.write(cipher.doFinal());
            fos.close();
            fis.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
     
    public void decrypt(String src, String dest, String password)
    {
        try
        {
            int buffersize=8*1024;
            File input=new File(src);
            File output=new File(dest);
            FileInputStream fis=new FileInputStream(input);
            FileOutputStream fos=new FileOutputStream(output);
            byte buffer[]=new byte[buffersize];
            KeySpec ks=new PBEKeySpec(password.toCharArray());
            SecretKeyFactory skf=SecretKeyFactory.getInstance(algo);
            SecretKey key=skf.generateSecret(ks);
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest=md.digest();
            byte[] salt=Arrays.copyOf(digest, 16);
            AlgorithmParameterSpec aps=new PBEParameterSpec(salt, 20);
            Cipher cipher=Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, key, aps);
            int reader=0;
            while((reader=fis.read(buffer))!=-1)
            {
                byte[] out=cipher.update(buffer, 0, reader);
                fos.write(out, 0, reader);
            }
            fos.write(cipher.doFinal());
            fos.close();
            fis.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}