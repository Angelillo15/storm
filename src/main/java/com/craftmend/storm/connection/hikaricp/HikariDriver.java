package com.craftmend.storm.connection.hikaricp;

import com.craftmend.storm.connection.StormDriver;
import com.craftmend.storm.dialect.Dialect;
import com.craftmend.storm.dialect.mariadb.MariaDialect;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.*;

public class HikariDriver implements StormDriver {

    @Getter
    private Dialect dialect = new MariaDialect();
    private HikariDataSource ds;

    public HikariDriver(HikariConfig hikariConfig) throws SQLException {
        ds = new HikariDataSource(hikariConfig);
    }

    @Override
    public void executeQuery(String query, Callback callback, Object... arguments) throws Exception {
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                for (int i = 0; i < arguments.length; i++) {
                    ps.setObject(i + 1, arguments[i]);
                }
                callback.onAccept(ps.executeQuery());
            }
        }
    }

    @Override
    public boolean execute(String query) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            try (Statement ps = conn.createStatement()) {
                return ps.execute(query);
            }
        }
    }

    @Override
    public int executeUpdate(String query, Object... arguments) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                for (int i = 0; i < arguments.length; i++) {
                    ps.setObject(i + 1, arguments[i]);
                }
                return ps.executeUpdate();
            }
        }
    }

    @Override
    public DatabaseMetaData getMeta() throws SQLException {
        return ds.getConnection().getMetaData();
    }

    @Override
    public boolean isOpen() {
        return ds != null && !ds.isClosed();
    }

    @Override
    public void close() {
        if (isOpen()) {
            ds.close();
        }
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }
}
