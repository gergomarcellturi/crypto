import lombok.Data;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

@Data
public class Transaction extends Utils {
    private String prevHash;
    private PublicKey from;
    private String to;
    private Long sum;
    private byte[] signature;

    public Transaction(PublicKey from, String to, Long sum, String prevHash) {
        this.from = from;
        this.to = DigestUtils.sha256Hex(to);
        this.sum = sum;
        this.prevHash = prevHash;
    }

    public void sign(Signature signature) throws SignatureException, DecoderException {
        signature.update(this.toContentString().getBytes(StandardCharsets.UTF_8));
        this.signature = signature.sign();
        Crypto.transactionList.add(0, this);
    }

    public static Transaction firstTransaction() {
        Transaction transaction = new Transaction(null, "ASD", 0L, "");
        transaction.setSignature(new byte[0]);
        return transaction;
    }

    public String getContent() {
        return String.format("from: %s | to: %s | sum: %d | signature: %s | prevHash: %s",
                this.from == null ? "" : this.from,
                this.to,
                this.sum,
                this.getSignature() == null ? "" : Hex.encodeHexString(this.getSignature()),
                this.prevHash
        );
    }

    public String toContentString() {
        return String.format("from: %s | to: %s | sum: %d",
                this.from == null ? "" : Hex.encodeHexString(this.from.getEncoded()),
                this.to,
                this.sum
        );
    }

    public String getHash() {
        return this.hashContent(this.toContentString());
    }
}
