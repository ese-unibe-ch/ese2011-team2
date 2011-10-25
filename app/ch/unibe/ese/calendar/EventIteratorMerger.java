package ch.unibe.ese.calendar;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Merges multiple Iterator<CalendarEvent> into one, assuming these iterators
 * are chronological by start date this Iterator will be so too
 * 
 */
public class EventIteratorMerger implements Iterator<CalendarEvent> {

	private List<Iterator<CalendarEvent>> baseIterators;
	private CalendarEvent[] nextFromBase;
	private CalendarEvent next;
	private StartDateComparator startDateComparator = new StartDateComparator();

	public EventIteratorMerger(Iterator<CalendarEvent>... baseIterators) {
		this(Arrays.asList(baseIterators));
	}

	public EventIteratorMerger(List<Iterator<CalendarEvent>> baseIterators) {
		if (baseIterators.size() < 2) {
			throw new IllegalArgumentException(
					"must merge at least 2 base iterator");
		}
		this.baseIterators = baseIterators;
		nextFromBase = new CalendarEvent[baseIterators.size()];
		for (int i = 0; i < baseIterators.size(); i++) {
			Iterator<CalendarEvent> baseIter = baseIterators.get(i);
			if (baseIter.hasNext()) {
				nextFromBase[i] = baseIter.next();
			} else {
				// just to be explicit
				nextFromBase[i] = null;
			}
		}
		prepareNext();
	}


	private void prepareNext() {
		next = null;
		int currentEarliest = 0;
		while (nextFromBase[currentEarliest] == null) {
			currentEarliest++;
			if (currentEarliest == nextFromBase.length) {
				return;
			}
		}
		for (int i = (currentEarliest + 1); i < nextFromBase.length; i++) {
			if (nextFromBase[i] == null) {
				continue;
			}
			if (startDateComparator.compare(nextFromBase[i],
					nextFromBase[currentEarliest]) < 0) {
				currentEarliest = i;
			}
		}
		next = nextFromBase[currentEarliest];
		if (baseIterators.get(currentEarliest).hasNext()) {
			nextFromBase[currentEarliest] = baseIterators.get(currentEarliest)
					.next();
		} else {
			nextFromBase[currentEarliest] = null;
		}
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public CalendarEvent next() {
		CalendarEvent result = next;
		prepareNext();
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("not implemented");
	}

}
