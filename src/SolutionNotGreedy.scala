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
    var field: Field = adjustField(new Field(seqGen))


    while (field.conflicts > 0) {
      retryNum += 1
      field = adjustField(field.recreate)
      println(s"Init: retryNum = $retryNum\n$field")

      // random
      field = goDown(field, 0)
    }
    field
  }

  def goDown(field: Field, rowNum: Int): Field = {
    val fields: List[Field] = all(field, rowNum)
    val top = fields.maxBy(_.conflicts).conflicts + 1
    val gen: Generator[Field] = new OneOfGen(fields.flatMap { f => List.tabulate(top - f.conflicts) { _ => f } })
    if (rowNum + 1 < size)
      goDown(gen.generate, rowNum + 1)
    else
      gen.generate
  }

  def all(field: Field, rowNum: Int): List[Field] = filtered(rowNum).map(field.copy(rowNum, _))
}
