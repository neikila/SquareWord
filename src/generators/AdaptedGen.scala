package generators

/**
  * Created by k.neyman on 04.04.2017.
  */
class AdaptedGen[T](val list: List[T]) extends Generator[T]{
  private val gen: Generator[Int] = new ChooseIntGen(1, math.pow(2, list.size).toInt)

  override def generate: T = list((math.log(gen.generate) / math.log(2)).toInt)
}
