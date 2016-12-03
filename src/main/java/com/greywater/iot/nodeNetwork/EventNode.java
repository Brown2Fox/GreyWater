package com.greywater.iot.nodeNetwork;

import com.greywater.iot.persistence.PersistManager;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EventNode extends Node<String> {

    private String importance;
    private String message;

    EventNode(List<Node> inputs) {
        super(inputs);
    }

    EventNode(List<Node> inputs, String importance, String message) {
        this.importance = importance;
        this.message = message;
        this.inputs = inputs;
    }

    public void writeEvent(String nodeID, Connection conn) throws SQLException, NamingException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.EVENTS_TABLE(ID, EVENT_TIME, EVENT_MESSAGE, EVENT_IMPORTANCE, NODE_ID) values(?,?,?,?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        String tableID = uid.randomUUID().toString();

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(now.getTime());

        pstmt.setString(1, tableID);
        pstmt.setTimestamp(2, currentTimestamp);
        pstmt.setString(3, message);
        pstmt.setString(4, importance);
        pstmt.setString(5, nodeID);
        
        pstmt.executeQuery();
    }

    @Override
    void eval() {
        inputs.forEach(node -> {
            if ((Boolean)node.getState()) {
                Connection conn = null;
                try {
                    conn = PersistManager.newConnection();
                    writeEvent(node.getId(), conn);
                    conn.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


