import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class OracleSqlRewrap {

    private String sql;
    private List<String> orderColumns;

    private boolean paginated = true;
    private long pageIdx = 1L;
    private long pageSize = 10L;

    private String internal_transitTable = "T_TRANSIT1";
    private String internal_rownumColumnName = "rowIdx";

    public OracleSqlRewrap(String sql) {
        this.sql = sql;
    }

    public WrapResult wrap() {
        StringBuilder wrappedSql = new StringBuilder();
        List<Object> paramList = new LinkedList<>();

        String tname = this.internal_transitTable;
        String rnCol = this.internal_rownumColumnName;

        wrappedSql.append("select * from (\n");
        wrappedSql.append("  select " + tname + ".*, rownum as " + rnCol + "  from (\n");
        wrappedSql.append("    ").append(this.sql).append("\n");
        wrappedSql.append("  ) " + tname + "\n");
        wrappedSql.append(") ");

        if (paginated) {
            long _pageIdx = (pageIdx < 1)?1:pageIdx;
            long _pageSize = (pageSize < 1)?10:pageSize;

            long start = (_pageIdx - 1) * _pageSize;
            long end = start + _pageSize;

            wrappedSql.append(" and " + rnCol + " > " + start + " and " + rnCol + " < " + end);
        }


        if ((this.getOrderColumns() != null) && (this.getOrderColumns().size() > 0)) {
            List<String> columnList = new LinkedList<>();
            this.getOrderColumns().forEach((columnName) -> {
                columnList.add(columnName.replaceAll("[^0-9a-zA-Z_-]", ""));
            });
            wrappedSql.append(" order by ");
            wrappedSql.append(StringUtils.join(columnList, ","));
        }

        return new WrapResult(wrappedSql.toString(), paramList);
    }



    public List<String> getOrderColumns() {
        return orderColumns;
    }

    public void setOrderColumns(List<String> orderColumns) {
        this.orderColumns = orderColumns;
    }

    public boolean isPaginated() {
        return paginated;
    }

    public void setPaginated(boolean paginated) {
        this.paginated = paginated;
    }

    public long getPageIdx() {
        return pageIdx;
    }

    public void setPageIdx(long pageIdx) {
        this.pageIdx = pageIdx;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public void setInternal_transitTable(String tableName) {
        if (tableName != null) {
            this.internal_transitTable = internal_transitTable;
        }
    }

    public void setInternal_rownumColumnName(String columnName) {
        if (columnName != null) {
            this.internal_rownumColumnName = internal_rownumColumnName;
        }
    }

    public static class WrapResult {
        private String sql;
        private List<Object> paramList;

        public WrapResult(String sql, List<Object> paramList) {
            this.sql = sql;
            this.paramList = paramList;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public List<Object> getParamList() {
            return paramList;
        }

        public void setParamList(List<Object> paramList) {
            this.paramList = paramList;
        }
    }
}
