package config.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.HashBasedTable;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfChapter;
import inject.BeanManager;
import util.ASObject;
import util.Pair;

@Singleton
public class ConfChapterProvider extends BaseProvider<ConfChapter> {

	private Map<Pair<Integer, Integer>, List<ConfChapter>> chapters = new HashMap<Pair<Integer, Integer>, List<ConfChapter>>();
	private HashBasedTable<Pair<Integer, Integer>, Integer, ConfChapter> sections = HashBasedTable.create();

	public static ConfChapterProvider getInst() {
		return BeanManager.getBean(ConfChapterProvider.class);
	}

	public ConfChapterProvider() {
		super("", "conf_chapter");
	}

	@Override
	protected Class<ConfChapter[]> getClassType() {
		return ConfChapter[].class;
	}

	@Override
	protected ConfChapter convertDBResult2Bean(ASObject o) {
		ConfChapter conf = new ConfChapter();
		conf.setId(o.getInt("id"));
		conf.setChapterId(o.getInt("chapterId"));
		conf.setSectionId(o.getInt("sectionId"));
		conf.setMaxGold(o.getInt("maxGold"));
		conf.setPower(o.getInt("power"));
		conf.setGameTypeId(o.getInt("gameTypeId"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfChapter conf) {
		ASObject o = new ASObject();
		o.put("id", conf.getId());
		o.put("chapterId", conf.getChapterId());
		o.put("sectionId", conf.getSectionId());
		o.put("maxGold", conf.getMaxGold());
		o.put("power", conf.getPower());
		o.put("gameTypeId", conf.getGameTypeId());
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			int chapterId = e.getChapterId();
			int gameTypeId = e.getGameTypeId();
			Pair<Integer, Integer> key = new Pair<Integer, Integer>(gameTypeId, chapterId);
			List<ConfChapter> list = chapters.get(key);
			if (list == null) {
				list = new ArrayList<ConfChapter>();
				chapters.put(key, list);
			}
			list.add(e);

			sections.put(key, e.getSectionId(), e);
		});
	}

	public List<ConfChapter> getChapter(int gameTypeId,int chapterId) {
		return chapters.get(new Pair<Integer, Integer>(gameTypeId, chapterId));
	}

	public boolean isContain(int gameTypeId,int chapterId) {
		return chapters.containsKey(new Pair<Integer, Integer>(gameTypeId, chapterId));
	}

	public ConfChapter nextChapter(int gameTypeId,int chapterId, int sectionId) {
		if (sectionId == 8) {
			return getSection(gameTypeId,chapterId + 1, 1);
		}
		return getSection(gameTypeId,chapterId, sectionId + 1);
	}

	public ConfChapter getSection(int gameTypeId,int chapterId, int sectionId) {
		return sections.get(new Pair<Integer, Integer>(gameTypeId, chapterId), sectionId);
	}
}
