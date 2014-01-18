class UnaryMinus extends Expr {
  Expr e;
  public int IntNum(){
  	return 0;
  }
  public double FloatNum(){
  	return 0;
  }

  UnaryMinus(Expr x) { e = x; }
  public String toString() { return "-" + e.toString(); }
  public boolean isGround() { return false; }
}
