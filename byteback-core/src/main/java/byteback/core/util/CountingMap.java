package byteback.core.util;

import java.util.HashMap;
import java.util.Map;

public class CountingMap<K, V> extends HashMap<K, V> {

	private final Map<K, Integer> accessCount;

	public CountingMap() {
		this.accessCount = new HashMap<>();
	}

	@Override
	public V put(K key, V value) {
		accessCount.put(key, accessCount.getOrDefault(key, 0) + 1);

		return super.put(key, value);
	}

	public Map<K, Integer> getAccessCount() {
		return accessCount;
	}

}
