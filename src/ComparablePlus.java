public interface ComparablePlus<T> extends Comparable<T> {
    default boolean lessThan( T obj ) {
        return this.compareTo(obj) < 0;
    }
    default boolean greaterThan( T obj ) {
        return this.compareTo(obj) > 0;
    }
    default boolean equalTo( T obj ) {
        return this.equals(obj);
    }
    default boolean notEqualTo( T obj ) {
        return !this.equals(obj);
    }
    default boolean lessThanEqualTo( T obj ) {
        return this.compareTo(obj) <= 0;
    }
    default boolean greaterThanEqualTo( T obj ) { return this.compareTo(obj) >= 0; }
}