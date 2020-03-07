import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParameterSetter {

    public static void setParameters(PreparedStatement stmt, Object[] params) throws SQLException {
        if (params == null) {
            return;
        }

        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param == null) {
                stmt.setObject(i+1, null);
            }else if (param instanceof BigDecimal) {
                stmt.setBigDecimal(i+1, (BigDecimal)param);
            }else if (param instanceof java.sql.Blob) {
                stmt.setBlob(i+1, (java.sql.Blob)param);
            }else if (param instanceof Boolean) {
                stmt.setBoolean(i+1, (Boolean)param);
            }else if (param instanceof Byte) {
                stmt.setByte(i+1, (Byte)param);
            }else if (param instanceof byte[]) {
                stmt.setBytes(i+1, (byte[]) param);
            }else if (param instanceof java.sql.Clob) {
                stmt.setClob(i+1, (java.sql.Clob)param);
            }else if (param instanceof java.util.Date) {
                stmt.setDate(i+1, new java.sql.Date(((java.util.Date)param).getTime()));
            }else if (param instanceof Double) {
                stmt.setDouble(i+1, (Double) param);
            }else if (param instanceof Float) {
                stmt.setFloat(i+1, (Float)param);
            }else if (param instanceof Integer) {
                stmt.setInt(i+1, (Integer) param);
            }else if (param instanceof Long) {
                stmt.setLong(i+1, (Long) param);
            }else if (param instanceof java.sql.NClob) {
                stmt.setNClob(i+1, (java.sql.NClob)param);
            }else if (param instanceof Short) {
                stmt.setShort(i+1, (Short) param);
            }else if (param instanceof String) {
                stmt.setString(i+1, (String)param);
            }else if (param instanceof URL) {
                stmt.setURL(i+1, (URL) param);
            }else {
                stmt.setObject(i+1, param);
            }

        }
    }
}
