package track;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Tracking {
	
	List<TrackingRow> recordlist = new ArrayList<TrackingRow>();

	public void insertRecord(TrackingRow newRecord) {
		int i=0;
		for(TrackingRow r : recordlist) {
			if(newRecord.range.lo < r.range.lo) {
				recordlist.add(i, newRecord);
				return;
			}
			i++;
		}
		recordlist.add(newRecord);
	}
	
	public void deleteRecord(TrackingRow record) {
		int i=0;
		
		for(TrackingRow listrecord : recordlist) {
			if(record == listrecord) {
				recordlist.remove(i);
				break;
			}
			i++;
		}
	}
	
	public void splitRecordList(TrackingRow part1, TrackingRow part2)
	{
		
		int i=0;	
		for(TrackingRow listrecord : recordlist)
		{
			if(part1.range.lo == listrecord.range.lo && part2.range.hi == listrecord.range.hi) {
				recordlist.remove(i);
				recordlist.add(i, part1);
				recordlist.add(i+1, part2);
				break;
			}
			i++;
		}
	}
	
	public void removeElements(int start, int end) {
		for (int i = start; i < end; i++) {
			recordlist.remove(i);
		}
	}

	public void mergeRecords() {
		int j = 0;
		TrackingRow next;
		TrackingRow current;
		for ( int i = 0; i < recordlist.size(); i++ ) {
			j = i + 1;
			current = recordlist.get(i);
			next = recordlist.get(j);
			while ( (current.statusCode.equals(next.statusCode) ) && (current.transferCode == next.transferCode) ) {
				current.range.hi = next.range.hi;
				next.invalid = true;
				j++;
				next = recordlist.get(j);
			}
			removeElements(i, j);
		}

	}
	
	public void displayRecordList()
	{
		for(TrackingRow listrecord : recordlist)
		{
			if(!listrecord.invalid)
			{
				System.out.println(listrecord.range.lo + " " + listrecord.range.hi + " " + listrecord.statusCode + " " + listrecord.transferCode);
			}
		}
	}
	

	public void insertingIntoRecordList(TrackingRow record)

	{
		TrackingRow previousRecord;
		TrackingRow listrecord = null;
		TrackingRow nextRecord;
		
		if(recordlist.size() == 0)
		{
			System.out.println("First insert");
			insertRecord(record);
			return;
		}
		
//		int positiontoCompare=0;
		boolean insertAtEnd=true;
		
		for(TrackingRow list : recordlist)
		{
			listrecord = list;
			if(record.range.lo >= listrecord.range.lo || record.range.hi >= listrecord.range.hi)
			{
				insertAtEnd = false;
				break;
			}
			
		}
		
//		if(insertAtEnd)
//		{
//			System.out.println("Insert at end");
//			insertRecord(record);
//		}
//		else
//		{
			String decision = listrecord.range.classify(record.range);
			switch(decision)
			{
				case "SAME":
					System.out.println("Same");
					listrecord.transferCode = record.transferCode;
					listrecord.statusCode = record.statusCode;
					break;
					
				case "SUPERSET":
					System.out.println("Superset");
					previousRecord = new TrackingRow(listrecord.range.lo, record.range.lo - 1, listrecord.statusCode, listrecord.transferCode);
					nextRecord = new TrackingRow(record.range.hi + 1, listrecord.range.hi, listrecord.statusCode, listrecord.transferCode);
					deleteRecord(listrecord);
					insertRecord(previousRecord);
					insertRecord(nextRecord);
					insertRecord(record);
					
					break;
							
				case "LESSDISJOINT":
					System.out.println("Less Disjoint");
					insertRecord(record);
					break;
					
				case "MOREDISJOINT":
					System.out.println("More disjoint");
					insertRecord(record);
					break;
				
				case "LESSOVERLAP":
					System.out.println("Less overlap");
					previousRecord = new TrackingRow(listrecord.range.lo, record.range.lo - 1, listrecord.statusCode, listrecord.transferCode);
					insertRecord(previousRecord);
					insertRecord(record);
					deleteRecord(listrecord);
					break;
				
				case "MOREOVERLAP":
					System.out.println("More overlap");
					nextRecord = new TrackingRow(record.range.hi + 1, listrecord.range.hi, listrecord.statusCode, listrecord.transferCode);
					deleteRecord(listrecord);
					insertRecord(record);
					insertRecord(nextRecord);
					break;
				default:
					System.out.println("Insert at end");
					insertRecord(record);
					break;
			}
//		}
				
			
			
	}
	
	public TrackingRow processInput(String line)
	{
		
		TrackingRow newRow = new TrackingRow();
		
		String[] recordInfo = line.split(" ");
	
		int low = Integer.parseInt(recordInfo[0]);
		int high = Integer.parseInt(recordInfo[1]);
		int transferCode = Integer.parseInt(recordInfo[3]);
		
		newRow.range =  new Range(low, high);
		newRow.statusCode = recordInfo[2];
		newRow.transferCode = transferCode;	
		
		return newRow;
	}
	
	public void readInput()
	{
		Scanner input = new Scanner(System.in);
		
		String nameOfTestCase = null;
		
		while(!(nameOfTestCase= input.nextLine()).equals("END"))
		{
			
			String information = null;
		
			while(!(information=input.nextLine()).equals("0"))
			{
				TrackingRow newRecord = processInput(information);
				insertingIntoRecordList(newRecord);
			}
		
			System.out.println(nameOfTestCase+" OUTPUT");
			displayRecordList();
			recordlist = new ArrayList<TrackingRow>();
		}
	}
	
}