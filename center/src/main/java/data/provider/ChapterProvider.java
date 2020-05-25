package data.provider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.Chapter;
import inject.BeanManager;
import mxw.RankManager;
import util.ASObject;

@Singleton
public class ChapterProvider extends DataProvider<Chapter> {

	private static final Logger logger = LoggerFactory.getLogger(ChapterProvider.class);

	/** 关卡ID,排行纪录(3条) **/
	Map<Integer, List<Chapter>> ranks = new ConcurrentHashMap<>();
	/** userId,关卡ID,最高纪录 **/
	HashBasedTable<Long, Integer, Chapter> record = HashBasedTable.create();

	public static ChapterProvider getInst() {
		return BeanManager.getBean(ChapterProvider.class);
	}

	public ChapterProvider() {
		super("p_chapter");
	}

	@Override
	protected Class<Chapter[]> getClassType() {
		return Chapter[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected Chapter convertDBResult2Bean(ASObject o) {
		Chapter bean = new Chapter();
		bean.setId(o.getLong("id"));
		bean.setUserId(o.getInt("userId"));
		bean.setCombatId(o.getInt("combatId"));
		bean.setStarLv(o.getInt("starLv"));
		bean.setPassTime(o.getInt("passTime"));
		bean.setCreateTime(o.getInt("createTime"));
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(Chapter bean) {
		ASObject o = new ASObject();
		o.put("id", bean.getId());
		o.put("userId", bean.getUserId());
		o.put("combatId", bean.getCombatId());
		o.put("starLv", bean.getStarLv());
		o.put("passTime", bean.getPassTime());
		o.put("createTime", bean.getCreateTime());
		return o;
	}

	@Override
	public void init() {
		getAllBean().forEach(e -> {
			record.put(e.getUserId(), e.getCombatId(), e);
			updateRank(e);
		});
		RankManager.getInst().calc();
	}

	public Chapter getById(long userId, int combatId) {
		return record.get(userId, combatId);
	}

	/**
	 * 
	 * @param rank
	 */
	public void updateRank(Chapter chapter) {
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

	public List<Chapter> getRankList(int combat) {
		List<Chapter> rankList = new ArrayList<Chapter>();
		List<Chapter> sortList = ranks.get(combat);
		if (sortList == null) {
			return rankList;
		}

		int size = sortList.size();
		for (int i = 0; i < 3 && i < size; i++) {
			rankList.add(sortList.get(i));
		}
		return rankList;
	}

	public void updateRecord(long userId, int combatId, int starLv, long passTime, int createTime) {
		Chapter chapter = record.get(userId, combatId);
		if (chapter == null) {
			logger.info("chapter isNull playerId:{} combatId:{}", userId, combatId);
			return;
		}
		if (passTime <= 0) {// 未通关
			logger.info("chapter passTime is zero playerId:{} combatId:{}", userId, combatId);
			return;
		}

		boolean flag = false;
		if (starLv > chapter.getStarLv()) {
			chapter.setStarLv(starLv);
			flag = true;
		}

		if (chapter.getPassTime() == 0 || passTime < chapter.getPassTime()) {
			chapter.setPassTime(passTime);
			flag = true;
		}

		if (flag) {
			chapter.setCreateTime(createTime);
			updateRank(chapter);
			udp(chapter);// 添加到缓存中更新
		}
	}

	public void addChapter(long userId, int combatId, int starLv, long passTime, int createTime) {
		Chapter chapter = record.get(userId, combatId);
		if (chapter != null) {
			return;
		}
		chapter = new Chapter();
		chapter.setUserId(userId);
		chapter.setCombatId(combatId);
		chapter.setStarLv(starLv);
		chapter.setPassTime(passTime);
		chapter.setCreateTime(createTime);
		record.put(userId, combatId, chapter);
		insert(chapter);
	}

	public boolean contains(long userId, int combatId) {
		return record.contains(userId, combatId);
	}

	public Chapter getRecord(long userId, int combatId) {
		return record.get(userId, combatId);
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
}
