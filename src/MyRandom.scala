trait Random {
  def nextInt: (Int, Random)

  def nextInt(n: Int): (Int, Random)
}

case class MyRandom(seed: Long) extends Random {
  def nextInt: (Int, Random) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRandom = MyRandom(newSeed)
    val n = (newSeed >>> 16).toInt
    (n, nextRandom)
  }

  def nextInt(n: Int): (Int, Random) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRandom = MyRandom(newSeed)
    val nn = ((newSeed >>> 16).toInt) % n
    (if (nn < 0) -nn else nn, nextRandom)
  }
}