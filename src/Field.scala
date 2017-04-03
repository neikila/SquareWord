import word.{Word, WordInSeqGen}

/**
  * Created by k.neyman on 02.04.2017.
  */
class Field(val list: List[Word], val wordGen: WordInSeqGen)(implicit val letters: List[Char]) {
  private val size = letters.size

  def this(word: WordInSeqGen)(implicit letters: List[Char]) =
    this(List.tabulate(letters.size) { _ => word.random }, word)

  def recreate: Field = new Field(wordGen)

  def copy(pos: Int, word: Word) = {
    new Field(list.updated(pos, word), wordGen)
  }

  def apply(pos: Int): Word = list(pos)

  lazy val conflicts: Int = {
    val vertical = (0 until size).map { i => size - list.map(_(i)).distinct.size }.sum
    val diagonalLeft = size - list.zipWithIndex.map {
      case (word, i) => word(i)
    }.distinct.size
    val diagonalRight = size - list.reverse.zipWithIndex.map {
      case (word, i) => word(i)
    }.distinct.size
    vertical + diagonalLeft + diagonalRight
  }

  override def toString: String = {
    list.mkString("\n")
  }
}
