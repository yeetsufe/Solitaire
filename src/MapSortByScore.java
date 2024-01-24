/**Name: Jim Wu
 * Date: 1/25/2022
 * Description: This class is responsible for creating a comparator interface to sort cards in the leaderboard by score using the String key set.
 */
import java.util.Comparator;
import java.util.TreeMap;

public class MapSortByScore implements Comparator <String>{
	TreeMap <String,Integer> map; //stores a reference of the leaderboard
	
	/* Description: MapSortByScore class constructor, I have decided to create a constructor in order for the interface to access the leaderboard map in the Driver
	 * class
	 * Parameters: TreeMap <String,Integer> map - the leaderboard map
	 */
	public MapSortByScore(TreeMap <String,Integer> map) {
		this.map = map;
	}
	
	@Override
	/* Description: Overwritten Comparator interface involving two string keys from the leaderboard.
	 * Parameters: String o1, String o2 - keys of the leaderboard scores being compared
	 */
	public int compare(String o1, String o2) {
		return this.map.get(o2)-this.map.get(o1);
	}
}
