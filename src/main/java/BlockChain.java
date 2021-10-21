import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockChain extends Utils {

    public static ArrayList<Block> blocks = new ArrayList<>() ;
    public static HashMap<String, List<Block>> tempBlocks = new HashMap<>();

    public static Block createBlock(List<Block> chain, List<Transaction> transactionList) {
        String prevHash = chain != null && !chain.isEmpty() ? chain.get(0).getHash() : DigestUtils.sha256Hex("first");
        return new Block(prevHash, transactionList);
    }

    public static void branch(String id, List<Block> blocks) {
        BlockChain.tempBlocks.put(id, blocks);
    }

    public static boolean verify(String transactionHash, MerkleTree merkleTree) {
        if (merkleTree == null) {
            return false;
        }

        return merkleTree.verify(transactionHash);
    }
}
