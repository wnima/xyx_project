//package db;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ScheduledFuture;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import actor.CenterActorManager;
//import actor.IActor;
//import actor.IRunner;
//import db.cache.DbCache;
//import db.cache.MulSearch;
//import db.data.DBAction;
//import db.data.ILoadResult;
//import util.ASObject;
//import util.LogHelper;
//import util.Pair;
//
//public class DataManager {
//	private static final Logger logger = LoggerFactory.getLogger(DataManager.class);
//	private static DataManager instance = new DataManager();
//
//	public static int SAVE_CHAR_PER_SEC = 200;
//
//	private DataManager() {
//	}
//
//	public static DataManager getInst() {
//		return instance;
//	}
//
//	@SuppressWarnings("unused")
//	private boolean ready = false;
//
//	private Map<DataType, Pair<DbCache, MulSearch<ILoadResult>>> dataCacheMap;
//
//	private ScheduledFuture<?> checkTimer = null;
//
//	public boolean init(int max_num, int clean_size) {
//		dataCacheMap = new HashMap<>();
//		DbCache dataCache = new DbCache();
//		int node_size = max_num + 100;
//		int hash_size = node_size * 3 / 4;
//		if (false == dataCache.init(hash_size, node_size, clean_size)) {
//			return false;
//		}
//		MulSearch<ILoadResult> queryCache = new MulSearch<ILoadResult>();
//		if (false == queryCache.init(hash_size)) {
//			return false;
//		}
//
//		dataCacheMap.put(DataType.PLAYER, new Pair<>(dataCache, queryCache));
//		ready = true;
//		return true;
//	}
//
//	public CacheData newCacheDataByType(DataType type) {
//		if (type == DataType.PLAYER) {
//			return new CharData();
//		}
//		return null;
//	}
//
//	public boolean loadAllData() {
//		return true;
//	}
//
//	public boolean start() {
//		checkTimer = CenterActorManager.getDBTimer().register(1 * 1000L, 1 * 1000L, () -> {
//			for (int i = 0; i < SAVE_CHAR_PER_SEC; i++) {
//				for (Map.Entry<DataType, Pair<DbCache, MulSearch<ILoadResult>>> dataTypePairEntry : dataCacheMap.entrySet()) {
//					Object data = dataTypePairEntry.getValue().getLeft().getNextDirtyNodeAndClean();
//					if (data == null) {
//						break;
//					}
//					final CacheData cacheData = (CacheData) data;
//					int dataId = cacheData.getDataId();
//					logger.info("[Saving >>>>>>>>>>>>>>>>>>>>>>>>>>>>> TO DB:{} id:{}", cacheData.getClass().getName(), dataId);
//					IActor actor = CenterActorManager.getDbActor(dataId);
//					actor.put(() -> {
//						try {
//							synchronized (cacheData) {
//								cacheData.saveAll();
//							}
//						} catch (Exception ex) {
//							LogHelper.ERROR.error(ex.getMessage(), ex);
//						}
//						return null;
//					});
//				}
//			}
//		}, CenterActorManager.getDbCheckActor(), "DB_CHECK");
//		return true;
//	}
//
//	public void stop() {
//		if (checkTimer != null) {
//			checkTimer.cancel(true);
//		}
//		checkTimer = null;
//	}
//
//	public void flush() {
//		flush(DataType.PLAYER);
//	}
//
//	/**
//	 * 将数据写到数据库
//	 * 
//	 * @param dataType
//	 */
//	private void flush(DataType dataType) {
//		List<Object> dataList = getCache(dataType).getAllData();
//		logger.info("FLUSH {} BEGIN with total size  = {} ", dataType, dataList.size());
//		int flushCnt = 0;
//		for (Object data : dataList) {
//			if (data == null) {
//				logger.info("FLUSH ING with null data, continue");
//				continue;
//			}
//			CacheData cacheData = (CacheData) data;
//			try {
//				synchronized (cacheData) {
//					cacheData.saveAll();
//					flushCnt++;
//					logger.info("FLUSH dataType:{] dataId：{}", dataType, cacheData.dataId);
//				}
//			} catch (Exception e) {
//				LogHelper.ERROR.error(e.getMessage(), e);
//			}
//			logger.info("FLUSH ING with the {}  data finished", flushCnt);
//		}
//		logger.info("FLUSH END ");
//	}
//
//	/**
//	 * 保存
//	 * 
//	 * @param type
//	 * @param dataId
//	 * @param action
//	 * @param data
//	 */
//	public void saveModule(DataType type, int dataId, DBAction action, ASObject data) {
//		CenterActorManager.getDbCheckActor().put(() -> {
//			DbCache dataCache = dataCacheMap.get(type).getLeft();
//			CacheData cacheData = (CacheData) dataCache.query(dataId);
//			if (cacheData == null) {
//				logger.info("saveModule the data is null in cache  why tableName:{} dataId:{}", type.getTableName(), dataId);
//				return null;
//			}
//			cacheData.updateModuleData(action, data);
//			dataCache.setDirty(dataId);
//			return null;
//		});
//	}
//
//	/**
//	 * 保存并删除
//	 * 
//	 * @param type
//	 * @param dataId
//	 * @param action
//	 * @param data
//	 */
//	public void saveAndRemoveFromCache(DataType type, int dataId, DBAction action, ASObject data) {
//		CenterActorManager.getDbCheckActor().put(() -> {
//			DbCache dataCache = dataCacheMap.get(type).getLeft();
//			CacheData cacheData = (CacheData) dataCache.query(dataId);
//			if (cacheData == null) {
//				logger.info("saveModule the data is null in cache  why tableName:{} dataId:{}", type.getTableName(), dataId);
//				return null;
//			}
//			cacheData.updateModuleData(action, data);
//			dataCache.hashDel(dataId);
//			return null;
//		});
//	}
//
//	/**
//	 * 刪除
//	 * 
//	 * @param type
//	 * @param dataId
//	 * @param action
//	 * @param data
//	 */
//	public void deleteModule(DataType type, int dataId, DBAction action, ASObject data) {
//		CenterActorManager.getDbCheckActor().put(() -> {
//			DbCache dataCache = dataCacheMap.get(type).getLeft();
//			CacheData cacheData = (CacheData) dataCache.query(dataId);
//			if (cacheData == null) {
//				logger.info("the data is null in cache  why type:{} dataId:{}", action.getValue(), dataId);
//				return null;
//			}
//			cacheData.deleteModuleData(action, data);
//			return null;
//		});
//	}
//
//	/**
//	 * 获取缓存中的数据
//	 * 
//	 * @param type
//	 * @param dataId
//	 * @param module
//	 * @return
//	 */
//	public ASObject getModule(DataType type, int dataId, DBAction module) {
//		DbCache cache = dataCacheMap.get(type).getLeft();
//		if (cache == null) {
//			return null;
//		}
//		Object data = cache.query(dataId);
//		if (data == null) {
//			return null;
//		}
//		CacheData cacheData = (CacheData) data;
//		return cacheData.getModuleData(module);
//	}
//
//	/**
//	 * 这个地方不需要线程,改一下
//	 * 
//	 * @param type
//	 * @param dataId
//	 * @param callBack
//	 */
//	public void loadData(DataType type, final int dataId, ILoadResult callBack) {
//		logger.info("loadData dataType:{} dataId:{} callBack:{}", type, dataId, callBack);
//		MulSearch<ILoadResult> queryCache = dataCacheMap.get(type).getRight();
//		DbCache dataCache = dataCacheMap.get(type).getLeft();
//		synchronized (queryCache) {
//			MulSearch.LinkNode<ILoadResult> iterator = queryCache.Search(dataId);
//			if (false == queryCache.AddKey(dataId, callBack)) {
//				if (callBack != null) {
//					callBack.onResult(null, "queryCache.AddKey" + dataId);
//				}
//				logger.info("Char={} queryCache.AddKey fail, cache size={}  clean_size={} dirty_size={}", dataId, dataCache.getCacheSize(), dataCache.getCleanSize(), dataCache.getDirtySize());
//				return;
//			}
//			if (iterator == null) {
//				if (false == dataCache.occupy(dataId)) {
//					queryCache.DeleteKey(dataId);
//					if (callBack != null) {
//						callBack.onResult(null, "dataCache.occupy" + dataId);
//					}
//					logger.info("Char={} dataCache.occupy fail, cache size ={} clean_size={} dirty_size={}", dataId, dataCache.getCacheSize(), dataCache.getCleanSize(), dataCache.getDirtySize());
//					return;
//				}
//				CenterActorManager.getLoadActor(dataId).put(new IRunner() {
//					@Override
//					public Object run() {
//						CacheData cacheData = newCacheDataByType(type);
//						cacheData.init(dataId);
//						boolean ret = false;
//						try {
//							ret = cacheData.loadAll();
//						} catch (Exception e) {
//							LogHelper.ERROR.error(e.getMessage(), e);
//							ret = false;
//						}
//						if (ret) {
//							cacheData = (CacheData) dataCache.setData(dataId, cacheData);
//						} else {
//							cacheData = (CacheData) dataCache.setData(dataId, null);
//							logger.info("Char={} load fail, cache size={} clean_size={} dirty_size={}", dataId, dataCache.getCacheSize(), dataCache.getCleanSize(), dataCache.getDirtySize());
//						}
//						synchronized (queryCache) {
//							MulSearch.LinkNode<ILoadResult> iterator = queryCache.Search(dataId);
//							while (iterator != null) {
//								ILoadResult callback = iterator.data;
//								if (callback != null) {
//									callback.onResult(cacheData, "" + dataId);
//								}
//								iterator = iterator.same_next;
//							}
//							queryCache.DeleteKey(dataId);
//						}
//						return null;
//					}
//				});
//			} else {
//				// do nothing, 已经加入链了
//			}
//		}
//	}
//
//	public DbCache getCache(DataType type) {
//		return dataCacheMap.get(type).getLeft();
//	}
//}
