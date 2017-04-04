import generators.{AdaptedGen, Generator, OneOfGen}
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
    var field: Field = new Field(seqGen)

    while (field.conflicts > 0) {
      retryNum += 1
      field = adjustField(field.recreate)
      // random
      field = goDown(field, 0)
      println(s"Init: retryNum = $retryNum\n$field")
    }
    field
  }

  def goDown(field: Field, rowNum: Int): Field = {
    val fields: List[Field] = all(field, rowNum)
    fields.find(_.conflicts == 0) match {
      case Some(min) => min
      case _ =>
        val gen: Generator[Field] = new AdaptedGen(fields.par.groupBy(_.conflicts).toList.sortBy { case (i, _) => -i} )
          .flatMap { case (_, list) => new OneOfGen(list.toList) }
        val fieldApplied: Field = gen.generate
        if (rowNum + 1 < size) goDown(fieldApplied, rowNum + 1)
        else fieldApplied
    }
  }

  def all(field: Field, rowNum: Int): List[Field] = filtered(rowNum).map(w => field.copy(rowNum, w))
}
