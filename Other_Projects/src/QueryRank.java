import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryRank
{
	private ArrayList<GoogleSearch> gsList;
	
	public QueryRank()
	{
		gsList = new ArrayList<GoogleSearch>();
	}
	
	public QueryRank(List<String> queryList)
	{
		gsList = new ArrayList<GoogleSearch>();
		for (int i = 0; i < queryList.size(); i++)
		{
			gsList.add(new GoogleSearch(queryList.get(i)));
		}
	}
	
	public void addQuery(String query)
	{
		gsList.add(new GoogleSearch(query));
	}
	
	public ArrayList<String> getRankedQueriesFormula1()
	{
		ArrayList<String> rankedList = new ArrayList<String>();
		
		for (int i = 0; i < gsList.size(); i++)
		{
			gsList.get(i).setScore(gsList.get(i).getTotalResults());
		}
		Collections.sort(gsList);
		
		for (int i = 0; i < gsList.size(); i++)
		{
			rankedList.add(gsList.get(i).getQuery());
		}
		
		return rankedList;
	}
	
	public ArrayList<String> getRankedQueriesFormula2(String originalQuery)
	{
		ArrayList<String> rankedList = new ArrayList<String>();
		GoogleSearch originalGs = new GoogleSearch(originalQuery);
		ArrayList<String> originalResults = originalGs.getTopK();
		
		for (int i = 0; i < gsList.size(); i++)
		{
			int score = gsList.get(i).getTotalResults();
			int matches = 0;
			int matchRankSum = 0;
			ArrayList<String> reformulatedResults = gsList.get(i).getTopK();
			for (int j = 0; j < reformulatedResults.size(); j++)
			{
				for (int k = 0; k < originalResults.size(); k++)
				{
					if (reformulatedResults.get(j).equals(originalResults.get(k)))
					{
						matches++;
						// rank is set as 0, 1, ..., k-1
						// so if total results = 10 and there is a match in the last document,
						// then matchRankSum += 1
						matchRankSum += originalResults.size() - k;
					}
				}
			}
			if (matches == 0) score = 0;
			else score *= 1.0 / matches * matchRankSum;
			gsList.get(i).setScore(score);
		}
		Collections.sort(gsList);
		
		for (int i = 0; i < gsList.size(); i++)
		{
			rankedList.add(gsList.get(i).getQuery());
		}
		
		return rankedList;
	}
	
	@Override
	public String toString()
	{
		String str = "";
		
		for (int i = 0; i < gsList.size(); i++)
		{
			if (i == 0) str += "{ ";
			else str += "  ";
			
			str += gsList.get(i).getQuery() + " ( " + gsList.get(i).getTotalResults() + " ) ";
			
			if (i + 1 == gsList.size()) str += " }";
			else str += ",\n";
		}
		
		return str;
	}
}
