package data.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.inject.Singleton;

import data.DataProvider;
import data.bean.ChapterBox;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ChapterBoxProvider extends DataProvider<ChapterBox> {

	private static final Logger logger = LoggerFactory.getLogger(ChapterBoxProvider.class);

	/** userId,章节ID,ChapterBox **/
	HashBasedTable<Long, Integer, ChapterBox> boxes = HashBasedTable.create();

	public static ChapterBoxProvider getInst() {
		return BeanManager.getBean(ChapterBoxProvider.class);
	}

	public ChapterBoxProvider() {
		super("p_chapter_box");
	}

	@Override
	protected Class<ChapterBox[]> getClassType() {
		return ChapterBox[].class;
	}

	@Override
	protected Map<String, Object> loadFilter() {
		return null;
	}

	@Override
	protected ChapterBox convertDBResult2Bean(ASObject o) {
		ChapterBox bean = new ChapterBox();
		bean.setId(o.getLong("id"));
		bean.setUserId(o.getInt("userId"));
		bean.setChapterId(o.getInt("chapterId"));
		bean.setBox1(o.getInt("box1"));
		bean.setBox2(o.getInt("box2"));
		bean.setBox3(o.getInt("box3"));
		return bean;
	}

	@Override
	protected ASObject convertBean2DbData(ChapterBox bean) {
		ASObject o = new ASObject();
		o.put("id", bean.getId());
		o.put("userId", bean.getUserId());
		o.put("chapterId", bean.getChapterId());
		o.put("box1", bean.getBox1());
		o.put("box2", bean.getBox2());
		o.put("box3", bean.getBox3());
		return o;
	}

	@Override
	public void init() {
		getAllBean().forEach(e -> {
			boxes.put(e.getUserId(), e.getChapterId(), e);
		});
	}

	public ChapterBox getById(long userId, int chapterId) {
		return boxes.get(userId, chapterId);
	}

	public void update(long userId, int chapterId, int box1, int box2, int box3) {
		ChapterBox box = boxes.get(userId, chapterId);
		if (box == null) {
			return;
		}
		if (box1 != -1) {
			box.setBox1(box1);
		}
		if (box2 != -1) {
			box.setBox2(box2);
		}
		if (box3 != -1) {
			box.setBox3(box3);
		}
		udp(box);
	}

	public ChapterBox add(long userId, int chapterId, int box1, int box2, int box3) {
		ChapterBox box = boxes.get(userId, chapterId);
		if (box != null) {
			return box;
		}
		box = new ChapterBox();
		box.setUserId(userId);
		box.setChapterId(chapterId);
		box.setBox1(box1);
		box.setBox2(box2);
		box.setBox3(box3);
		boxes.put(userId, chapterId, box);
		insert(box);
		return box;
	}

}
