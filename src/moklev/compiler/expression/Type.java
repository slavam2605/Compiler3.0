package moklev.compiler.expression;

/**
 * @author Moklev Vyacheslav
 */
public enum Type {
    BOOL(1), INT8(1), INT16(2), INT32(4), INT64(8), FLOAT(4), DOUBLE(8);

    private int sizeOf;

    Type(int sizeOf) {
        this.sizeOf = sizeOf;
    }

    public int getSizeOf() {
        return sizeOf;
    }

    public static Type fromString(String s) {
        switch (s) {
            case "bool":
                return BOOL;
            case "int8":
                return INT8;
            case "int16":
                return INT16;
            case "int32":
                return INT32;
            case "int64":
                return INT64;
            case "float":
                return FLOAT;
            case "double":
                return DOUBLE;
            default:
                throw new IllegalArgumentException("Unknown value: " + s);
        }
    }
}
