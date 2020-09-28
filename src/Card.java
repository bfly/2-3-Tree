public record Card(Rank rank, Suit suit) implements ComparablePlus<Card> {

    public Card( int rank, int suit) {
        this(Card.fromRankOrdinal(rank), Card.fromSuitOrdinal(suit));
    }

    enum Rank {Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Ace }
    enum Suit {Clubs, Diamonds, Hearts, Spades }

    static final Rank[] allRanks = Rank.values();
    static final Suit[] allSuits = Suit.values();
    static final int DECK_COUNT = allRanks.length * allSuits.length;

    @Override
    public String toString() {
        return  this.rank + " of " + this.suit;
    }

    public static Rank fromRankOrdinal(int n) {return allRanks[n];}
    public static Suit fromSuitOrdinal(int n) {return allSuits[n];}

    //@Override
    public int compareTo(Card card) {
        if(this.suit == card.suit) return this.rank.ordinal() - card.rank.ordinal();
        return (this.suit.ordinal() - card.suit.ordinal());
    }
}