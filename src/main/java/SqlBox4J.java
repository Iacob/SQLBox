import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 *
 * !!!Warning!!!
 *
 * Still under heavy construction. DO NOT USE.
 */
public class SqlBox4J {

    static class QueryResult {
        private PreparedStatement statement;
        private ResultSet resultset;

        public QueryResult(PreparedStatement statement, ResultSet resultset) {
            this.statement = statement;
            this.resultset = resultset;
        }

        PreparedStatement getStatement() {
            return statement;
        }

        ResultSet getResultset() {
            return resultset;
        }
    }
    static class UpdateResult {

        private PreparedStatement statement;
        private int rows;

        public UpdateResult(PreparedStatement statement, int rows) {
            this.statement = statement;
            this.rows = rows;
        }

        PreparedStatement getStatement() {
            return statement;
        }

        int getRows() {
            return rows;
        }
    }
    static class SqlBoxRawValue {
        private String value;
        SqlBoxRawValue(String value) {
            this.value = value;
        }
        String getValue() {
            return value;
        }
    }
    static class SqlBoxValueWrapper {
        private String wrapper;
        private Object value;
        SqlBoxValueWrapper(String wrapper, Object value) {
            this.wrapper = wrapper;
            this.value = value;
        }
        String getWrapper() {
            return wrapper;
        }
        Object getValue() {
            return value;
        }
    }

    static SqlBox4J createInsert(String tb, Map<String, Object> map) {
        SqlBox4J sqlBox = new SqlBox4J();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        map.forEach((k, v) -> {});
        map.forEach((k, v) -> {
            if (v instanceof SqlBoxRawValue) {
                values.append(v).append(",");
            }else if (v instanceof SqlBoxValueWrapper) {
                values.append(((SqlBoxValueWrapper) v).getWrapper()).append(",");
                sqlBox.append(null, ((SqlBoxValueWrapper) v).getValue());
            }else {
                values.append("?,");
                sqlBox.append(null, v);
            }}
        );
        if (columns.length() > 0) {
            columns.deleteCharAt(columns.length() - 1);
        }
        if (values.length() > 0) {
            values.deleteCharAt(values.length() - 1);
        }
        String sql = "insert into $tb (" + columns.toString() + ") values (" + values.toString() + ")";
        sqlBox.append(sql, null);
        return sqlBox;
    }

    private StringBuilder sql = new StringBuilder();
    private LinkedList<Object> params = new LinkedList<>();

    public SqlBox4J() {
    }

    public SqlBox4J(String sql, Object... params) {
        this.append(sql, params);
    }

    public void append(String clause, Object... params) {
        if (clause != null) {
            sql.append(clause);
        }
        if (params != null) {
            this.params.addAll(Arrays.asList(params));
        }
    }

    public void appendLine(String clause, Object... params) {
        sql.append(clause + "\n");
        this.params.addAll(Arrays.asList(params));
    }

    public UpdateResult test() {
        UpdateResult updateResult = new UpdateResult(null, 5);
        //println updateResult
        return updateResult;
    }

    private void setStatementParams(PreparedStatement stmt) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            stmt.setObject((i+1), params.get(i));
        }
    }

    public QueryResult query(Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(this.sql.toString());
        this.setStatementParams(stmt);
        ResultSet resultSet = stmt.executeQuery();
        return new QueryResult(stmt, resultSet);
    }

    public UpdateResult update(Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(this.sql.toString());
        this.setStatementParams(stmt);
        int rows = stmt.executeUpdate();
        return new UpdateResult(stmt, rows);
    }
}
