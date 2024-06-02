package zedata.com.Until;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Until {


    public static String Method_Make_MD5(String str){
        byte[] digest = null;
        try{

            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;

    }
}
