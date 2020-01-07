package helpers

import CasinoLib.model.Event
import java.sql.PreparedStatement

class Logger {
    companion object {

        fun log(events: Array<Event>) {
            val queryBuilder = StringBuilder("insert into log (service, message) values \n")
            events.forEach { queryBuilder.append(getQueryStringForEvent(it)) }
            queryBuilder.setLength(queryBuilder.length - 2)
            queryBuilder.append(" returning id, date, service")
            val preparedStatement = Database.conn.prepareStatement(queryBuilder.toString())
            var index = 1
            events.forEach { index = prepareStatement(preparedStatement, index, it) }
            val resultSet = preparedStatement.executeQuery()
            events.forEach {
                run {
                    resultSet.next()
                    it.id = resultSet.getInt("id")
                    it.date = resultSet.getTimestamp("date")
                    it.service = resultSet.getString("service")
                }
            }
        }

        @Deprecated(message = "Too slow logging message one by one")
        fun log(event: Event) {
            val preparedStatement = getPreparedStatement(event)
            val resultSet = preparedStatement.executeQuery()
            resultSet.next()
            event.id = resultSet.getInt("id")
            event.date = resultSet.getTimestamp("date")
            event.service = resultSet.getString("service")
        }

        private fun getQueryStringForEvent(event: Event): String {
            return if (event.service != null)
                "(?, ?), "
            else
                "(DEFAULT, ?), "
        }

        private fun prepareStatement(preparedStatement: PreparedStatement, index: Int, event: Event): Int {
            return if (event.service != null) {
                preparedStatement.setString(index, event.service)
                preparedStatement.setString(index + 1, event.message)
                index + 2
            } else {
                preparedStatement.setString(index, event.message)
                index + 1
            }
        }

        @Deprecated(message = "Old")
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