package word

import generators.OneOfGen




case class WordInSeqGen(sequences: AllSequences)(implicit letters: List[Char]) extends WordGen {
  def this()(implicit letters: List[Char]) = this(new AllSequences())

  private val gen: OneOfGen[Word] = new OneOfGen[Word](sequences.combinations)

  override def random: Word = gen.generate

  override def first: Word = sequences.combinations.head
}