import generators.{Generator, OneOfGen}
import word.{Word, WordInSeqGen}

/**
  * Created by k.neyman on 02.04.2017.
  */
class SolutionGreedy(
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
      field = goDown(field, 0)
    }
    field
  }

  def goDown(field: Field, rowNum: Int): Field = {
    val f = filtered(rowNum)
      .toStream
      .map { w => field.copy(rowNum, w) }
      .minBy(_.conflicts)
    if (rowNum + 1 < size) goDown(f, rowNum + 1)
    else f
  }
}
