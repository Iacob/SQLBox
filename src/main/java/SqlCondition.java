import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SqlCondition {

    public static class SqlConditionComponent {
        private String clause;
        private List<Object> params;
        public SqlConditionComponent(String clause, List<Object> params) {
            this.clause = clause;
            this.params = params;
        }
        public String getClause() {
            return clause;
        }
        public List<Object> getParams() {
            return params;
        }
    }

    public static interface SqlPredicateCommon {
        public SqlConditionComponent build();
    }

    public static class SqlPredicateEqual implements SqlPredicateCommon {
        private String column;
        private Object param;
        public SqlPredicateEqual(String column, Object param) {
            this.column = column;
            this.param = param;
        }

        @Override
        public SqlConditionComponent build() {
            List<Object> paramList = new LinkedList<>();
            paramList.add(param);
            return new SqlConditionComponent(column + " = ?", paramList);
        }
    }

    public static class SqlPredicateNotEqual implements SqlPredicateCommon {
        private String column;
        private Object param;
        public SqlPredicateNotEqual(String column, Object param) {
            this.column = column;
            this.param = param;
        }

        @Override
        public SqlConditionComponent build() {
            List<Object> paramList = new LinkedList<>();
            paramList.add(param);
            return new SqlConditionComponent(column + " <> ?", paramList);
        }
    }

    public static class SqlPredicateInList implements SqlPredicateCommon {
        private String column;
        private Object[] params;
        public SqlPredicateInList(String column, Object... params) {
            this.column = column;
            this.params = params;
        }

        @Override
        public SqlConditionComponent build() {

            if ((this.params == null) || (this.params.length < 1)) {
                return new SqlConditionComponent("", new ArrayList<>());
            }

            StringBuilder clause = new StringBuilder();

            for (Object param : params) {
                if (clause.length() > 0) {
                    clause.append(",");
                }
                clause.append("?");
            }

            return new SqlConditionComponent(column + " in (" + clause.toString() + ")", Arrays.asList(params));
        }
    }
}
