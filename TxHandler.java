import java.util.ArrayList;

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
    	ArrayList<Transaction.Output> outputs = tx.getOutputs();
    	for (Transaction.Output op : outputs) {
    		;
    	}
    	
    	ArrayList<Transaction.Input> inputs = tx.getInputs();
    	for (int i = 0; i < tx.numInputs(); i++) {
    		Transaction.Input curr = tx.getInput(i);
    		result = Crypto.verifySignature(tx.getOutput(curr.outputIndex).address, tx.getRawDataToSign(i), curr.signature);
    		if (result == false) {
    			return false;
    		}
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
    	return possibleTxs;
    }

}