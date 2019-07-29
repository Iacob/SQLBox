import groovy.beans.Bindable

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class GroovySqlBox {

    static class QueryResult {
        private PreparedStatement statement
        private ResultSet resultset

        public QueryResult(PreparedStatement statement, ResultSet resultset) {
            this.statement = statement
            this.resultset = resultset
        }

        PreparedStatement getStatement() {
            return statement
        }

        ResultSet getResultset() {
            return resultset
        }
    }
    static class UpdateResult {

        private PreparedStatement statement
        private int rows

        public UpdateResult(PreparedStatement statement, int rows) {
            this.statement = statement
            this.rows = rows
        }

        PreparedStatement getStatement() {
            return statement
        }

        int getRows() {
            return rows
        }
    }
    static class SqlBoxRawValue {
        private String value
        SqlBoxRawValue(String value) {
            this.value = values
        }
        String getValue() {
            return value
        }
    }
    static class SqlBoxValueWrapper {
        private String wrapper
        private Object value
        SqlBoxValueWrapper(String wrapper, Object value) {
            this.wrapper = wrapper
            this.value = value
        }
        String getWrapper() {
            return wrapper
        }
        Object getValue() {
            return value
        }
    }

    static GroovySqlBox createInsert(String tb, Map<String, Object> map) {
        GroovySqlBox sqlBox = new GroovySqlBox()
        def columns = new StringBuilder()
        def values = new StringBuilder()
        map.forEach({k, v ->
            if (v instanceof SqlBoxRawValue) {
                values.append(v).append(",")
            }else if (v instanceof SqlBoxValueWrapper) {
                values.append(v.wrapper).append(",")
                sqlBox.append(null, v.value)
            }else {
                values.append("?,")
                sqlBox.append(null, v)
            }
        })
        if (columns.length() > 0) {
            columns.deleteCharAt(columns.length - 1)
        }
        if (values.length() > 0) {
            values.deleteCharAt(values.length - 1)
        }
        String sql = "insert into $tb ($columns) values ($values)"
        sqlBox.append(sql, null)
        return sqlBox
    }

    private StringBuilder sql = new StringBuilder()
    private LinkedList<Object> params = new LinkedList<>()

    GroovySqlBox(String sql, Object... params) {
        this.append(sql, params)
    }

    public void append(String clause, Object... params) {
        if (clause != null) {
            sql.append(clause)
        }
        if (params != null) {
            this.params.addAll(params)
        }
    }

    public void appendLine(String clause, Object... params) {
        sql.append(clause + "\n")
        this.params.addAll(params)
    }

    public UpdateResult test() {
        def updateResult = new UpdateResult(null, 5)
        //println updateResult
        return updateResult
    }

    private void setStatementParams(PreparedStatement stmt) {
        for (Object param : params) {
            stmt.setObject(param)
        }
    }

    public QueryResult query(Connection connection) {
        def stmt = connection.prepareStatement(this.sql)
        this.setStatementParams(stmt)
        def resultSet = stmt.executeQuery()
        return new QueryResult(stmt, resultSet)
    }

    public UpdateResult update(Connection connection) {
        def stmt = connection.prepareStatement(this.sql)
        this.setStatementParams(stmt)
        def rows = stmt.executeUpdate()
        return new UpdateResult(stmt, rows)
    }
}
