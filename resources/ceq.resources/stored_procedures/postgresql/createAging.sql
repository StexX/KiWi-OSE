CREATE OR REPLACE FUNCTION createAging(create_time timestamp, action_time timestamp, unit_value double precision) RETURNS double precision AS $$
DECLARE
    time_diff interval := action_time - create_time;
    result double precision;
BEGIN
    result = unit_value - extract(epoch FROM time_diff) / 60;
    
    IF result < 0 THEN
        result = 0;
    END IF;
    
    RETURN result;
END;

$$ LANGUAGE plpgsql;