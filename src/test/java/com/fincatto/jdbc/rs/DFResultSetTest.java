package com.fincatto.jdbc.rs;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class DFResultSetTest {

    @Test
    public void getColumnKeyTest() throws SQLException {
        Assert.assertEquals("table.column", DFResultSet.getColumnKey("table", "column"));
    }
}
