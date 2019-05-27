package com.verify.utils;

import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 加密配置文件，如 username password 等敏感信息
 */
public class DESUtil {
    private static Key key;
    private static String KEY_STR = "123key456";
    private static String CHARSETNAME = "UTF-8";
    private static String ALGORITHM = "DES";
    private static final String KEY_SEED = "PHOENIX_KEY_SEED";
    private static Logger logger = LoggerFactory.getLogger(DESUtil.class);

    public DESUtil() {
    }

    public static String getEncryptString(String str, String keySeed) {

        try {
            byte[] bytes = str.getBytes(CHARSETNAME);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(1, getKey(keySeed));
            byte[] doFinal = cipher.doFinal(bytes);
            BASE64Encoder base64Encoder = new BASE64Encoder();
            return base64Encoder.encode(doFinal);
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }

    public static String getEncryptString(String str) {
        return getEncryptString(str, (String)null);
    }

    public static String getDecryptString(String str, String keySeed) {
        logger.info("started to getDecryptString:******:keySeed is:" + keySeed);

        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] bytes = base64Decoder.decodeBuffer(str);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(2, getKey(keySeed));
            byte[] doFinal = cipher.doFinal(bytes);
            return new String(doFinal, CHARSETNAME);
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }

    public static String getDecryptString(String str) {
        return getDecryptString(str, (String)null);
    }

    private static Key getKey(String keySeed) {
        if (key != null) {
            return key;
        } else {
            if (keySeed == null) {
                keySeed = System.getenv("PHOENIX_KEY_SEED");
            }

            if (keySeed == null) {
                keySeed = System.getProperty("PHOENIX_KEY_SEED");
            }

            if (keySeed == null) {
                keySeed = KEY_STR;
            }

            try {
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                secureRandom.setSeed(keySeed.getBytes());
                KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
                generator.init(secureRandom);
                key = generator.generateKey();
                return key;
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length >= 2 && args[0].equals("encrypt")) {
            if (args.length == 2) {
                System.out.println(getEncryptString(args[1]));
            } else if (args.length == 3) {
                System.out.println(getEncryptString(args[1], args[2]));
            } else {
                System.out.println("args error");
            }
        } else {
            System.out.println("args error");
        }

    }
}