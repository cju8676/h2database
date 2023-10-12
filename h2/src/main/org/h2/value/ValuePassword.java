package org.h2.value;

import org.h2.engine.CastDataProvider;
import org.h2.util.StringUtils;

import static org.h2.value.ValueVarchar.EMPTY;

public class ValuePassword extends ValueStringBase {

    private ValuePassword(String value) {
        super(value);
    }

    @Override
    public StringBuilder getSQL(StringBuilder builder, int sqlFlags) {
        return StringUtils.quoteStringSQL(builder, value);
    }

    @Override
    public int getValueType() {
        return SECURE_PASSWORD;
    }

    public static Value get(String s) {
        return get(s, null);
    }

    public static Value get(String s, CastDataProvider provider) {
        if (s.isEmpty()) {
            return provider != null && provider.getMode().treatEmptyStringsAsNull ? ValueNull.INSTANCE : EMPTY;
        }
        ValuePassword obj = new ValuePassword(StringUtils.cache(s));
        if (s.length() > 100) {
            return obj;
        }
        return Value.cache(obj);
    }
}
