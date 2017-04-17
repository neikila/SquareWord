import word.Word

/**
  * Created by k.neyman on 02.04.2017.
  */
object Main extends SolutionNotGreedy("слиток", "слиток" :: "" :: "кит" :: "" :: "  лик" :: "" :: Nil) {

  def main(args: Array[String]): Unit = {
    val result: Field = solve
    println(s"Result:\n$result")
  }
}
