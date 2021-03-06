import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;


public class decipher{

    public static void main(String args[]){

        try{
            String password = "IthUlaB5oF+NpKjmiF4P4Q==";
            File input = new File("cipher.txt");
            File output = new File("after.txt");

            byte[] cipherText;
            FileInputStream fisin = new FileInputStream(input);
            FileOutputStream fiout = new FileOutputStream(output);

            SecretKey secretKey;

            byte[] keydata = Base64.getDecoder().decode(password);
            SecretKeySpec sks = new SecretKeySpec(keydata, "AES");


            Cipher c;
            c = Cipher.getInstance("AES/CBC/NoPadding");


            IvParameterSpec spec = new IvParameterSpec("aaaaaaaaaaaaaaaa".getBytes());

            c.init(Cipher.DECRYPT_MODE, sks, spec);

            long bytesRead = 0;
            long fileSize = input.length();
            int blockSize = c.getBlockSize();

            while (bytesRead < fileSize){
                byte[] dataBlock = new byte[blockSize];
                bytesRead += fisin.read(dataBlock);
                cipherText = c.update(dataBlock);
                fiout.write(cipherText);
            }
            cipherText = c.doFinal();

            fiout.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }


    }

}
