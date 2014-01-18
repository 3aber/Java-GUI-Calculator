class Variable extends Expr {
  String n;
  public int IntNum(){
  	return 0;
  }
  public double FloatNum(){
  	return 0;
  }
  Variable(String x) { n = x; }
  public String toString() { return n; }
  public boolean isGround() { return true; }
}
