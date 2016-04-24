// Interface for HashCode. Has only one method declaration
public interface HashCode<K> {

	// For an input object, computes and returns its HashCode as an integer
    public int giveCode(K k);
}
