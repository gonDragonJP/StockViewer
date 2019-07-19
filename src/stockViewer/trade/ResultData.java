package stockViewer.trade;

import java.util.ArrayList;

public class ResultData {
	
	public double winRate, aveProfit, aveLoss, profitFactor;
	public int winNumber, loseNumber, maxRowWin, maxRowLose, totalProfit, maxDrawDown;

	public void calc(ArrayList<TradeData> clearingPointList) {
		
		init();
		
		int tradeNumber = clearingPointList.size();
		if(tradeNumber ==0) return;
		
		int sumProfit =0, sumLoss = 0, rowWin = 0, rowLose = 0, maxAcount = 0;
		
		for(TradeData e: clearingPointList) {
			
			if(e.profit >0) {
				winNumber +=1;		sumProfit += e.profit;
				maxRowWin = Math.max(maxRowWin, ++rowWin);
				rowLose = 0;
			}
			if(e.profit <0) {
				loseNumber +=1;	sumLoss += e.profit;
				maxRowLose = Math.max(maxRowLose, ++rowLose);
				rowWin = 0;
			}
			
			totalProfit += e.profit;
			maxAcount = Math.max(maxAcount, e.acount);
			maxDrawDown = Math.max(maxDrawDown, maxAcount - e.acount);
		}
		
		if(winNumber >0)	aveProfit = sumProfit / winNumber;
		if(loseNumber >0)	aveLoss = -sumLoss / loseNumber;
		winRate = (double)winNumber / tradeNumber * 100;
		profitFactor = aveProfit * winRate / (aveLoss *(100- winRate));
		
		winRate = Math.round(winRate *10) / 10;
		profitFactor = Math.round(profitFactor*100) / 100d;
	}
	
	private void init() {
		
		winRate = aveProfit = aveLoss = profitFactor = 0;
		winNumber = loseNumber = maxRowWin = maxRowLose = totalProfit = maxDrawDown =0;
	}
}
