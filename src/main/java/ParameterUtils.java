import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ParameterUtils {

    public static Object getObjectParameter(Object[] args, String name) {
        if (args == null) {
            return null;
        }
        if (name == null) {
            return null;
        }
        int i = 0;
        while (true) {
            if (i >= args.length) {
                return null;
            }
            String paramName = null;
            try { paramName = (String) args[i]; }catch (Throwable t) {}
            if (paramName == null) {
                return null;
            }
            i++;

            if (i >= args.length) {
                return null;
            }
            Object paramValue = args[i];

            if ((name + ":").equals(paramName)) {
                return paramValue;
            }
        }
    }

    public static String getStringParameter(Object[] args, String name) {
        Object resultObject = getObjectParameter(args, name);
        String result = null;
        try { result = (String)resultObject; }catch (Throwable t) {}
        return result;
    }

    public static String getStringParameter(Object[] args, String name, String defaultValue) {
        String stringResult = getStringParameter(args, name);
        if (stringResult != null) {
            return stringResult;
        }else {
            return defaultValue;
        }
    }

    public static Boolean getBooleanParameter(Object[] args, String name) {
        Object resultObject = getObjectParameter(args, name);
        Boolean result = null;
        try { result = (Boolean)resultObject; }catch (Throwable t) {}
        return result;
    }

    public static Boolean getBooleanParameter(Object[] args, String name, Boolean defaultValue) {
        Boolean booleanResult = getBooleanParameter(args, name);
        if (booleanResult != null) {
            return booleanResult;
        }else {
            return defaultValue;
        }
    }

    public static Integer getIntegerParameter(Object[] args, String name) {
        Object resultObject = getObjectParameter(args, name);
        Integer result = null;
        try { result = (Integer)resultObject; }catch (Throwable t) {}
        return result;
    }

    public static Integer getIntegerParameter(Object[] args, String name, Integer defaultValue) {
        Integer integerResult = getIntegerParameter(args, name);
        if (integerResult != null) {
            return integerResult;
        }else {
            return defaultValue;
        }
    }

    public static Long getLongParameter(Object[] args, String name) {
        Object resultObject = getObjectParameter(args, name);
        Long result = null;
        try { result = (Long)resultObject; }catch (Throwable t) {}
        return result;
    }

    public static Long getLongParameter(Object[] args, String name, Long defaultValue) {
        Long longResult = getLongParameter(args, name);
        if (longResult != null) {
            return longResult;
        }else {
            return defaultValue;
        }
    }

    public static <T> T getTypedParameter(Object[] args, String name) {
        Object resultObject = getObjectParameter(args, name);
        T result = null;
        try { result = (T)resultObject; }catch (Throwable t) {}
        return result;
    }

    public static <T> T getTypedParameter(Object[] args, String name, T defaultValue) {
        T typedResult = getTypedParameter(args, name);
        if (typedResult != null) {
            return typedResult;
        }else {
            return defaultValue;
        }
    }

    public static Object iterateParamValues(Object[] args, BiConsumer<String, Object> paramValueConsumer) {
        if (args == null) {
            return null;
        }
        int i = 0;
        while (true) {
            if (i >= args.length) {
                return null;
            }
            String paramName = null;
            try { paramName = (String) args[i]; }catch (Throwable t) {}
            if (paramName == null) {
                return null;
            }
            i++;

            if (i >= args.length) {
                return null;
            }
            Object paramValue = args[i];

            String paramNameFormal = paramName.trim();
            if (paramNameFormal.endsWith(":") && (paramNameFormal.length() > 0)) {
                paramValueConsumer.accept(paramNameFormal.substring(0, (paramNameFormal.length() - 1)), paramValue);
            }
        }
    }
}
