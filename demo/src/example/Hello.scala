package example

import rx._

object Hello extends App {

  var count = 0
  val a = Var(1)
  val b = Var(2)

  def mkRx(i: Int)(implicit ctx: Ctx.Owner) = Rx {
    println("!!! count += 1")
    count += 1
    i + b()
  }

  val c = Rx {
    println("!!! val newRx = mkRx(a())")
    val newRx = mkRx(a())
    newRx()
  }

  println(c.now, count) // (3,1)
  a() = 4
  println(c.now, count) // (6,2)

  b() = 3
  // `b()`变了，首先`newRx()`会重新计算（因为它依赖于`b()`），会调用`count+=1`
  // 然后`c`会重新计算（因为它依赖于`newRx()`），会再次调用`count+=1`
  // 所以打印出`4`
  println(c.now, count) // (7,4)

  // 从0到100，共101次
  (0 to 100).foreach { i => a() = i }

  println(c.now, count) //(103,105)
  b() = 4
  println(c.now, count) //(104,107)

}
