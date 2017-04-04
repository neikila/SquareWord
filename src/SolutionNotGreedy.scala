import generators.{Generator, OneOfGen}
import word.{Word, WordInSeqGen}

/**
  * Created by k.neyman on 02.04.2017.
  */
class SolutionNotGreedy (
  lettersAsWord: String,
  predef: List[String]
) extends BaseSolution(lettersAsWord, predef) {

  def solve: Field = {
    var retryNum = 0
    var field: Field = createField

    while (field.conflicts > 0) {
      retryNum += 1
      field = createField
      // random
      field = goDown(field, 0)
      println(s"Init: retryNum = $retryNum\n$field")
    }
    field
  }

  def goDown(field: Field, rowNum: Int): Field = {
    val fields: List[Field] = all(field, rowNum)
    val top = fields.maxBy(_.conflicts).conflicts + 1
    val min = fields.minBy(_.conflicts)
    if (min.conflicts == 0)
      min
    else {
      val gen: Generator[Field] = new OneOfGen(fields.flatMap { f => List.tabulate(top - f.conflicts) { _ => f } })
      val fieldApplied: Field = gen.generate
      println(s"field applied:\n$fieldApplied")
      if (rowNum + 1 < size) goDown(fieldApplied, rowNum + 1)
      else fieldApplied
    }
  }

  def all(field: Field, rowNum: Int): List[Field] = filtered(rowNum).map(field.copy(rowNum, _))
}
