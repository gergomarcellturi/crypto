import ascelion.merkle.TreeBuilder;
import ascelion.merkle.TreeLeaf;
import ascelion.merkle.TreeRoot;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class MerkleTree {
    private TreeRoot<String> root;
    private TreeBuilder<String> builder;
    private BinaryOperator<String> concatFn;
    private UnaryOperator<String> hashFn;

    public MerkleTree() {
        this.concatFn = (s1, s2) -> s1 + s2;
        this.hashFn = DigestUtils::sha256Hex;
        this.builder = new TreeBuilder<>(hashFn, concatFn, "GENESIS");
    }

    public void add(String data) {
        builder.collect( new TreeLeaf<>("SHA-256", data));
    }

    public void build() {
        this.root = this.builder.build();
    }

    public String getHash() {
        return this.root.hash();
    }

    public boolean verify(String hash) {
        for (int i = 0; i < this.root.count(); i++) {
            if (this.root.getLeaf(i).getContent().equals(hash)) {
                return true;
            }
        }
        return false;
    }
}
