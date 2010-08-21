CREATE OR REPLACE FUNCTION select_equity_times(start_time timestamp without time zone, end_time timestamp without time zone, step integer) RETURNS bigint AS $$

DECLARE
    time_diff interval := (end_time - start_time) / step;
    result timestamp[];
BEGIN
    DROP TABLE IF EXISTS equity_time_intervals;
    
    CREATE TABLE equity_time_intervals (
        id bigint NOT NULL,
        compute_time timestamp
    ) WITH (OIDS=FALSE);
    ALTER TABLE equity_time_intervals OWNER TO kiwi;

    FOR i IN 0..step LOOP
        result[i] := (i * time_diff) + start_time;
        INSERT INTO equity_time_intervals VALUES (i, result[i]);
    end LOOP;
      
RETURN 0;
END;

$$ LANGUAGE plpgsql;
