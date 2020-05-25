package util;

import java.util.List;
import java.util.Map;

import pb.util.PBBool;
import pb.util.PBFourInt;
import pb.util.PBFourIntLong;
import pb.util.PBFourLong;
import pb.util.PBFourLongInt;
import pb.util.PBInt;
import pb.util.PBIntList;
import pb.util.PBListInt;
import pb.util.PBListLongInt;
import pb.util.PBLong;
import pb.util.PBLongList;
import pb.util.PBLongStringLong;
import pb.util.PBPairInt;
import pb.util.PBPairIntIntList;
import pb.util.PBPairIntLong;
import pb.util.PBPairIntString;
import pb.util.PBPairIntStringList;
import pb.util.PBPairIntTriple;
import pb.util.PBPairList;
import pb.util.PBPairListList;
import pb.util.PBPairLong;
import pb.util.PBPairLongInt;
import pb.util.PBPairLongList;
import pb.util.PBPairString;
import pb.util.PBPairStringLong;
import pb.util.PBPairTripleList;
import pb.util.PBString;
import pb.util.PBStringList;
import pb.util.PBThreeLongInt;
import pb.util.PBTriple;
import pb.util.PBTripleIntLong;
import pb.util.PBTripleIntString;
import pb.util.PBTripleList;
import pb.util.PBTripleLong;
import pb.util.PBTripleString;

public class PBUtilHelper {

	public static PBInt pbInt(int value) {
		PBInt.Builder b = PBInt.newBuilder();
		b.setValue(value);
		return b.build();
	}

	public static PBLong pbLong(long value) {
		PBLong.Builder b = PBLong.newBuilder();
		b.setValue(value);
		return b.build();
	}

	public static PBString pbString(String value) {
		PBString.Builder b = PBString.newBuilder();
		b.setValue(value);
		return b.build();
	}

	public static PBBool pbBool(Boolean value) {
		PBBool.Builder b = PBBool.newBuilder();
		b.setValue(value);
		return b.build();
	}

	public static PBPairInt pbPairInt(int key, int value) {
		PBPairInt.Builder b = PBPairInt.newBuilder();
		b.setKey(key);
		b.setValue(value);
		return b.build();
	}

	public static PBPairLong pbPairLong(long key, long value) {
		PBPairLong.Builder b = PBPairLong.newBuilder();
		b.setKey(key);
		b.setValue(value);
		return b.build();
	}

	public static PBPairIntLong pbPairIntLong(Integer key, long value) {
		PBPairIntLong.Builder b = PBPairIntLong.newBuilder();
		b.setKey(key);
		b.setValue(value);
		return b.build();
	}

	public static PBPairIntString pbPairIntString(int key, String value) {
		PBPairIntString.Builder b = PBPairIntString.newBuilder();
		b.setKey(key);
		b.setValue(value);
		return b.build();
	}

	public static PBTripleIntString PBTripleIntString(int key, String value, String value2) {
		PBTripleIntString.Builder b = PBTripleIntString.newBuilder();
		b.setOne(key);
		b.setTwo(value);
		b.setThree(value2);
		return b.build();
	}

	public static PBPairIntStringList pbPairIntStringList(Map<Integer, String> values) {
		PBPairIntStringList.Builder b = PBPairIntStringList.newBuilder();
		values.forEach((e, f) -> {
			b.addList(pbPairIntString(e, f));
		});
		return b.build();
	}

	public static PBTriple pbTriple(int param1, int param2, int param3) {
		PBTriple.Builder b = PBTriple.newBuilder();
		b.setOne(param1);
		b.setTwo(param2);
		b.setThree(param3);
		return b.build();
	}

	public static PBTripleString pbTripleString(String param1, String param2, String param3) {
		PBTripleString.Builder b = PBTripleString.newBuilder();
		b.setOne(param1);
		b.setTwo(param2);
		b.setThree(param3);
		return b.build();
	}

	public static PBThreeLongInt pbThreeLongInt(long param1, int param2, int param3) {
		PBThreeLongInt.Builder b = PBThreeLongInt.newBuilder();
		b.setParam1(param1);
		b.setParam2(param2);
		b.setParam3(param3);
		return b.build();
	}

	public static PBTripleIntLong PbTripleIntLong(int param1, int param2, long param3) {
		PBTripleIntLong.Builder b = PBTripleIntLong.newBuilder();
		b.setOne(param1);
		b.setTwo(param2);
		b.setThree(param3);
		return b.build();
	}

	public static PBTripleLong pbTripleLong(long param1, long param2, long param3) {
		PBTripleLong.Builder b = PBTripleLong.newBuilder();
		b.setOne(param1);
		b.setTwo(param2);
		b.setThree(param3);
		return b.build();
	}

	public static PBFourInt pbFourInt(int param1, int param2, int param3, int param4) {
		PBFourInt.Builder b = PBFourInt.newBuilder();
		b.setParam1(param1);
		b.setParam2(param2);
		b.setParam3(param3);
		b.setParam4(param4);
		return b.build();
	}

	public static PBFourLongInt pbFourLongInt(long param1, int param2, int param3, int param4) {
		PBFourLongInt.Builder b = PBFourLongInt.newBuilder();
		b.setParam1(param1);
		b.setParam2(param2);
		b.setParam3(param3);
		b.setParam4(param4);
		return b.build();
	}

	public static PBFourLong pbFourLong(long param1, long param2, long param3, long param4) {
		PBFourLong.Builder b = PBFourLong.newBuilder();
		b.setParam1(param1);
		b.setParam2(param2);
		b.setParam3(param3);
		b.setParam4(param4);
		return b.build();
	}

