package byteback.dummy.complete;

class LinearSearch {

  def apply(a: Array[Int], n: Int): Int = {
    var i: Int = 0;

    while (i < a.length) {
      if (a[i] == n) {
        return i;
      }
    }

    return -1;
  }

}
