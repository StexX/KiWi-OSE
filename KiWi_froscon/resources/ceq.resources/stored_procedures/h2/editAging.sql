CREATE ALIAS editAging AS $$
    import org.h2.tools.SimpleResultSet;
    import java.sql.ResultSet;
    import java.sql.Types;
    @CODE 
    ResultSet compute(long createTime, long actionTime, double equityValue) {
        SimpleResultSet result = new SimpleResultSet(); 
        result.addColumn("rst", Types.DOUBLE, 10, 10);
        long timeDiff = (actionTime - createTime) * 1000 * 60;
        double resultValue = equityValue - timeDiff;
        
        if (resultValue < 0) {
            result.addRow(0);
        } else {
            result.addRow(resultValue);
        }
        return result; 
    }
$$;