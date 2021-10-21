import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public abstract class Utils {

    public String getHexString(byte[] data) {
        return Hex.encodeHexString(data);
    }

    public String hashContent(String text) {
        return DigestUtils.sha256Hex(text);
    }

    public Boolean checkProof(String hash, int numberOfZeroes) {
        String zeros = new String(new char[numberOfZeroes]).replace("\0", "0");
        return hash.startsWith(zeros);
    }

    PublicKey getPublicKey(byte[] encodedKey) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(encodedKey);
        return factory.generatePublic(encodedKeySpec);
    }
}