	public static PBPairLongInt pbPairLongInt(long key, int value) {
		PBPairLongInt.Builder b = PBPairLongInt.newBuilder();
		b.setKey(key);
		b.setValue(value);
		return b.build();
	}

	public static PBPairString pbPairString(String key, String value) {
		PBPairString.Builder b = PBPairString.newBuilder();
		b.setKey(key);
		b.setValue(value);
		return b.build();
	}

	public static PBPairStringLong pbPairStringLong(String key, long value) {
		PBPairStringLong.Builder b = PBPairStringLong.newBuilder();
		b.setKey(key);
		b.setValue(value);
		return b.build();
	}

	public static PBIntList pbIntList(List<Integer> list) {
		PBIntList.Builder b = PBIntList.newBuilder();
		list.forEach(e -> {
			b.addList(e);
		});
		return b.build();
	}

	public static PBLongList pbLongList(List<Long> list) {
		PBLongList.Builder b = PBLongList.newBuilder();
		list.forEach(e -> {
			b.addList(e);
		});
		return b.build();
	}

	public static PBPairList pbPairList(List<Pair<Integer, Integer>> list) {
		PBPairList.Builder b = PBPairList.newBuilder();
		list.forEach(e -> {
			b.addList(pbPairInt(e.getLeft(), e.getRight()));
		});
		return b.build();
	}

	public static PBPairList pbPairList(Map<Integer, Integer> map) {
		PBPairList.Builder b = PBPairList.newBuilder();
		map.forEach((e, f) -> {
			b.addList(pbPairInt(e, f));
		});
		return b.build();
	}

	public static PBPairList pbPairList2(List<PBPairInt> renewadInfo) {
		PBPairList.Builder b = PBPairList.newBuilder();
		b.addAllList(renewadInfo);
		return b.build();
	}

	public static PBPairIntTriple pbPairIntTriple(int key, int param1, int param2, int param3) {
		return pbPairIntTriple(key, pbTriple(param1, param2, param3));
	}

	public static PBPairIntTriple pbPairIntTriple(int key, PBTriple triple) {
		PBPairIntTriple.Builder b = PBPairIntTriple.newBuilder();
		b.setKey(key);
		b.setValue(triple);
		return b.build();
	}

	public static PBListLongInt pbListLongInt(List<Pair<Long, Integer>> list) {
		PBListLongInt.Builder b = PBListLongInt.newBuilder();
		list.forEach(e -> {
			b.addPbList(pbPairLongInt(e.getLeft(), e.getRight()));
		});
		return b.build();
	}

	public static PBListLongInt pbListLongInt(Map<Long, Integer> m) {
		PBListLongInt.Builder b = PBListLongInt.newBuilder();
		m.forEach((e, f) -> {
			b.addPbList(pbPairLongInt(e, f));
		});
		return b.build();
	}

	public static PBPairIntIntList pbPairIntIntList(int key, List<Integer> list) {
		PBPairIntIntList.Builder b = PBPairIntIntList.newBuilder();
		b.setKey(key);
		b.setValue(pbIntList(list));
		return b.build();
	}

	public static PBPairListList pbPairListList(List<Map<Integer, Integer>> pairList) {
		PBPairListList.Builder b = PBPairListList.newBuilder();
		pairList.forEach(e -> b.addList(pbPairList(e)));
		return b.build();
	}

	public static PBPairLongList pbPairLongList(int key, List<Map<Integer, Integer>> list) {
		PBPairLongList.Builder b = PBPairLongList.newBuilder();
		b.setKey(key);
		b.setValue(pbPairListList(list));
		return b.build();
	}

	public static PBPairLongList pbPairLongList(long key, List<Map<Integer, Integer>> pairList) {
		PBPairLongList.Builder builder = PBPairLongList.newBuilder();
		builder.setKey(key);
		builder.setValue(pbPairListList(pairList));
		return builder.build();
	}

	public static PBPairTripleList pbPairTripleList(long key, List<PBTriple> pairList) {
		PBPairTripleList.Builder builder = PBPairTripleList.newBuilder();
		builder.setKey(key);
		builder.addAllList(pairList);
		return builder.build();
	}

	public static PBListInt pbListInt(int param1, long param2, List<Integer> list) {
		PBListInt.Builder b = PBListInt.newBuilder();
		b.setParam1(param1);
		b.setParam2(param2);
		b.addAllParam3(list);
		return b.build();
	}

	public static PBStringList pbStringList(List<String> list) {
		PBStringList.Builder b = PBStringList.newBuilder();
		b.addAllList(list);
		return b.build();
	}

	public static PBTripleList pbTripleList1(List<PBTriple> list) {
		PBTripleList.Builder b = PBTripleList.newBuilder();
		b.addAllList(list);
		return b.build();
	}

	public static PBTripleList pbTripleList(List<Triple<Integer, Integer, Integer>> list) {
		PBTripleList.Builder b = PBTripleList.newBuilder();
		list.forEach(e -> b.addList(pbTriple(e.getA(), e.getB(), e.getC())));
		return b.build();
	}


	public static PBFourIntLong pbFourIntLong(int param1, long param2, long param3, long param4) {
		PBFourIntLong.Builder b = PBFourIntLong.newBuilder();
		b.setParam1(param1);
		b.setParam2(param2);
		b.setParam3(param3);
		b.setParam4(param4);
		return b.build();
	}

	public static PBLongStringLong pbLongStringLong(long one, String two, long three) {
		PBLongStringLong.Builder b = PBLongStringLong.newBuilder();
		b.setOne(one);
		b.setTwo(two);
		b.setThree(three);
		return b.build();
	}

}
