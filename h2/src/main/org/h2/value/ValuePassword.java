package org.h2.value;

import org.h2.engine.CastDataProvider;

public class ValuePassword extends ValueStringBase {

    ValuePassword(String v) {
        super(v);
    }

    @Override
    public StringBuilder getSQL(StringBuilder builder, int sqlFlags) {
        return new StringBuilder("SECURE_PASSWORD");
    }

    @Override
    public int getValueType() {
        return 0;
    }
}
