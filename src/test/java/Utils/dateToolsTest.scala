package Utils

import java.util.{Date}
import org.scalatest.FunSuite

class dateToolsTest extends FunSuite {

  test("testNotIsFuture") {
    val past = new Date()
    Thread.sleep(1000) // wait for 1000 millisecond

    assert(!dateTools.isFuture(past))
  }


  // TESTS A IMPLLEMENTER

  test("testParseDate") {

  }

  test("testGetDate") {

  }
}
