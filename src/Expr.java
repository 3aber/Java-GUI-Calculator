abstract class Expr {
  abstract public String toString();
  abstract public boolean isGround();
  abstract public int IntNum();
  abstract public double FloatNum();

  static void print(Expr e) { System.out.println(e.toString()); }

  static String betweenParens(final Expr e) {
    return (e.isGround()) ? e.toString()
                          : "( " + e.toString() + " )"; }

  public static void main(String[] args) {
    Expr e1 = new Plus(new Integer2(5), new Variable("b"));
    Expr e2 = new Times(e1, new Times(e1, e1));
    Expr e3 = new Times(new Integer2(5), new Variable("b"));
    Expr e4 = new Plus(new Variable("a"), e1);
    print(e1);
    print(e2);
    print(e3);
    print(e4);

    String s1 = "2+(3*7)*(5/3)-2";
    Parser p = new Parser(s1);
    try {
      Expr e5 = p.parse();
      print(e5);
    } catch (ParseError e) {
      System.out.println(e);
    }
  }
}
