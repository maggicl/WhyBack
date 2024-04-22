package byteback.whyml;

import java.util.Comparator;
import java.util.List;

@FunctionalInterface
public interface ListComparator<T> extends Comparator<List<T>> {
	int compareElement(T o1, T o2);

	default int compare(List<T> o1, List<T> o2) {
		final int thisSize = o1.size();
		final int otherSize = o2.size();

		int i = 0;
		for (; i < Math.min(thisSize, otherSize); i++) {
			int result = compareElement(o1.get(i), o2.get(i));
			if (result != 0) return result;
		}

		if (i == thisSize && thisSize < otherSize) {
			return -1;
		} else if (i == otherSize && thisSize > otherSize) {
			return 1;
		} else {
			return 0;
		}
	}
}
