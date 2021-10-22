import org.apache.commons.codec.DecoderException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Crypto {
    static Miner miner = new Miner("miner0");

    static ArrayList<Transaction> transactionList = new ArrayList<>();

    public static ArrayList<Transaction> getTransactionList() {
        return (ArrayList<Transaction>) transactionList.stream().filter(transaction -> !"".equals(transaction.getPrevHash())).collect(Collectors.toList());
    }

    public static void handleTransactions() {
        miner.createBlock(Crypto.getTransactionList());

        if (!BlockChain.tempBlocks.containsKey(miner.getId())) {
            BlockChain.tempBlocks.put(miner.getId(), new ArrayList<>());
        }
        List<Block> blockChain = BlockChain.tempBlocks.get(miner.getId());

        if (blockChain.size() > 1) {
            System.out.printf("Local blockchain: %s%n", miner.getId());
            System.out.println("====================");
            Crypto.getTransactionList().forEach(transaction -> {
                if (BlockChain.verify(transaction.getHash(), blockChain.get(0).getTransactionTree())) {
                    System.out.println( blockChain.get(0).getHash() + " contains: " + transaction.getHash() );
                } else {
                    System.out.println( blockChain.get(0).getHash() + " doesn't contain: " + transaction.getHash());
                }
            });

        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException, InvalidKeyException, DecoderException, InvalidKeySpecException {
        Crypto.transactionList.add(Transaction.firstTransaction());
        Person tom = new Person("Tom");
        Person daniel = new Person("Daniel");
        Person edward = new Person("Edward");
        Person john = new Person("John");
        Person livia = new Person("Lívia");



        tom.send(daniel, 1L);
        daniel.send(livia, 2L);
        livia.send(daniel, 3L);
        daniel.send(livia, 4L);
        livia.send(daniel, 5L);
        daniel.send(john, 6L);
        john.send(edward, 7L);
        edward.send(john, 8L);

        System.out.println("Transactions were successful");
    }
}
