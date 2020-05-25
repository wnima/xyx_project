package manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.google.inject.Singleton;
import com.google.protobuf.InvalidProtocolBufferException;

import config.bean.ConfExplore;
import config.provider.ConfExploreProvider;
import data.bean.DbExtreme;
import data.bean.Extreme;
import data.provider.ExtremeProvider;
import inject.BeanManager;
import pb.CommonPb.AtkExtreme;
import pb.SerializePb.SerAtkExtreme;

/**
 * 极限挑战
 * 
 * @author Administrator
 *
 */

@Singleton
public class ExtremeDataManager {

	public Map<Integer, Extreme> recordMap = new HashMap<Integer, Extreme>();

	public Set<Integer> saveSet = new HashSet<>();

	public static ExtremeDataManager getInst() {
		return BeanManager.getBean(ExtremeDataManager.class);
	}

	@PostConstruct
	public void init() throws InvalidProtocolBufferException {
		List<DbExtreme> list = ExtremeProvider.getInst().getAllBean();
		for (int i = 0; i < list.size(); i++) {
			DbExtreme dbExtreme = list.get(i);
			Extreme extreme = dserExtreme(dbExtreme);
			recordMap.put(extreme.getExtremeId(), extreme);
		}

		Iterator<ConfExplore> it = ConfExploreProvider.getInst().getAllConfig().iterator();
		while (it.hasNext()) {
			ConfExplore staticExplore = (ConfExplore) it.next();
			if (staticExplore.getType() == 3) {
				Extreme extreme = recordMap.get(staticExplore.getExploreId());
				if (extreme == null) {
					extreme = new Extreme();
					extreme.setExtremeId(staticExplore.getExploreId());
					recordMap.put(staticExplore.getExploreId(), extreme);
					ExtremeProvider.getInst().insert(serExtreme(extreme));
				}
			}
		}
	}

	public DbExtreme serExtreme(Extreme extreme) {
		DbExtreme dbExtreme = new DbExtreme();
		dbExtreme.setExtremeId(extreme.getExtremeId());
		if (extreme.getFirst1() != null) {
			dbExtreme.setFirst1(extreme.getFirst1().toByteArray());
		} else {
			dbExtreme.setFirst1(AtkExtreme.newBuilder().build().toByteArray());
		}

		SerAtkExtreme.Builder builder = SerAtkExtreme.newBuilder();
		builder.addAllAtkExtreme(extreme.getLast3());
		dbExtreme.setLast3(builder.build().toByteArray());

		return dbExtreme;
	}

	public Extreme dserExtreme(DbExtreme dbExtreme) throws InvalidProtocolBufferException {
		Extreme extreme = new Extreme();
		extreme.setExtremeId(dbExtreme.getExtremeId());
		if (dbExtreme.getFirst1() != null) {
			extreme.setFirst1(AtkExtreme.parseFrom(dbExtreme.getFirst1()));
		}

		if (dbExtreme.getLast3() != null) {
			List<AtkExtreme> list = SerAtkExtreme.parseFrom(dbExtreme.getLast3()).getAtkExtremeList();
			if (list != null) {
				for (AtkExtreme atkExtreme : list) {
					extreme.getLast3().add(atkExtreme);
				}
			}
		}
		return extreme;
	}

	public void record(int extremeId, AtkExtreme atkExtreme) {
		Extreme extreme = recordMap.get(extremeId);
		if (extreme.getFirst1() == null || !extreme.getFirst1().hasAttacker()) {
			extreme.setFirst1(atkExtreme);
		}

		if (extreme.getLast3().size() >= 3) {
			extreme.getLast3().removeFirst();
		}

		extreme.getLast3().add(atkExtreme);

		saveSet.add(extremeId);
	}

	public Extreme getExtreme(int extremeId) {
		return recordMap.get(extremeId);
	}
}
