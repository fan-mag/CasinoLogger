package helpers

import model.Event
import java.sql.PreparedStatement

class Logger {
    companion object {
        @JvmStatic
        fun log(event: Event) {
            val preparedStatement = getPreparedStatement(event)
            val resultSet = preparedStatement.executeQuery()
            resultSet.next()
            event.id = resultSet.getInt("id")
            event.date = resultSet.getTimestamp("date")
            event.service = resultSet.getString("service")
        }

        private fun getPreparedStatement(event: Event): PreparedStatement {
            if (event.service != null) {
                val query = "INSERT INTO log (service, message) VALUES (?, ?) RETURNING *"
                val preparedStatement = Database.conn.prepareStatement(query)
                preparedStatement.setString(1, event.service)
                preparedStatement.setString(2, event.message)
                return preparedStatement
            } else {
                val query = "INSERT INTO log (service, message) VALUES (DEFAULT, ?) RETURNING *"
                val preparedStatement = Database.conn.prepareStatement(query)
                preparedStatement.setString(1, event.message)
                return preparedStatement
            }
        }

    }
}