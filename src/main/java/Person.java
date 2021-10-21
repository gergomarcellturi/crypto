import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;


@Data
public class Person extends Utils {
    private String name;
    private KeyPair keyPair;

    public Person(String name) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        this.name = name;
        this.keyPair = keyGen.generateKeyPair();

        System.out.println("-------------------------");
        System.out.printf("Name: %s%n", this.name);
        System.out.printf("public key: %s%n", Hex.encodeHexString(this.keyPair.getPublic().getEncoded()));
        System.out.printf("private key: %s%n", Hex.encodeHexString(this.keyPair.getPrivate().getEncoded()));
        System.out.println("-------------------------");
    }


    public Transaction send(Person personTo, Long sum) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, DecoderException, InvalidKeySpecException {
        Transaction previousTransaction = Crypto.transactionList.get(0);
        if (!verify(previousTransaction)) {
            throw new Error("Transaction veryfication failed");
        }
        Transaction transaction = new Transaction(
                this.keyPair.getPublic(),
                personTo.getPublicKeyHash(),
                sum,
                previousTransaction.getHash());
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(this.keyPair.getPrivate());
        transaction.sign(signature);
        System.out.printf("Transaction signed: %s%n", Hex.encodeHexString(transaction.getSignature()));
        Crypto.handleTransactions();
        return transaction;
    }

    public boolean verify(Transaction transaction) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, DecoderException, InvalidKeySpecException {
        if (Crypto.transactionList.size() == 1) {
            return true;
        }
        boolean publicKeyCheck = DigestUtils.sha256Hex(getPublicKeyHash()).equals(transaction.getTo());
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(transaction.getFrom());
        signature.update(transaction.toContentString().getBytes(StandardCharsets.UTF_8));
        boolean transactionCheck = signature.verify(transaction.getSignature());
        return publicKeyCheck && transactionCheck;
    }

    public String getPublicKeyHash() {
        return Hex.encodeHexString(this.keyPair.getPublic().getEncoded());
    }

}
