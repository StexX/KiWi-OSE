CREATE OR REPLACE FUNCTION calculate_equity(create_time timestamp, action_time timestamp, unit_value double precision, action_type character varying) RETURNS double precision AS $$

DECLARE
    result double precision;
BEGIN
    IF action_type = 'ANNOTATE' THEN
        result = annotateAging(create_time, action_time, unit_value);
    end IF;
    
    IF action_type = 'COMMENT' THEN
        result = commentAging(create_time, action_time, unit_value);
    end IF;
    
    IF action_type = 'CREATE' THEN
        result = createAging(create_time, action_time, unit_value);
    end IF;
    
    IF action_type = 'DELETE' THEN
        result = deleteAging(create_time, action_time, unit_value);
    end IF;

    IF action_type = 'EDIT' THEN
        result = editAging(create_time, action_time, unit_value);
    end IF;
        
    IF action_type = 'ADDTYPE' THEN
        result = addtypeAging(create_time, action_time, unit_value);
    end IF;
    
    IF action_type = 'SHARE' THEN
        result = shareAging(create_time, action_time, unit_value);
    end IF;
    
    IF action_type = 'TWEET' THEN
        result = tweetAging(create_time, action_time, unit_value);
    end IF;
        
    IF action_type = 'VISIT' THEN
        result = visitAging(create_time, action_time, unit_value);
     ELSE
        result = generalAging(create_time, action_time, unit_value);
    end IF;

    RETURN result;
END;

$$ LANGUAGE plpgsql;