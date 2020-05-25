package manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import config.bean.ConfChapter;
import config.provider.ConfChapterProvider;
import data.bean.Chapter;
import data.provider.ChapterProvider;
import inject.BeanManager;

@Singleton
public class ChapterManager {

	private static final Logger logger = LoggerFactory.getLogger(ChapterManager.class);

	public static ChapterManager getInst() {
		return BeanManager.getBean(ChapterManager.class);
	}

	public void load() {
	}

	// 当前章节
	public int getChapterId(long playerId) {
		int chapterId = 1;
		for (ConfChapter e : ConfChapterProvider.getInst().getAllConfig()) {
			if (ChapterProvider.getInst().contains(playerId, e.getId())) {
				if (e.getChapterId() > chapterId) {
					chapterId = e.getChapterId();
				}
			}
		}
		return chapterId;
	}
	
	public void initChapterFrist(long playerId, int chapterId) {
		if (!ConfChapterProvider.getInst().isContain(chapterId)) {
			return;
		}
		ConfChapter conf = ConfChapterProvider.getInst().getSection(chapterId, 1);
		if (conf == null) {
			return;
		}
		ChapterProvider.getInst().addChapter(playerId, conf.getId(), 0, 0, 0);
	}

	public boolean isPassChapter(long playerId, int chapterId, int sectionId) {
		int star = 0;
		boolean pass = true;
		List<ConfChapter> confList = ConfChapterProvider.getInst().getChapter(chapterId);
		for (ConfChapter e : confList) {
			Chapter chapter = ChapterProvider.getInst().getById(playerId, e.getId());
			if (chapter == null || chapter.getPassTime() == 0) {
				pass = false;
				break;
			}
			star += chapter.getStarLv();
		}
		return pass && star >= 16;
	}

	public boolean initIfNoChapter(int gameType, long playerId, int chapterId, int sectionId) {
		if (!ConfChapterProvider.getInst().isContain(chapterId)) {
			return false;
		}
		if (sectionId == 8) {// 最后一关
			int starLv = 0;
			List<ConfChapter> confList = ConfChapterProvider.getInst().getChapter(gameType,chapterId);
			for (ConfChapter e : confList) {
				Chapter chapter = ChapterProvider.getInst().getById(playerId, e.getId());
				if (chapter != null) {
					starLv += chapter.getStarLv();
				}
			}
			if (starLv < 16) {// 本章星数小于16则不可通关
				return false;
			}
		}
		ConfChapter conf = ConfChapterProvider.getInst().nextChapter(gameType,chapterId, sectionId);
		if (conf == null) {
			return false;
		}
		ChapterProvider.getInst().addChapter(playerId, conf.getId(), 0, 0, 0);
		return true;
	}

}
