package comun;

public enum Color {
	red("#ff0000"), green("#00ff00"), blue("#0000ff");

	public String value;

	private Color() {
	}

	private Color(String i) {
		this.value = i;
	}
}
