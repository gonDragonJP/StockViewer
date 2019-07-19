package stockViewer.trade;

import java.util.ArrayList;

public class TradeDataList extends ArrayList<TradeData>{

	public boolean add(TradeData e) {
		
		boolean result = super.add(e);
		
		calcList();
		
		return result;
	}
	
	public ArrayList<TradeData> getClearingPointList() {
		
		ArrayList<TradeData> clearingPointList = new ArrayList<>();
		
		for(TradeData e: this) {
			
			if(e.sumUnit == 0) clearingPointList.add(e);
		}
		
		return clearingPointList;
	}
	
	private void calcList() {
		
		int sumUnit =0, acount =0;
		int preBalancedAcount =0;
		
		for(TradeData e: this) {
			
			sumUnit = e.getSumUnit(sumUnit);
			e.sumUnit = sumUnit;
			
			acount = e.getAcount(acount);
			e.acount = acount;
			
			e.setProfit(preBalancedAcount);
			
			if(sumUnit==0) {
				preBalancedAcount = acount;
			}
		}
	}
}
