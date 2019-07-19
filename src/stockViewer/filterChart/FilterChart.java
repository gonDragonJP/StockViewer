package stockViewer.filterChart;

import stockViewer.stockdata.ChartData;

public class FilterChart {
	
	public enum Index{
		
		Nikkei255_ETF(1321);
		
		public int tickerCode;
		public ChartData chartData;
		
		Index(int tickerCode){
			
			this.tickerCode = tickerCode;
			this.chartData = new ChartData();
			refresh();
		}
		
		public void refresh() {
			
			this.chartData.loadDB(tickerCode);
		}
	}
}
