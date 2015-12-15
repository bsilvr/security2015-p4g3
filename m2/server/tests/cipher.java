import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import javax.crypto.spec.IvParameterSpec;


public class cipher{

    public static void main(String args[]){

        try{
            String password = "passwordpassword";
            File input = new File("input.txt");
            File output = new File("output.txt");

            byte[] cipherText;
            FileInputStream fisin = new FileInputStream(input);
            FileOutputStream fiout = new FileOutputStream(output);

            SecretKey secretKey;

            byte[] keydata = password.getBytes();
            SecretKeySpec sks = new SecretKeySpec(keydata, "AES");


            Cipher c;
            c = Cipher.getInstance("AES/CBC/NoPadding");


            IvParameterSpec spec = new IvParameterSpec("aaaaaaaaaaaaaaaa".getBytes());

            c.init(Cipher.ENCRYPT_MODE, sks, spec);

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
