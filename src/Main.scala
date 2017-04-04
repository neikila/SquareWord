import word.Word

/**
  * Created by k.neyman on 02.04.2017.
  */
object Main extends SolutionNotGreedy("кобура", "кобура" :: "" :: "   раб" :: "" :: " бак" :: "  " :: Nil) {

  def main(args: Array[String]): Unit = {
//    println(filtered.mkString("\n"))
//
    val result: Field = solve
    println(s"Result:\n$result")
//
//    println(collectAll(0, seqGen.first))
//    println(collectAll(2, seqGen.first))

//    val w1 = new StubWord("cdba")
//    val w2 = new StubWord("bacd")
//    val w3 = new StubWord("abdc")
//    val w4 = new StubWord("dcab")
//    println(new Field(w1 :: w2 :: w3 :: w4 :: Nil).conflicts)

//    printAll(new WordInSeqGen().random)
  }

//  class StubWord(word: String) extends Word(word) {
//    override def next: Option[Word] = ???
//  }
}
