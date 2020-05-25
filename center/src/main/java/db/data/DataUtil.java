package db.data;

import java.util.zip.CRC32;

import util.MiscUtil;


public class DataUtil {

    public static long crc32(Object data) {
        if (null == data) {
            return 0;
        }
        CRC32 crc = new CRC32();
        byte [] b = MiscUtil.writeObjectAsBuffer(data);
        crc.update(b);
        return crc.getValue();
    }
}
