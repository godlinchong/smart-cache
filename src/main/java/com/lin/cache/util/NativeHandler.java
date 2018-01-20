package com.lin.cache.util;

/**
 * 类名称:  NativeHandler <br>
 * 类描述:转码工具 <br>
 *
 * @author: chong.lin
 * @date: 2018/1/19 下午11:36
 * @company: 易宝支付(YeePay)
 */
import java.util.Date;

public class NativeHandler {

	public static final int MARKER_BYTE = 1;
	public static final int MARKER_BOOLEAN = 8192;
	public static final int MARKER_INTEGER = 4;
	public static final int MARKER_LONG = 16384;
	public static final int MARKER_CHARACTER = 16;
	public static final int MARKER_STRING = 32;
	public static final int MARKER_STRINGBUFFER = 64;
	public static final int MARKER_FLOAT = 128;
	public static final int MARKER_SHORT = 256;
	public static final int MARKER_DOUBLE = 512;
	public static final int MARKER_DATE = 1024;
	public static final int MARKER_STRINGBUILDER = 2048;
	public static final int MARKER_BYTEARR = 4096;
	public static final int F_COMPRESSED = 2;
	public static final int F_SERIALIZED = 8;

	public NativeHandler() {
	}

	public static boolean isHandled(Object value) {
		return value instanceof Byte || value instanceof Boolean || value instanceof Integer || value instanceof Long || value instanceof Character || value instanceof String || value instanceof StringBuffer || value instanceof Float || value instanceof Short || value instanceof Double || value instanceof Date || value instanceof StringBuilder || value instanceof byte[];
	}

	public static int getMarkerFlag(Object value) {
		return value instanceof Byte ? 1 : (value instanceof Boolean ? 8192 : (value instanceof Integer ? 4 : (value instanceof Long ? 16384 : (value instanceof Character ? 16 : (value instanceof String ? 32 : (value instanceof StringBuffer ? 64 : (value instanceof Float ? 128 : (value instanceof Short ? 256 : (value instanceof Double ? 512 : (value instanceof Date ? 1024 : (value instanceof StringBuilder ? 2048 : (value instanceof byte[] ? 4096 : -1))))))))))));
	}

	public static byte[] encode(Object value) throws Exception {
		return value instanceof Byte ? encode((Byte) value) : (value instanceof Boolean ? encode((Boolean) value) : (value instanceof Integer ? encode(((Integer) value).intValue()) : (value instanceof Long ? encode(((Long) value).longValue()) : (value instanceof Character ? encode((Character) value) : (value instanceof String ? encode((String) value) : (value instanceof StringBuffer ? encode((StringBuffer) value) : (value instanceof Float ? encode(((Float) value).floatValue()) : (value instanceof Short ? encode((Short) value) : (value instanceof Double ? encode(((Double) value).doubleValue()) : (value instanceof Date ? encode((Date) value) : (value instanceof StringBuilder ? encode((StringBuilder) value) : (value instanceof byte[] ? encode((byte[]) value) : null))))))))))));
	}

	protected static byte[] encode(Byte value) {
		byte[] b = new byte[]{value.byteValue()};
		return b;
	}

	protected static byte[] encode(Boolean value) {
		byte[] b = new byte[1];
		if (value.booleanValue()) {
			b[0] = 1;
		} else {
			b[0] = 0;
		}

		return b;
	}

	protected static byte[] encode(int value) {
		return getBytes(value);
	}

	protected static byte[] encode(long value) throws Exception {
		return getBytes(value);
	}

	protected static byte[] encode(Date value) {
		return getBytes(value.getTime());
	}

	protected static byte[] encode(Character value) {
		return encode(value.charValue());
	}

	protected static byte[] encode(String value) throws Exception {
		return value.getBytes("UTF-8");
	}

	protected static byte[] encode(StringBuffer value) throws Exception {
		return encode(value.toString());
	}

	protected static byte[] encode(float value) throws Exception {
		return encode(Float.floatToIntBits(value));
	}

	protected static byte[] encode(Short value) throws Exception {
		return encode(value.shortValue());
	}

	protected static byte[] encode(double value) throws Exception {
		return encode(Double.doubleToLongBits(value));
	}

