class Minus extends Expr { // NOT BinaryOp!
  Expr left;
  Expr right;
  Minus(Expr x, Expr y) { left = x; right = y; }
  public String toString() { 
    return betweenParens(left) + " - " + betweenParens(right); }
  public boolean isGround() { return false; }
  
  public int IntNum(){
  	int num1 = Integer.parseInt(left.toString());
  	int num2 = Integer.parseInt(right.toString());
  	return num1-num2;
  }
  
  public double FloatNum(){
  	double num1 = Double.parseDouble(left.toString());
  	double num2 = Double.parseDouble(right.toString());
  	return num1-num2;
  }
}
