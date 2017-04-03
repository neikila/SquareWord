import generators.{Generator, OneOfGen}
import word.{Word, WordInSeqGen}

/**
  * Created by k.neyman on 02.04.2017.
  */
class Solution(
  protected val lettersAsWord: String,
  protected val predef: List[String]
) {
  protected implicit val letters: List[Char] = lettersAsWord.toList
  protected val size = letters.size
  protected val seqGen: WordInSeqGen = new WordInSeqGen
  protected val filtered: List[Generator[Word]] = predef.zipWithIndex.map {
    case (_, i) => new OneOfGen(collectAll(i, seqGen.first))
  }

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
        adjusted = adjusted.copy(i, filtered(i).generate)
    }
    adjusted
  }

  def isCorrect(word: String, predef: String): Boolean = {
    (word.toList zip predef.toList) forall { case (w, p) => p == ' ' || w == p }
  }

  def createField: Field = new Field(filtered.map(_.generate), seqGen)

  def solve: Field = {
    var retryNum = 0
    var field: Field = createField
    while (field.conflicts > 0) {
      retryNum += 1
      field = createField
      println(s"Init: retryNum = $retryNum\n$field")
      field = recurse(field, 0, field.conflicts)
    }
    field
  }

  // Вернет наиболее выгодный
  def recurse(field: Field, pos: Int, minRecurseNum: Int): Field = {
    field.conflicts match {
      case 0 => field
      case num if num < minRecurseNum =>
        recurse(field, pos, num)
      case num => change(field, pos, minRecurseNum) match {
        case Some(fieldChanged) if fieldChanged.conflicts < num => fieldChanged
        case None if pos + 1 < size => recurse(field, pos + 1, minRecurseNum)
        case _ => field
      }
    }
  }

  def change(field: Field, pos: Int, minRecurseNum: Int): Option[Field] = {
    val next: Option[Word] = new FilteredWord(pos, field(pos)).next
    next match {
      case Some(word) => Some(recurse(field.copy(pos, word), pos, minRecurseNum))
      case _ => None
    }
  }

  class FilteredWord(val wordPos: Int, word: Word) extends Word(word.word) {
    override def next: Option[Word] = {
      var nextWord: Option[Word] = word.next

      val standard: String = predef.applyOrElse[Int, String](wordPos, e => "")
      while (nextWord.isDefined && !isCorrect(nextWord.get.word, standard))
        nextWord = nextWord.flatMap(_.next)

      nextWord.map(new FilteredWord(wordPos, _))
    }
  }

  def collectAll(pos: Int, word: Word): List[Word] = collect(new FilteredWord(pos, word))
  def collect(filtered: Word): List[Word] = filtered.next match {
    case Some(w) => w :: collect(w)
    case _ => Nil
  }
}
