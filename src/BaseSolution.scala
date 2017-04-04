import generators.{Generator, OneOfGen}
import word.{Word, WordInSeqGen}

/**
  * Created by k.neyman on 03.04.2017.
  */
abstract class BaseSolution(
                             protected val lettersAsWord: String,
                             protected val predef: List[String]
                           ) {

  protected implicit val letters: List[Char] = lettersAsWord.toList
  protected val size = letters.size
  protected val seqGen: WordInSeqGen = new WordInSeqGen

  protected val filtered: List[List[Word]] = predef.zipWithIndex.map {
    case (_, i) => collectAll(i, seqGen.copy().first)
  }

  protected val filteredGen: List[Generator[Word]] = filtered.map(new OneOfGen(_))

  def solve: Field

  def printAll(word: Word): Unit = {
    println(word)
    word.next match {
      case Some(nextWord) => printAll(nextWord)
      case _ => println("Finished")
    }
  }

  def adjustField(field: Field): Field = {
    var adjusted = field

    (adjusted.list zip predef zipWithIndex) foreach { case ((word, standard), i) =>
      if (!isCorrect(word.word, standard))
        adjusted = adjusted.copy(i, filteredGen(i).generate)
    }
    adjusted
  }

  def createField = new Field(filteredGen.map(_.generate), seqGen)

  def isCorrect(word: String, predef: String): Boolean = {
    (word.toList zip predef.toList) forall { case (w, p) => p == ' ' || w == p }
  }

  class FilteredWord(val wordPos: Int, word: Word) extends Word(word.word) {
    override def next: Option[Word] = {
      var nextWord: Option[Word] = word.next

      val standard: String = predef(wordPos)
      while (nextWord.isDefined && !isCorrect(nextWord.get.word, standard))
        nextWord = nextWord.get.next

      nextWord.map(new FilteredWord(wordPos, _))
    }
  }

  def collectAll(pos: Int, word: Word): List[Word] = collect(new FilteredWord(pos, word))

  def collect(filtered: Word): List[Word] = filtered.next match {
    case Some(w) => w :: collect(w)
    case _ => Nil
  }
}
