CREATE OR REPLACE FUNCTION calculate_equity(action_time timestamp, content_item_id bigint) RETURNS double precision AS $$
BEGIN
    RETURN SUM(calculate_equity(activity.created, action_time, activity.equity, activity.dtype)) 
    FROM activity 
    WHERE (activity.contentitem_id = content_item_id) AND (activity.created <= action_time);
END;

$$ LANGUAGE plpgsql;