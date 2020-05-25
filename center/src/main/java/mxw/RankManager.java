package mxw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import com.google.inject.Singleton;

import data.bean.Chapter;
import data.provider.ChapterProvider;

@Singleton
public class RankManager {

	private ReadWriteLock lock = new ReentrantReadWriteLock();
	// 总排行
	List<UserRank> rankList = new ArrayList<UserRank>();

	static RankManager inst = new RankManager();

	public static RankManager getInst() {
		return inst;
	}

	public List<UserRank> getRankList() {
		try {
			lock.readLock().lock();
			return rankList;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void calc() {
		List<Chapter> passList = ChapterProvider.getInst().getAllBean();
		Map<Long, Integer> starLvMap = passList.stream().collect(Collectors.groupingBy(Chapter::getUserId, Collectors.summingInt(Chapter::getStarLv)));
		Map<Long, Optional<Chapter>> combatMap = passList.stream().collect(Collectors.groupingBy(Chapter::getUserId, Collectors.maxBy(Comparator.comparing(Chapter::getCombatId))));
		Iterator<Entry<Long, Integer>> it = starLvMap.entrySet().iterator();
		try {
			lock.writeLock().lock();
			rankList.clear();

			while (it.hasNext()) {
				Entry<Long, Integer> b = it.next();
				if (b.getKey() == 0) {
					continue;
				}
				UserRank userRank = new UserRank(b.getKey(), b.getValue());
				Optional<Chapter> cp = combatMap.get(b.getKey());
				if (cp.isPresent()) {
					userRank.setCombatId(cp.get().getCombatId());
					userRank.setPassTime(cp.get().getPassTime());
					userRank.setCreateTime(cp.get().getCreateTime());
				}
				rankList.add(userRank);
			}
			Collections.sort(rankList, new ComparatorStar());
		} finally {
			lock.writeLock().unlock();
		}
	}

	public static void updRank(Map<Integer, List<Chapter>> ranks, Chapter chapter) {
		if (chapter.getPassTime() <= 0) {// 未通关
			return;
		}
		List<Chapter> sortList = ranks.get(chapter.getCombatId());
		if (sortList == null) {
			sortList = new ArrayList<Chapter>();
			ranks.put(chapter.getCombatId(), sortList);
		}

		if (sortList.isEmpty()) {
			sortList.add(chapter);
			return;
		}

		// 如果已经存在
		Optional<Chapter> opt = sortList.stream().filter(e -> e.getUserId() == chapter.getUserId()).findAny();
		if (opt.isPresent()) {// 已经在记录中,则不处理
			return;
		}

		// 数量小于3,则直接添加
		if (sortList.size() < 3) {
			sortList.add(chapter);
			return;
		}

		Optional<Chapter> o = sortList.stream().max(Comparator.comparing(Chapter::getPassTime));
		Chapter maxPassTimeChapter = o.get();
		long maxPassTime = maxPassTimeChapter.getPassTime();
		if (chapter.getPassTime() > maxPassTime) {
			return;
		}

		// 替换时间最长的
		int index = sortList.indexOf(maxPassTimeChapter);
		sortList.set(index, chapter);
	}

	public static void main(String[] args) {
		List<UserRank> list = new ArrayList<UserRank>();
		list.add(new UserRank(1, 30, 30, 1263));
		list.add(new UserRank(2, 30, 62, 123));
		list.add(new UserRank(3, 20, 40, 124));
		list.add(new UserRank(4, 30, 30, 123));
		list.add(new UserRank(5, 70, 30, 123));
		list.add(new UserRank(6, 12, 30, 123));

		Collections.sort(list, new ComparatorStar());
		System.out.println(list);
	}
}
