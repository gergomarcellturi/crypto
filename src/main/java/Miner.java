import lombok.Data;

import java.util.List;

@Data
public class Miner {

    private String id;
    private int offset = 0;
    private boolean pending = false;

    public Miner(String id) {
        this.id = id;
    }

    public void createBlock(List<Transaction> transactionList) {
        final int transactionsInBlock = 4;
        List<Block> localChain;
        if (BlockChain.tempBlocks.containsKey(this.id)) {
             localChain = BlockChain.tempBlocks.get(this.id);
        } else {
            localChain = BlockChain.blocks;
        }
        List<Transaction> transactions = transactionList.subList(this.offset, transactionList.size());

        if (transactions.size() % transactionsInBlock == 0) {
            Block newBlock = BlockChain.createBlock(localChain, transactions);
            this.offset += transactionsInBlock;
            localChain.add(0, newBlock);

            System.out.printf("New block created: %s%n", newBlock.getHash());
            BlockChain.branch(this.id, localChain);

            this.setPending(false);
        }

    }

}