	protected static byte[] encode(StringBuilder value) throws Exception {
		return encode(value.toString());
	}

	protected static byte[] encode(byte[] value) {
		return value;
	}

	protected static byte[] getBytes(long value) {
		byte[] b = new byte[]{(byte) ((int) (value >> 56 & 255L)), (byte) ((int) (value >> 48 & 255L)), (byte) ((int) (value >> 40 & 255L)), (byte) ((int) (value >> 32 & 255L)), (byte) ((int) (value >> 24 & 255L)), (byte) ((int) (value >> 16 & 255L)), (byte) ((int) (value >> 8 & 255L)), (byte) ((int) (value >> 0 & 255L))};
		return b;
	}

	protected static byte[] getBytes(int value) {
		byte[] b = new byte[]{(byte) (value >> 24 & 255), (byte) (value >> 16 & 255), (byte) (value >> 8 & 255), (byte) (value >> 0 & 255)};
		return b;
	}

	public static Object decode(byte[] b, int flag) throws Exception {
		return b.length < 1 ? null : ((flag & 1) == 1 ? decodeByte(b) : ((flag & 8192) == 8192 ? decodeBoolean(b) : ((flag & 4) == 4 ? decodeInteger(b) : ((flag & 16384) == 16384 ? decodeLong(b) : ((flag & 16) == 16 ? decodeCharacter(b) : ((flag & 32) == 32 ? decodeString(b) : ((flag & 64) == 64 ? decodeStringBuffer(b) : ((flag & 128) == 128 ? decodeFloat(b) : ((flag & 256) == 256 ? decodeShort(b) : ((flag & 512) == 512 ? decodeDouble(b) : ((flag & 1024) == 1024 ? decodeDate(b) : ((flag & 2048) == 2048 ? decodeStringBuilder(b) : ((flag & 4096) == 4096 ? decodeByteArr(b) : null)))))))))))));
	}

	protected static Byte decodeByte(byte[] b) {
		return new Byte(b[0]);
	}

	protected static Boolean decodeBoolean(byte[] b) {
		boolean value = b[0] == 1;
		return value ? Boolean.TRUE : Boolean.FALSE;
	}

	public static Integer decodeInteger(byte[] b) {
		return new Integer(toInt(b));
	}

	protected static Long decodeLong(byte[] b) throws Exception {
		return new Long(toLong(b));
	}

	protected static Character decodeCharacter(byte[] b) {
		return new Character((char) decodeInteger(b).intValue());
	}

	protected static String decodeString(byte[] b) throws Exception {
		return new String(b, "UTF-8");
	}

	protected static StringBuffer decodeStringBuffer(byte[] b) throws Exception {
		return new StringBuffer(decodeString(b));
	}

	protected static Float decodeFloat(byte[] b) throws Exception {
		Integer l = decodeInteger(b);
		return new Float(Float.intBitsToFloat(l.intValue()));
	}

	protected static Short decodeShort(byte[] b) throws Exception {
		return new Short((short) decodeInteger(b).intValue());
	}

	protected static Double decodeDouble(byte[] b) throws Exception {
		Long l = decodeLong(b);
		return new Double(Double.longBitsToDouble(l.longValue()));
	}

	protected static Date decodeDate(byte[] b) {
		return new Date(toLong(b));
	}

	protected static StringBuilder decodeStringBuilder(byte[] b) throws Exception {
		return new StringBuilder(decodeString(b));
	}

	protected static byte[] decodeByteArr(byte[] b) {
		return b;
	}

	protected static int toInt(byte[] b) {
		return ((b[3] & 255) << 32) + ((b[2] & 255) << 40) + ((b[1] & 255) << 48) + ((b[0] & 255) << 56);
	}

	protected static long toLong(byte[] b) {
		return ((long) b[7] & 255L) + (((long) b[6] & 255L) << 8) + (((long) b[5] & 255L) << 16) + (((long) b[4] & 255L) << 24) + (((long) b[3] & 255L) << 32) + (((long) b[2] & 255L) << 40) + (((long) b[1] & 255L) << 48) + (((long) b[0] & 255L) << 56);
	}
}
