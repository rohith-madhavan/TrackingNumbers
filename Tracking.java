package track;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Tracking {
	
	List<TrackingRow> recordlist = new ArrayList<TrackingRow>();

	public void insertRecord(TrackingRow newRecord) {
		int i=0;
		
		for(TrackingRow listrecord : recordlist) {
			if(newRecord.range.lo<listrecord.range.lo) {
				recordlist.add(i, newRecord);
				break;
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
				System.out.println(" "+listrecord.range.hi+" - "+listrecord.range.lo+" -- "+listrecord.statusCode+" -- "+listrecord.transferCode);
			}
		}
	}
	

	public void insertingIntoRecordList(TrackingRow record)

	{
		TrackingRow previousRecord;
		TrackingRow listrecord = null;
		TrackingRow nextRecord;
		if(recordlist.size()==0)
		{
			insertRecord(record);
		}
		
		int positiontoCompare=0;
		boolean insertAtEnd=true;
		
		for(TrackingRow list : recordlist)
		{
			listrecord = list;
			if(record.range.lo>listrecord.range.lo || record.range.hi>listrecord.range.hi)
			{
				insertAtEnd=false;
				break;
			}
			
		}
		
		if(insertAtEnd)
		{
			insertRecord(record);
		}
		else
		{
			String decision = listrecord.range.classify(record.range);
			switch(decision)
			{
				case "SAME":
					listrecord.transferCode = record.transferCode;
					listrecord.statusCode = record.statusCode;
					break;
					
				case "SUPERSET":
					
					previousRecord = new TrackingRow(listrecord.range.lo, record.range.lo - 1, listrecord.statusCode, listrecord.transferCode);
					nextRecord = new TrackingRow(record.range.hi + 1, listrecord.range.hi, listrecord.statusCode, listrecord.transferCode);
					deleteRecord(listrecord);
					insertRecord(previousRecord);
					insertRecord(nextRecord);
					insertRecord(record);
					
					break;
							
				case "LESSDISJOINT":
					insertRecord(record);
					break;
					
				case "MOREDISJOINT":
					insertRecord(record);
					break;
				
				case "LESSOVERLAP":
					previousRecord = new TrackingRow(listrecord.range.lo, record.range.lo - 1, listrecord.statusCode, listrecord.transferCode);
					insertRecord(previousRecord);
					insertRecord(record);
					deleteRecord(listrecord);
					break;
				
				case "MOREOVERLAP":
					nextRecord = new TrackingRow(record.range.hi + 1, listrecord.range.hi, listrecord.statusCode, listrecord.transferCode);
					deleteRecord(listrecord);
					insertRecord(record);
					insertRecord(nextRecord);
					break;
			}
		}
				
			
			
	}
	
	public TrackingRow processInput(String recordinformation)
	{
		TrackingRow newRecord = new TrackingRow();
		
		String[] informationaboutrecord = recordinformation.split(" ");
	
		newRecord.range =  new Range(new Integer(informationaboutrecord[0]),new Integer(informationaboutrecord[1]));
		newRecord.statusCode = informationaboutrecord[2];
		newRecord.transferCode = Integer.parseInt(informationaboutrecord[3]);
	
		
		return newRecord;
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
		
			System.out.println(nameOfTestCase+" OUTPUT >> ");
			displayRecordList();
			recordlist = new ArrayList<TrackingRow>();
		}
	}
	
}