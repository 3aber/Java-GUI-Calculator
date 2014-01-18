class Integer2 extends Expr {
  int i;
  public int IntNum(){
  	return 0;
  }
  public double FloatNum(){
  	return 0; // do nothing
  }

  Integer2(int x) { i = x; }
  public String toString() { return Integer.toString(i) ; }
  public boolean isGround() { return true; }
}
