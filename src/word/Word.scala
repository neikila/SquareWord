package word

import scala.util.Random

/**
  * Created by k.neyman on 02.04.2017.
  */
abstract class Word(val word: String) {
  def this(word: List[Char]) = this(word.mkString)

  val size = word.length

  def apply(pos: Int): Char = {
    if (pos >= size) {
      println(word)
    }
    word(pos)
  }

  override def toString: String = word

  def next: Option[Word]
}