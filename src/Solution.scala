import generators.{Generator, OneOfGen}
import word.{Word, WordInSeqGen}

/**
  * Created by k.neyman on 02.04.2017.
  */
class Solution (
                 lettersAsWord: String,
                 predef: List[String]
               ) extends BaseSolution(lettersAsWord, predef) {

  def solve: Field = {
    var retryNum = 0
    var field: Field = adjustField(new Field(seqGen))
    while (field.conflicts > 0) {
      retryNum += 1
      field = adjustField(field.recreate)
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
}
