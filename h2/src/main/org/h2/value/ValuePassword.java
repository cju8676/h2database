package org.h2.value;

import org.h2.engine.CastDataProvider;
import org.h2.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.security.SecureRandom;
import java.util.Base64;

import static org.h2.value.ValueVarchar.EMPTY;

public class ValuePassword extends ValueStringBase {

    private ValuePassword(String value) {
        super(value);
    }

    @Override
    public StringBuilder getSQL(StringBuilder builder, int sqlFlags) {
        return StringUtils.quoteStringSQL(builder, value);
    }

    @Override
    public int getValueType() {
        return SECURE_PASSWORD;
    }

    public static Value get(String s) {
        return get(s, null);
    }

    public static Value get(String s, CastDataProvider provider) {
        if (s.isEmpty()) {
            return provider != null && provider.getMode().treatEmptyStringsAsNull ? ValueNull.INSTANCE : EMPTY;
        }
        String[] split = s.split("~~~");
        if (split.length != 2) {
            return Value.cache(new ValuePassword(StringUtils.cache(s)));
        }
        String pword = split[0];
        String key = split[1];
        ValuePassword obj = new ValuePassword(StringUtils.cache(encrypt(pword, key)));
        if (s.length() > 100) {
            return obj;
        }
        return Value.cache(obj);
    }

    public static String encrypt(String s,  String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            System.out.println("ENCRYPTION KEY: " + key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(s.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return s; // Return the original string if encryption fails
        }
    }

}
