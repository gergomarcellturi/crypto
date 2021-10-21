import lombok.Data;

import java.util.List;

@Data
public class Block extends Utils {

    private int nonce;
    private String prevHash;
    private String hash;
    private MerkleTree transactionTree;
    private long timestamp;

    public Block(String prevHash, List<Transaction> transactionList) {
        this.prevHash = prevHash;
        this.transactionTree = new MerkleTree();
        transactionList.forEach(transaction -> {
            this.transactionTree.add(transaction.getHash());
        });
        this.transactionTree.build();
        String treeHash = this.transactionTree.getHash();

        timestamp = System.currentTimeMillis();
        int nonce = 0;
        String hash;

        while(true) {
            hash = hashContent(prevHash + treeHash + Integer.toString(nonce) + Long.toString(timestamp));
            if (checkProof(hash, 4)) {
                System.out.println("Work proved: " + hash);
                System.out.println("nonce: " + nonce);
                break;
            }
            nonce++;
        }
        this.hash = hash;
        this.nonce = nonce;
    }


}
