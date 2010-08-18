CREATE ALIAS calculate_equity AS $$
    import java.sql.Connection;
    import java.sql.ResultSet;
    import java.sql.Statement;
    import java.sql.SQLException;
    import java.util.Date;
    @CODE 
    double compute(Connection conn, Date createTime, Date actionTime, double equityValue, String actionType) {
        long createDate = createTime.getTime();
        long actionDate = actionTime.getTime();

        if ("ANNOTATE".equals(actionType)) {
            String sqlToDo = "CALL annotateAging(" + createDate + "," + actionDate + "," + equityValue + ")";
            try {
                ResultSet srs =  conn.createStatement().executeQuery(sqlToDo);
                srs.absolute(1);
                return srs.getDouble(1); 
            } catch (Exception exception) {}
        } else if ("COMMENT".equals(actionType)) {
            String sqlToDo = "CALL commentAging(" + createDate + "," + actionDate + "," + equityValue + ")";
            try {
                ResultSet srs =  conn.createStatement().executeQuery(sqlToDo);
                srs.absolute(1);
                return srs.getDouble(1); 
            } catch (Exception exception) {}
        } else if ("CREATE".equals(actionType)) {
            String sqlToDo = "CALL createAging(" + createDate + "," + actionDate + "," + equityValue + ")";
            try {
                ResultSet srs =  conn.createStatement().executeQuery(sqlToDo);
                srs.absolute(1);
                return srs.getDouble(1); 
            } catch (Exception exception) {}
        } else if ("DELETE".equals(actionType)) {
                    String sqlToDo = "CALL deleteAging(" + createDate + "," + actionDate + "," + equityValue + ")";
            try {
                ResultSet srs =  conn.createStatement().executeQuery(sqlToDo);
                srs.absolute(1);
                return srs.getDouble(1); 
            } catch (Exception exception) {}

        } else if ("EDIT".equals(actionType)) {
            String sqlToDo = "CALL editAging(" + createDate + "," + actionDate + "," + equityValue + ")";
            try {
                ResultSet srs =  conn.createStatement().executeQuery(sqlToDo);
                srs.absolute(1);
                return srs.getDouble(1); 
            } catch (Exception exception) {}
        } else if ("ADDTYPE".equals(actionType)) {
            String sqlToDo = "CALL addtypeAging(" + createDate + "," + actionDate + "," + equityValue + ")";
            try {
                ResultSet srs =  conn.createStatement().executeQuery(sqlToDo);
                srs.absolute(1);
                return srs.getDouble(1); 
            } catch (Exception exception) {}

        } else if ("SHARE".equals(actionType)) {
            String sqlToDo = "CALL shareAging(" + createDate + "," + actionDate + "," + equityValue + ")";
            try {
                ResultSet srs =  conn.createStatement().executeQuery(sqlToDo);
                srs.absolute(1);
                return srs.getDouble(1); 
            } catch (Exception exception) {}

        } else if ("VISIT".equals(actionType)) {
            String sqlToDo = "CALL visitAging(" + createDate + "," + actionDate + "," + equityValue + ")";
            try {
                ResultSet srs =  conn.createStatement().executeQuery(sqlToDo);
                srs.absolute(1);
                return srs.getDouble(1); 
            } catch (Exception exception) {}

        } else {
            String sqlToDo = "CALL generalAging(" + createDate + "," + actionDate + "," + equityValue + ")";
            try {
                ResultSet srs =  conn.createStatement().executeQuery(sqlToDo);
                srs.absolute(1);
                return srs.getDouble(1); 
            } catch (Exception exception) {}
        }
        
        return 0;
    }
    
$$;
