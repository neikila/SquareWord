package word

import generators.OneOfGen

/**
  * Created by k.neyman on 02.04.2017.
  */
class AllSequences(implicit letters: List[Char]) {
  def create(base: List[Char], symbolsLeft: List[Char]): List[String] = {
    symbolsLeft match {
      case h :: t => shuffle(base ::: h :: Nil).flatMap(create(_, t))
      case Nil => base.mkString :: Nil
    }
  }

  def shuffle(base: List[Char]): List[List[Char]] = {
    val last = base.last
    base :: (base.take(base.size - 1) map { sym => base.swap(last, sym)})
  }

  implicit class Swapper(list: List[Char]) {
    def swap(first: Char, second: Char): List[Char] = {
      val fIndex = list.indexOf(first)
      val sIndex = list.indexOf(second)
      list.updated(fIndex, second).updated(sIndex, first)
    }
  }

  val combinations: List[RoundedInSeq] =
    letters.sorted match {
      case h :: t => create(h :: Nil, t)
        .zipWithIndex
        .map { case (word, id) => new RoundedInSeq(id, word) }
      case _ => throw new IndexOutOfBoundsException
    }

  class WordInSeq(val id: Int, word: String) extends Word(word) {
    override def next: Option[Word] =
      if (id + 1 < combinations.length) Some(combinations(id + 1))
      else None
  }

  class RoundedInSeq(val startId: Int, id: Int, word: String) extends WordInSeq(id, word) {
    def this(id: Int, word: String) = this(id, id, word)

    override def next: Option[Word] = {
      val nextId: Int = (id + 1) % combinations.length
      if (nextId != startId) {
        Some(combinations(nextId).copy(startId))
      }
      else None
    }

    def copy(startId: Int): RoundedInSeq = new RoundedInSeq(startId, id, word)
  }
}


class WordInSeqGen(implicit letters: List[Char]) extends WordGen {
  val sequences: AllSequences = new AllSequences()
  private val gen: OneOfGen[Word] = new OneOfGen[Word](sequences.combinations)

  override def random: Word = gen.generate

  override def first: Word = sequences.combinations.head
}