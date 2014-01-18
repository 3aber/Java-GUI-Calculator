class Times extends BinaryOp {
  Times(Expr x, Expr y) { left = x; right = y; }
  public String toString() { 
    // we are the context
    return super.toString(this, " * "); }
  public boolean isSame(Expr e) { return e instanceof Times; }
  
  public int IntNum(){
  	int num1 = Integer.parseInt(left.toString());
  	int num2 = Integer.parseInt(right.toString());
  	return num1*num2;
  }
  
  public double FloatNum(){
  	double num1 = Double.parseDouble(left.toString());
  	double num2 = Double.parseDouble(right.toString());
  	return num1*num2;
  }
}
