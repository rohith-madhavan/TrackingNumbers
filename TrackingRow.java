package track;

public class TrackingRow {
	String statusCode;
	int transferCode;
	Range range;
	boolean invalid;
	
	TrackingRow()
	{
		invalid = false;
	}
	
	TrackingRow(int lo, int hi, String statusCode, int transferCode)
	{
		invalid = false;
		range = new Range(lo, hi);
		this.statusCode = statusCode;
		this.transferCode = transferCode;
	}
}