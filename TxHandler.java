import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TxHandler {

	private UTXOPool pool;
    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        this.pool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        // IMPLEMENT THIS
    	boolean result = true;
    	double opSum = 0;
    	double ipSum = 0;
    	ArrayList<Transaction.Output> outputs = tx.getOutputs();
    	for (Transaction.Output op : outputs) {
    		// condition 4
    		if (op.value < 0) {
    			return false;
    		}
    		opSum += op.value;
    	}
    	
    	Set<UTXO> seenUTXOs = new HashSet<UTXO>();
    	ArrayList<Transaction.Input> inputs = tx.getInputs();
    	for (int i = 0; i < tx.numInputs(); i++) {
    		Transaction.Input currIn = tx.getInput(i);
    		UTXO currUTXO = new UTXO(currIn.prevTxHash, currIn.outputIndex);
    		
    		// condition 1
    		if (pool.contains(currUTXO) == false) {
    			return false;
    		}
            Transaction.Output output = pool.getTxOutput(currUTXO);
    		
            // condition 2
            result = Crypto.verifySignature(output.address, tx.getRawDataToSign(i), currIn.signature);
    		if (result == false) {
    			return false;
    		}
    		
    		// condition 3
    		if (seenUTXOs.contains(currUTXO)) {
    			return false;
    		} else {
    			seenUTXOs.add(currUTXO);
    		}
    		
    		if (output.value < 0) {
    			return false;
    		}
    		ipSum += output.value;
    	}
    	
    	// condition 5
    	if (ipSum < opSum) {
    		return false;
    	}
    	
    	return true;
    	
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
    	Set<Transaction> txns = new HashSet<Transaction>();
    	
    	for (Transaction tx : possibleTxs) {
    		if (isValidTx(tx)) {
    			txns.add(tx);
    			// remove inputs from utxo pool
    			for (int i = 0; i < tx.numInputs(); i++) {
    	    		Transaction.Input currIn = tx.getInput(i);
    	    		UTXO currUTXO = new UTXO(currIn.prevTxHash, currIn.outputIndex);
    	    		pool.removeUTXO(currUTXO);
    			}
    			// add unspent outputs to utxo pool
    			for (int i = 0; i < tx.numOutputs(); i++) {
    				Transaction.Output currOut = tx.getOutput(i);
    				UTXO currUTXO = new UTXO(tx.getHash(), i);
    				pool.addUTXO(currUTXO, currOut);
    			}
    		}
    	}
    	
    	return txns.toArray(new Transaction[txns.size()]);
    }

}
